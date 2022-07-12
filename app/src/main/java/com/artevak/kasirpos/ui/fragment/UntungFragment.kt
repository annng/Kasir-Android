package com.artevak.kasirpos.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
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
import com.artevak.kasirpos.BuildConfig
import com.artevak.kasirpos.R
import com.artevak.kasirpos.ui.adapter.AdapterTransaksi
import com.artevak.kasirpos.databinding.FragmentUntungBinding
import com.artevak.kasirpos.model.Transaksi
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.TotalUntungResponse
import com.artevak.kasirpos.response.UntungResponse
import com.artevak.kasirpos.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class UntungFragment : Fragment() {

    private var _binding: FragmentUntungBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //lateinit var binding_shimmer : ShimmerSuratBinding
    lateinit var shimmerFrameLayout : ShimmerFrameLayout
    lateinit var mUserPref : UserPreference
    lateinit var adapter : AdapterTransaksi

    var listTransaksi = ArrayList<Transaksi>()
    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat


    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_TRANSAKSI = "transaksi"
    var TAG_GET_REPORT = "reporttransaksi"
    var TAG_GET_TOTALUNTUNG = "totaluntung"
    var TAG_GET_MORE_TRANSAKSI = "moretransaksi"
    var dari = ""
    var sampai = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUntungBinding.inflate(inflater, container, false)
        val root: View = binding.root

        shimmerFrameLayout = root.findViewById(R.id.sflMain)
        mUserPref = UserPreference(requireContext())
        adapter = AdapterTransaksi(listTransaksi)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUntung.setHasFixedSize(true)
        binding.rvUntung.layoutManager = layoutManager
        binding.rvUntung.adapter = adapter


        binding.cardLaporan.setOnClickListener {
            showDialogFilter()
        }
        binding.fabDownload.setOnClickListener {
            if (listTransaksi.size > 0){
               //TODO download transaction
            }else{
                Toasty.error(requireContext(), "data transaksi masih kosong", Toast.LENGTH_SHORT, true).show()
            }
        }


        getDataUntung()
        getTotalUntung()
        return root
    }

    fun showDialogFilter(){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_time_untung, null)

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
            filterDataUntung(selected_tgl_awal,selected_tgl_akhir)
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    fun filterDataUntung(tgl_mulai : String,tgl_akhir : String){
        Log.d(TAG_GET_REPORT," tgl_mulai "+tgl_mulai+ " dan tgl akir "+tgl_akhir)
        resetPagination()
        showLoadingShimmer()
        //TODO filter untung start & end date
    }

    fun resetPagination(){
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataUntung(){
        showLoadingShimmer()
        resetPagination()

        //TODO get data Untung

        hideLoadingShimmer()
    }

    fun getTotalUntung(){
        binding.progressBar.visibility = View.VISIBLE

        //TODO get total untung
    }


    fun showLoadingShimmer(){
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvUntung.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmer(){
        if (shimmerFrameLayout.isVisible){
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.clearAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        binding.rvUntung.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): UntungFragment{
            val fragment = UntungFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}