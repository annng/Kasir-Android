package com.artevak.kasirpos.ui.fragment.stock

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseFragment
import com.artevak.kasirpos.databinding.FragmentStokBinding
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.artevak.kasirpos.ui.activity.item.add.TambahBarangActivity
import com.artevak.kasirpos.ui.adapter.AdapterBarang
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class StokFragment : BaseFragment() {

    private var _binding: FragmentStokBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var adapter: AdapterBarang
    lateinit var i: Intent
    var listBarang = ArrayList<ResponseData<Barang>>()

    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_BARANG = "barang"
    var TAG_GET_MORE_BARANG = "morebarang"
    var KATA_KUNCI = ""
    var isSearching = false
    var isFilterStokMenipis = false

    val viewModel: StokViewModel by viewModel()


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStokBinding.inflate(inflater, container, false)

        shimmerFrameLayout = binding.root.findViewById(R.id.sflMain)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initAdapter()

        observeData()

        getDataBarang()
    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            when (it.status) {
                StatusRequest.LOADING -> {
                    showLoadingShimmer()
                }
                StatusRequest.SUCCESS -> {
                    hideLoadingShimmer()
                    listBarang.clear()
                    it.data?.let { it1 -> listBarang.addAll(it1) }
                    adapter.notifyDataSetChanged()

                    if (listBarang.size == 0) {
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }
                }
                else -> {
                    hideLoadingShimmer()
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initListener() {


        binding.btnTambahBarang.setOnClickListener {
            val i = Intent(requireActivity(), TambahBarangActivity::class.java)
            startActivity(i)
        }
        binding.btnStokTipis.setOnClickListener {

            binding.btnStokTipis.setTextColor(R.color.black)
            isFilterStokMenipis = true

        }

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
    }

    private fun initAdapter() {
        adapter = AdapterBarang(listBarang)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBarang.setHasFixedSize(true)
        binding.rvBarang.layoutManager = layoutManager
        binding.rvBarang.adapter = adapter

    }

    fun resetPagination() {
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataBarang() {
        isFilterStokMenipis = false
        resetPagination()

        viewModel.getItems()
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