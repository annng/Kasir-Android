package com.tapisdev.penjualankasir.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.activity.TambahBarangActivity
import com.tapisdev.penjualankasir.adapter.AdapterBarang
import com.tapisdev.penjualankasir.adapter.AdapterTransaksi
import com.tapisdev.penjualankasir.databinding.FragmentHomeBinding
import com.tapisdev.penjualankasir.databinding.FragmentHutangBinding
import com.tapisdev.penjualankasir.databinding.FragmentStokBinding
import com.tapisdev.penjualankasir.databinding.FragmentUntungBinding
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.Transaksi
import com.tapisdev.penjualankasir.model.UserPreference
import com.tapisdev.penjualankasir.response.TotalUntungResponse
import com.tapisdev.penjualankasir.response.UntungResponse
import com.tapisdev.penjualankasir.util.ApiMain
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
    var TAG_GET_TOTALUNTUNG = "totaluntung"
    var TAG_GET_MORE_TRANSAKSI = "moretransaksi"


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


        getDataUntung()
        getTotalUntung()
        return root
    }

    fun resetPagination(){
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataUntung(){
        showLoadingShimmer()
        resetPagination()

        ApiMain().services.historyTransaksi(mUserPref.getToken(),CURRENT_PAGE).enqueue(object :
            retrofit2.Callback<UntungResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<UntungResponse>, response: Response<UntungResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_TRANSAKSI,response.toString())
                Log.d(TAG_GET_TRANSAKSI,"http status : "+response.code())

                if(response.code() == 200) {
                    listTransaksi.clear()
                    response.body()?.data_untung?.let {
                        Log.d(TAG_GET_TRANSAKSI,"dari API : "+it)
                        Log.d(TAG_GET_TRANSAKSI,"jumlah dari API : "+it.size)
                        listTransaksi.addAll(it)
                        adapter.notifyDataSetChanged()

                        hideLoadingShimmer()
                        Log.d(TAG_GET_TRANSAKSI,"isi adapter  : "+adapter.itemCount)
                    }

                    if (listTransaksi.size == 0){
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_TRANSAKSI,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<UntungResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_TRANSAKSI,"rusak nya gpapa kok  ")
                    hideLoadingShimmer()
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_TRANSAKSI,"rusak : "+t.message.toString())
                }
            }
        })
    }

    fun getTotalUntung(){
        binding.progressBar.visibility = View.VISIBLE

        ApiMain().services.getTotalUntung(mUserPref.getToken()).enqueue(object :
            retrofit2.Callback<TotalUntungResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TotalUntungResponse>, response: Response<TotalUntungResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_TRANSAKSI,response.toString())
                Log.d(TAG_GET_TRANSAKSI,"http status : "+response.code())


                if(response.code() == 200) {
                    val responAPI = response.body()
                    Log.d(TAG_GET_TOTALUNTUNG,"respon api : "+responAPI.toString())
                    val totalUntung = responAPI!!.data
                    val total =  totalUntung.toInt()
                    Log.d(TAG_GET_TOTALUNTUNG,"hasil : "+totalUntung)
                    binding.progressBar.visibility = View.GONE
                    binding.tvKeuntunganTotal.setText("Rp. "+df.format(total))
                    binding.tvKeuntunganTotal.visibility = View.VISIBLE


                }else {
                    binding.progressBar.visibility = View.GONE
                    Toasty.error(requireContext(), "gagal mengambil data total untung", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_TRANSAKSI,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<TotalUntungResponse>, t: Throwable){
                //Tulis code jika response fail
                binding.progressBar.visibility = View.GONE
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_TRANSAKSI,"rusak nya gpapa kok  ")
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_TRANSAKSI,"rusak : "+t.message.toString())
                }
            }
        })
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