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
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.activity.TambahBarangActivity
import com.tapisdev.penjualankasir.adapter.AdapterBarang
import com.tapisdev.penjualankasir.databinding.FragmentStokBinding
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.UserPreference
import com.tapisdev.penjualankasir.response.BarangResponse
import com.tapisdev.penjualankasir.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response

class StokFragment : Fragment() {

    private var _binding: FragmentStokBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var shimmerFrameLayout : ShimmerFrameLayout
    lateinit var adapter : AdapterBarang
    lateinit var i : Intent
    lateinit var mUserPref : UserPreference
    var listBarang = ArrayList<Barang>()

    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_BARANG = "barang"
    var KATA_KUNCI = ""
    var isSearching = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStokBinding.inflate(inflater, container, false)
        val root: View = binding.root

        shimmerFrameLayout = root.findViewById(R.id.sflMain)
        mUserPref = UserPreference(requireContext())
        adapter = AdapterBarang(listBarang)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBarang.setHasFixedSize(true)
        binding.rvBarang.layoutManager = layoutManager
        binding.rvBarang.adapter = adapter

        binding.btnTambahBarang.setOnClickListener {
            val i = Intent(requireActivity(),TambahBarangActivity::class.java)
            startActivity(i)
        }
        binding.rvBarang.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d("rvBarang"," ini terakir")
                    //next page
                    /*if (isSearching){
                        getMoreSearchLowongan(NEXT_PAGE,KATA_KUNCI)
                    }else if (isFilterCategory){
                        getMoreKategoriLowongan(NEXT_PAGE,SELECTED_KATEGORI)
                    }else {
                        getMoreDataLowongan(NEXT_PAGE)
                    }*/

                }
            }
        })


        getDataBarang()
        return root
    }

    fun getDataBarang(){
        showLoadingShimmer()

        ApiMain().services.getBarang(mUserPref.getToken(),CURRENT_PAGE,KATA_KUNCI).enqueue(object :
            retrofit2.Callback<BarangResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<BarangResponse>, response: Response<BarangResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_BARANG,response.toString())
                Log.d(TAG_GET_BARANG,"http status : "+response.code())

                if(response.code() == 200) {
                    listBarang.clear()
                    response.body()?.data_barang?.let {
                        Log.d(TAG_GET_BARANG,"dari API : "+it)
                        Log.d(TAG_GET_BARANG,"jumlah dari API : "+it.size)
                        listBarang.addAll(it)
                        adapter.notifyDataSetChanged()

                        hideLoadingShimmer()
                        Log.d(TAG_GET_BARANG,"isi adapter  : "+adapter.itemCount)
                    }

                    if (listBarang.size == 0){
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_BARANG,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<BarangResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_BARANG,"rusak nya gpapa kok  ")
                    hideLoadingShimmer()
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_BARANG,"rusak : "+t.message.toString())
                }
            }
        })
    }

    fun showLoadingShimmer(){
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvBarang.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmer(){
        if (shimmerFrameLayout.isVisible){
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.clearAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        binding.rvBarang.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getDataBarang()
    }

    companion object {
        fun newInstance(): StokFragment{
            val fragment = StokFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}