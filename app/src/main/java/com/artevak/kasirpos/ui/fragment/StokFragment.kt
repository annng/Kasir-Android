package com.artevak.kasirpos.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseFragment
import com.artevak.kasirpos.databinding.FragmentStokBinding
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.ui.activity.item.add.TambahBarangActivity
import com.artevak.kasirpos.ui.adapter.AdapterBarang
import com.facebook.shimmer.ShimmerFrameLayout

class StokFragment : BaseFragment() {

    private var _binding: FragmentStokBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var adapter: AdapterBarang
    lateinit var i: Intent
    var listBarang = ArrayList<Barang>()

    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_BARANG = "barang"
    var TAG_GET_MORE_BARANG = "morebarang"
    var KATA_KUNCI = ""
    var isSearching = false
    var isFilterStokMenipis = false


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStokBinding.inflate(inflater, container, false)
        val root: View = binding.root

        shimmerFrameLayout = root.findViewById(R.id.sflMain)
        adapter = AdapterBarang(listBarang)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBarang.setHasFixedSize(true)
        binding.rvBarang.layoutManager = layoutManager
        binding.rvBarang.adapter = adapter

        binding.btnTambahBarang.setOnClickListener {
            val i = Intent(requireActivity(), TambahBarangActivity::class.java)
            startActivity(i)
        }
        binding.btnStokTipis.setOnClickListener {

            binding.btnStokTipis.setTextColor(R.color.black)
            isFilterStokMenipis = true
            getDataBarangStokMenipis()
        }
        binding.rvBarang.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d("rvBarang", " ini terakir")
                    //next page
                    if (isFilterStokMenipis) {
                        getMoreDataBarangStokMenipis()
                    } else {
                        getMoreDataBarang()
                    }

                }
            }
        })
        binding.etSearch.setOnKeyListener { view, i, keyEvent ->

            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    KATA_KUNCI = binding.etSearch.text.toString()
                    getDataBarang()
                } else {
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

    fun resetPagination() {
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataBarang() {
        showLoadingShimmer()
        isFilterStokMenipis = false
        resetPagination()

        listBarang.clear()
        listBarang.add(
            Barang(
                name = "Macbook Pro 2020",
                harga_beli = 210000,
                harga_jual = 2500000,
                deskripsi = "Mahal boss"
            )
        )
        adapter.notifyDataSetChanged()

        hideLoadingShimmer()

        if (listBarang.size == 0) {
            binding.tvInfoEmpty.visibility = View.VISIBLE
        }
    }

    fun getMoreDataBarang() {
        binding.progressBar.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE

        listBarang.add(
            Barang(
                name = "Macbook Pro 2020",
                harga_beli = 210000,
                harga_jual = 2500000,
                deskripsi = "Mahal boss"
            )
        )
        adapter.notifyDataSetChanged()


    }

    fun getDataBarangStokMenipis() {
        showLoadingShimmer()
        resetPagination()

        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        listBarang.add(
            Barang(
                name = "Macbook Pro 2020",
                harga_beli = 210000,
                harga_jual = 2500000,
                deskripsi = "Mahal boss"
            )
        )
        adapter.notifyDataSetChanged()

        hideLoadingShimmer()
    }

    fun getMoreDataBarangStokMenipis() {
        binding.progressBar.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE

        listBarang.add(
            Barang(
                name = "Macbook Pro 2020",
                harga_beli = 210000,
                harga_jual = 2500000,
                deskripsi = "Mahal boss"
            )
        )
        adapter.notifyDataSetChanged()
    }


    fun showLoadingShimmer() {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvBarang.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmer() {
        if (shimmerFrameLayout.isVisible) {
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
        fun newInstance(): StokFragment {
            val fragment = StokFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}