package com.tapisdev.penjualankasir.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
    var TAG_GET_MORE_BARANG = "morebarang"
    var KATA_KUNCI = ""
    var isSearching = false
    var isFilterStokMenipis = false


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
        binding.btnStokTipis.setOnClickListener {
            isFilterStokMenipis = true
            getDataBarangStokMenipis()
        }
        binding.rvBarang.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d("rvBarang"," ini terakir")
                    //next page
                    if (isFilterStokMenipis){
                        //get more stok tipis
                        getMoreDataBarangStokMenipis()
                    }else{
                        getMoreDataBarang()
                    }

                }
            }
        })
        binding.etSearch.setOnKeyListener { view, i, keyEvent ->

            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER ){
                if (binding.etSearch.text.toString().length != 0){
                    KATA_KUNCI = binding.etSearch.text.toString()
                    getDataBarang()
                }else{
                    //action ketika search dikosongkan
                    KATA_KUNCI = ""
                    getDataBarang()
                }
                //showInfoMessage("enter diklik")
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }


        getDataBarang()
        return root
    }

    fun resetPagination(){
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataBarang(){
        showLoadingShimmer()
        isFilterStokMenipis = false
        resetPagination()

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

    fun getMoreDataBarang(){
        binding.progressBar.visibility = View.VISIBLE

        ApiMain().services.getBarang(mUserPref.getToken(),NEXT_PAGE,KATA_KUNCI).enqueue(object :
            retrofit2.Callback<BarangResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<BarangResponse>, response: Response<BarangResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_MORE_BARANG,response.toString())
                Log.d(TAG_GET_MORE_BARANG,"http status : "+response.code())
                binding.progressBar.visibility = View.GONE

                if(response.code() == 200) {
                    //listLowongan.clear()
                    response.body()?.data_barang?.let {
                        Log.d(TAG_GET_MORE_BARANG,"dari API : "+it)
                        Log.d(TAG_GET_MORE_BARANG,"jumlah dari API : "+it.size)

                        if (response.body()?.data_barang?.size != 0){
                            listBarang.addAll(it)
                            adapter.notifyDataSetChanged()
                        }

                        Log.d(TAG_GET_MORE_BARANG,"isi adapter  : "+adapter.itemCount)
                        Log.d(TAG_GET_MORE_BARANG,"isi more data  : "+response.body()?.data_barang?.size)
                    }

                    if (response.body()?.data_barang?.size == 0){
                        Log.d(TAG_GET_MORE_BARANG,"more data kosong ")
                    }else{
                        CURRENT_PAGE++
                        NEXT_PAGE++
                    }
                    Log.d(TAG_GET_MORE_BARANG,"current page  : "+CURRENT_PAGE)
                    Log.d(TAG_GET_MORE_BARANG,"next page  : "+NEXT_PAGE)

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data baru", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_MORE_BARANG,"err :"+response.message())
                    binding.progressBar.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<BarangResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_MORE_BARANG,"rusak nya gpapa kok  ")
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_MORE_BARANG,"rusak : "+t.message.toString())
                }

                binding.progressBar.visibility = View.GONE
            }
        })
    }

    fun getDataBarangStokMenipis(){
        showLoadingShimmer()
        resetPagination()

        ApiMain().services.getBarangStokTipis(mUserPref.getToken(),CURRENT_PAGE,KATA_KUNCI).enqueue(object :
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

    fun getMoreDataBarangStokMenipis(){
        binding.progressBar.visibility = View.VISIBLE

        ApiMain().services.getBarangStokTipis(mUserPref.getToken(),NEXT_PAGE,KATA_KUNCI).enqueue(object :
            retrofit2.Callback<BarangResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<BarangResponse>, response: Response<BarangResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_MORE_BARANG,response.toString())
                Log.d(TAG_GET_MORE_BARANG,"http status : "+response.code())
                binding.progressBar.visibility = View.GONE

                if(response.code() == 200) {
                    //listLowongan.clear()
                    response.body()?.data_barang?.let {
                        Log.d(TAG_GET_MORE_BARANG,"dari API : "+it)
                        Log.d(TAG_GET_MORE_BARANG,"jumlah dari API : "+it.size)

                        if (response.body()?.data_barang?.size != 0){
                            listBarang.addAll(it)
                            adapter.notifyDataSetChanged()
                        }

                        Log.d(TAG_GET_MORE_BARANG,"isi adapter  : "+adapter.itemCount)
                        Log.d(TAG_GET_MORE_BARANG,"isi more data  : "+response.body()?.data_barang?.size)
                    }

                    if (response.body()?.data_barang?.size == 0){
                        Log.d(TAG_GET_MORE_BARANG,"more data kosong ")
                    }else{
                        CURRENT_PAGE++
                        NEXT_PAGE++
                    }
                    Log.d(TAG_GET_MORE_BARANG,"current page  : "+CURRENT_PAGE)
                    Log.d(TAG_GET_MORE_BARANG,"next page  : "+NEXT_PAGE)

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data baru", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_MORE_BARANG,"err :"+response.message())
                    binding.progressBar.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<BarangResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_MORE_BARANG,"rusak nya gpapa kok  ")
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_MORE_BARANG,"rusak : "+t.message.toString())
                }

                binding.progressBar.visibility = View.GONE
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