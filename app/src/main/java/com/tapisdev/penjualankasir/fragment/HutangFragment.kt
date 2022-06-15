package com.tapisdev.penjualankasir.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.activity.TambahBarangActivity
import com.tapisdev.penjualankasir.activity.TambahHutangActivity
import com.tapisdev.penjualankasir.adapter.AdapterHutang
import com.tapisdev.penjualankasir.adapter.AdapterTransaksi
import com.tapisdev.penjualankasir.databinding.FragmentHomeBinding
import com.tapisdev.penjualankasir.databinding.FragmentHutangBinding
import com.tapisdev.penjualankasir.databinding.FragmentStokBinding
import com.tapisdev.penjualankasir.model.Hutang
import com.tapisdev.penjualankasir.model.Transaksi
import com.tapisdev.penjualankasir.model.UserPreference
import com.tapisdev.penjualankasir.response.HutangResponse
import com.tapisdev.penjualankasir.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HutangFragment : Fragment() {

    private var _binding: FragmentHutangBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //lateinit var binding_shimmer : ShimmerSuratBinding
    lateinit var shimmerFrameLayout : ShimmerFrameLayout
    lateinit var mUserPref : UserPreference
    lateinit var adapter : AdapterHutang

    var listHutang = ArrayList<Hutang>()
    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat

    var TIPE_HUTANG = "pelanggan"
    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_HUTANG = "hutang"
    var TAG_GET_REPORT = "reporthutang"
    var TAG_GET_MORE_HUTANG = "morehutang"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHutangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        shimmerFrameLayout = root.findViewById(R.id.sflMain)
        mUserPref = UserPreference(requireContext())
        adapter = AdapterHutang(listHutang)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHutang.setHasFixedSize(true)
        binding.rvHutang.layoutManager = layoutManager
        binding.rvHutang.adapter = adapter

        binding.btnTambahHutang.setOnClickListener {
            val i = Intent(requireContext(),TambahHutangActivity::class.java)
            startActivity(i)
        }
        binding.btnHutangSaya.setOnClickListener {
            TIPE_HUTANG = "saya"
            getDataHutang()
        }
        binding.cardLaporan.setOnClickListener {
            showDialogFilter()
        }


        getDataHutang()
        return root
    }

    fun showDialogFilter(){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_time_hutang, null)

        var selected_tgl_awal = ""
        var selected_tgl_akhir = ""
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val btnPilihAwal = view.findViewById<Button>(R.id.btnPilihAwal)
        val btnPilihAkhir = view.findViewById<Button>(R.id.btnPilihAkhir)
        val btnFilter = view.findViewById<Button>(R.id.btnFilter)
        val etTanggalAwal = view.findViewById<EditText>(R.id.etTanggalAwal)
        val etTanggalAkhir = view.findViewById<EditText>(R.id.etTanggalAkhir)

        btnPilihAwal.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                etTanggalAwal.setText("" + dayOfMonth + "/" + (monthOfYear + 1 ) + "/" + year)
                //selected_tgl_awal = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year
                selected_tgl_awal = ""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth

            }, year, month, day)

            dpd.show()
        }
        btnPilihAkhir.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                etTanggalAkhir.setText("" + dayOfMonth + "/" + (monthOfYear + 1 ) + "/" + year)
                //selected_tgl_akhir = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year
                selected_tgl_akhir = ""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth

            }, year, month, day)

            dpd.show()
        }
        btnFilter.setOnClickListener {
            filterDataHutang(selected_tgl_awal,selected_tgl_akhir)
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    fun filterDataHutang(tgl_mulai : String,tgl_akhir : String){
        Log.d(TAG_GET_REPORT," tgl_mulai "+tgl_mulai+ " dan tgl akir "+tgl_akhir)
        resetPagination()
        showLoadingShimmer()

        ApiMain().services.getReportHutang(mUserPref.getToken(),tgl_mulai,tgl_akhir).enqueue(object :
            retrofit2.Callback<HutangResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<HutangResponse>, response: Response<HutangResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_REPORT,response.toString())
                Log.d(TAG_GET_REPORT,"http status : "+response.code())

                if(response.code() == 200) {
                    listHutang.clear()
                    response.body()?.data_hutang?.let {
                        Log.d(TAG_GET_REPORT,"dari API : "+it)
                        Log.d(TAG_GET_REPORT,"jumlah dari API : "+it.size)
                        listHutang.addAll(it)
                        adapter.notifyDataSetChanged()

                        hideLoadingShimmer()
                        Log.d(TAG_GET_REPORT,"isi adapter  : "+adapter.itemCount)
                    }

                    if (listHutang.size == 0){
                        Toasty.info(requireContext(), "Belum ada data untuk rentang waktu ini..", Toast.LENGTH_SHORT, true).show()
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_REPORT,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<HutangResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_REPORT,"rusak nya gpapa kok  ")
                    hideLoadingShimmer()
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_REPORT,"rusak : "+t.message.toString())
                }
            }
        })
    }

    fun resetPagination(){
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataHutang(){
        showLoadingShimmer()
        resetPagination()

        ApiMain().services.getDataHutang(mUserPref.getToken(),CURRENT_PAGE,TIPE_HUTANG).enqueue(object :
            retrofit2.Callback<HutangResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<HutangResponse>, response: Response<HutangResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_HUTANG,response.toString())
                Log.d(TAG_GET_HUTANG,"http status : "+response.code())

                if(response.code() == 200) {
                    listHutang.clear()
                    response.body()?.data_hutang?.let {
                        Log.d(TAG_GET_HUTANG,"dari API : "+it)
                        Log.d(TAG_GET_HUTANG,"jumlah dari API : "+it.size)
                        listHutang.addAll(it)
                        adapter.notifyDataSetChanged()

                        hideLoadingShimmer()
                        Log.d(TAG_GET_HUTANG,"isi adapter  : "+adapter.itemCount)
                    }

                    if (listHutang.size == 0){
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_HUTANG,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<HutangResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_HUTANG,"rusak nya gpapa kok  ")
                    hideLoadingShimmer()
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_HUTANG,"rusak : "+t.message.toString())
                }
            }
        })
    }

    fun showLoadingShimmer(){
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvHutang.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmer(){
        if (shimmerFrameLayout.isVisible){
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.clearAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        binding.rvHutang.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        getDataHutang()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): HutangFragment{
            val fragment = HutangFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}