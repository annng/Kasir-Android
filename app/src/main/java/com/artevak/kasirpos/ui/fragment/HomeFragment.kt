package com.artevak.kasirpos.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseFragment
import com.artevak.kasirpos.databinding.FragmentHomeBinding
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.DataChartPenjualan
import com.artevak.kasirpos.data.model.Pelanggan
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.ui.activity.customer.TambahPelangganActivity
import com.artevak.kasirpos.ui.activity.profile.view.ProfileActivity
import com.artevak.kasirpos.ui.adapter.AdapterBarang
import com.artevak.kasirpos.ui.adapter.AdapterPelanggan
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //lateinit var binding_shimmer : ShimmerSuratBinding
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var sflBarang: ShimmerFrameLayout
    lateinit var adapter: AdapterPelanggan
    lateinit var adapterBarang: AdapterBarang
    var listPelanggan = ArrayList<Pelanggan>()
    var listBarang = ArrayList<ResponseData<Barang>>()
    var listDataChart = ArrayList<DataChartPenjualan>()
    //lateinit var chart : LineChart

    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_PELANGGAN = "pelanggan"
    var TAG_GET_BARANG = "pelanggan"
    var TAG_GET_CHART = "datachart"
    var TAG_GET_MORE_PELANGGAN = "morepelanggan"
    var KATA_KUNCI = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerFrameLayout = binding.shimmerHorizontal.sflHorizontal
        sflBarang = binding.shimmerHorizontal.sflHorizontal
        adapter = AdapterPelanggan(listPelanggan)
        adapterBarang = AdapterBarang(listBarang)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPelanggan.setHasFixedSize(true)
        binding.rvPelanggan.layoutManager = layoutManager
        binding.rvPelanggan.adapter = adapter

        val horizontallayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBarang.setHasFixedSize(true)
        binding.rvBarang.layoutManager = horizontallayoutManager
        binding.rvBarang.adapter = adapterBarang



        binding.btnTambahPelanggan.setOnClickListener {
            val i = Intent(requireActivity(), TambahPelangganActivity::class.java)
            startActivity(i)
        }
        binding.ivProfil.setOnClickListener {
            val i = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(i)
        }

        getDataPelanggan()
        getDataBarang()
        getDataChart()
//        configChartModel()
    }

    fun configChartModel() {
        binding.chart.setBackgroundColor(Color.WHITE)
        binding.chart.setDrawGridBackground(false)
        binding.chart.setMaxVisibleValueCount(60)
        binding.chart.setPinchZoom(true)
        binding.chart.description.isEnabled = false

        val values = ArrayList<Entry>()
        for (i in 0 until listDataChart.size) {
            values.add(
                Entry(
                    i.toFloat(),
                    listDataChart.get(i).jumlah!!.toFloat(),
                    resources.getDrawable(R.drawable.star)
                )
            )
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "Grafik Penjualan Bulan ini")
        set1.setDrawIcons(false)
        // draw dashed line
        set1.enableDashedLine(10f, 5f, 0f)
        set1.color = Color.BLUE
        set1.setCircleColor(Color.RED)
        set1.lineWidth = 1f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 15f
        set1.valueTextSize = 9f
        set1.enableDashedHighlightLine(10f, 5f, 0f)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the data sets
        val data = LineData(dataSets)
        binding.chart.data = data

        binding.chart.animateX(1500)
    }

    fun resetPagination() {
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataPelanggan() {
        //loading
//        showLoadingShimmerPelanggan()
//        resetPagination()

        listPelanggan.clear()
        listPelanggan.add(
            Pelanggan(
                name = "Anang",
                phone = "+6285747325450",
                alamat = "Soka, Lerep"
            )
        )
        listPelanggan.add(
            Pelanggan(
                name = "Ardhea",
                phone = "+6285747123456",
                alamat = "Banyumanik"
            )
        )
        adapter.notifyDataSetChanged()

        hideLoadingShimmerPelanggan()



        if (listPelanggan.size == 0) {
            binding.tvInfoEmpty.visibility = View.VISIBLE
        }


        //when failed
//        Toasty.error(requireContext(), "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
        hideLoadingShimmerPelanggan()

    }

    fun getDataBarang() {
        showLoadingShimmerBarang()
        resetPagination()



        listBarang.clear()
        listBarang.add(
            ResponseData(
                Barang(
                    name = "Samsung S21",
                    harga_beli = 180000,
                    harga_jual = 200000,
                    deskripsi = "Ram 8/256"
                ), ""
            )
        )
        listBarang.add(
            ResponseData(
                Barang(
                    name = "Macbook Pro 2020",
                    harga_beli = 210000,
                    harga_jual = 2500000,
                    deskripsi = "Mahal boss"
                ), ""
            )
        )

        adapterBarang.notifyDataSetChanged()

        hideLoadingShimmerBarang()

        if (listBarang.size == 0) {
            binding.tvInfoEmpty.visibility = View.VISIBLE
        }

        hideLoadingShimmerBarang()
    }

    fun getDataChart() {

        listDataChart.clear()
        listDataChart.add(DataChartPenjualan("Barang", 20))
        listDataChart.add(DataChartPenjualan("Pelanggan", 12))

//        configChartModel()

    }


    fun showLoadingShimmerPelanggan() {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvPelanggan.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmerPelanggan() {
        if (shimmerFrameLayout.isVisible) {
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.clearAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        binding.rvPelanggan.visibility = View.VISIBLE
    }

    fun showLoadingShimmerBarang() {
        sflBarang.visibility = View.VISIBLE
        sflBarang.startShimmerAnimation()

        binding.rvBarang.visibility = View.GONE
        binding.tvInfoEmptyBarang.visibility = View.GONE
    }

    fun hideLoadingShimmerBarang() {
        if (sflBarang.isVisible) {
            sflBarang.stopShimmerAnimation()
            sflBarang.clearAnimation()
            sflBarang.visibility = View.GONE
        }

        binding.rvBarang.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}