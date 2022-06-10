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

    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_HUTANG = "hutang"
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


        getDataHutang()
        return root
    }

    fun resetPagination(){
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataHutang(){
        showLoadingShimmer()
        resetPagination()

        ApiMain().services.getDataHutang(mUserPref.getToken(),CURRENT_PAGE).enqueue(object :
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