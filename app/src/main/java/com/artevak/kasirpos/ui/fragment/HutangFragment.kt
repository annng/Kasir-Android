package com.artevak.kasirpos.ui.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseFragment
import com.artevak.kasirpos.ui.activity.debt.TambahHutangActivity
import com.artevak.kasirpos.ui.adapter.AdapterHutang
import com.artevak.kasirpos.databinding.FragmentHutangBinding
import com.artevak.kasirpos.model.Hutang
import com.artevak.kasirpos.model.UserPreference
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HutangFragment : BaseFragment() {

    private var _binding: FragmentHutangBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //lateinit var binding_shimmer : ShimmerSuratBinding
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var adapter: AdapterHutang

    var listHutang = ArrayList<Hutang>()
    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat

    var TIPE_HUTANG = "pelanggan"
    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var TAG_GET_HUTANG = "hutang"
    var TAG_GET_REPORT = "reporthutang"
    var TAG_GET_MORE_HUTANG = "morehutang"
    var dari = ""
    var sampai = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHutangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        shimmerFrameLayout = root.findViewById(R.id.sflMain)
        adapter = AdapterHutang(listHutang)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHutang.setHasFixedSize(true)
        binding.rvHutang.layoutManager = layoutManager
        binding.rvHutang.adapter = adapter

        binding.btnTambahHutang.setOnClickListener {
            val i = Intent(requireContext(), TambahHutangActivity::class.java)
            startActivity(i)
        }
        binding.btnHutangSaya.setOnClickListener {
            TIPE_HUTANG = "saya"
            getDataHutang()
        }
        binding.cardLaporan.setOnClickListener {
            showDialogFilter()
        }
        binding.fabDownload.setOnClickListener {
            //TODO download hutang
        }


        getDataHutang()

        return root
    }

    fun showDialogFilter() {
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
            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    etTanggalAwal.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                    //selected_tgl_awal = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year
                    selected_tgl_awal = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth

                },
                year,
                month,
                day
            )

            dpd.show()
        }
        btnPilihAkhir.setOnClickListener {
            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    etTanggalAkhir.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                    //selected_tgl_akhir = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year
                    selected_tgl_akhir = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth

                },
                year,
                month,
                day
            )

            dpd.show()
        }
        btnFilter.setOnClickListener {
            filterDataHutang(selected_tgl_awal, selected_tgl_akhir)
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    fun filterDataHutang(tgl_mulai: String, tgl_akhir: String) {
       //TODO filter hutang by start & end date
    }

    fun resetPagination() {
        CURRENT_PAGE = 1
        NEXT_PAGE = CURRENT_PAGE + 1
    }

    fun getDataHutang() {
        showLoadingShimmer()
        resetPagination()

        listHutang.clear()
        listHutang.add(Hutang().apply {
            id = "12"
            id_pelanggan = "23"
            nama_pelanggan = "Anang"
            hutang = 50000
            status = "Lunas"
            tgl_hutang = "2022-07-21"
            hutang_type = "Kredit"
            deskripsi = "Hutang buat biaya sekolah anak"
        })

        adapter.notifyDataSetChanged()

        hideLoadingShimmer()


        if (listHutang.size == 0) {
            binding.tvInfoEmpty.visibility = View.VISIBLE
        }
    }


    fun showLoadingShimmer() {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvHutang.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmer() {
        if (shimmerFrameLayout.isVisible) {
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
        fun newInstance(): HutangFragment {
            val fragment = HutangFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}