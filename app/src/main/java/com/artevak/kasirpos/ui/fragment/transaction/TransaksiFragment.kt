package com.artevak.kasirpos.ui.fragment.transaction

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseFragment
import com.artevak.kasirpos.common.util.ext.toPriceFormat
import com.artevak.kasirpos.databinding.FragmentTransaksiBinding
import com.artevak.kasirpos.data.model.*
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.artevak.kasirpos.ui.activity.HomeActivity
import com.artevak.kasirpos.ui.activity.customer.select.SelectPelangganActivity
import com.artevak.kasirpos.ui.adapter.AdapterBarang
import com.artevak.kasirpos.ui.adapter.AdapterKeranjang
import com.artevak.kasirpos.ui.adapter.AdapterPelanggan
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class TransaksiFragment : BaseFragment() {

    private var _binding: FragmentTransaksiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var listBarang = ArrayList<ResponseData<Barang>>()
    var listCustomer = ArrayList<ResponseData<Customer>>()
    var listKeranjang = ArrayList<Keranjang>()

    var selectedBarang: ResponseData<Barang>? = null
    lateinit var adapter: AdapterBarang
    lateinit var adapterKeranjang: AdapterKeranjang
    lateinit var adapterPelanggan: AdapterPelanggan
    lateinit var pDialogLoading: SweetAlertDialog
    lateinit var orderInfo: OrderInfo

    val nf = NumberFormat.getNumberInstance(Locale.getDefault())
    val df = nf as DecimalFormat

    var totalBayar: Long = 0
    var TAG_GET_BARANG = "barang"
    var TAG_ORDER = "order"
    var id_pelanggan = "0"
    var TAG_TEXT_AUTOCOMPLETE = "autocomplete"

    val viewModel: TransactionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter = AdapterBarang(listBarang)
        adapterKeranjang = AdapterKeranjang(listKeranjang, this)
        adapterPelanggan = AdapterPelanggan(listCustomer)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTransaksi.setHasFixedSize(true)
        binding.rvTransaksi.layoutManager = layoutManager
        binding.rvTransaksi.adapter = adapterKeranjang

        val adapterAutcomplete = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, listBarang
        )
        binding.edSearchBarang.setAdapter(adapterAutcomplete)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        observeData()

        viewModel.getItems()
    }

    private fun initListener() {
        binding.edSearchBarang.setOnItemClickListener { adapterView, view, i, l ->

            val selected = adapterView.getItemAtPosition(i) as ResponseData<Barang>
            val pos: Int = listBarang.indexOf(selected)

            selectedBarang = listBarang[pos]
            showSelectedBarangInfo(listBarang[pos].data)
        }
        binding.ivRefresh.setOnClickListener {
            //get fresh data of barang
            getDataBarang()
        }
        binding.ivSelesaiTransaksi.setOnClickListener {
            checkValidationOrder()
        }
        binding.cardPelanggan.setOnClickListener {
            val i = Intent(requireContext(), SelectPelangganActivity::class.java)
            startActivity(i)
        }
        binding.btnTambahBarang.setOnClickListener {
            checkValidationTambah()
        }

    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            when (it.status) {
                StatusRequest.LOADING -> {

                }
                StatusRequest.SUCCESS -> {

                    listBarang.clear()
                    it.data?.let { it1 -> listBarang.addAll(it1) }
                    adapter.notifyDataSetChanged()

                }
                else -> {
                    viewModel.getItems()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (listBarang.size == 0) {
            getDataBarang()
        }
        setPelangganInfo()
        //totalBayar = 0
        //listKeranjang.clear()
        //adapterKeranjang.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun saveToCart(keranjang: Keranjang) {
        listKeranjang.add(keranjang)
        adapterKeranjang.notifyDataSetChanged()

        //count total
        totalBayar = 0
        for (i in 0 until listKeranjang.size) {
            totalBayar += listKeranjang.get(i).subtotal!!
        }
        binding.tvTotal.text = "Rp. " + df.format(totalBayar)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeFromCart(keranjang: Keranjang) {
        listKeranjang.remove(keranjang)
        adapterKeranjang.notifyDataSetChanged()

        //count total
        totalBayar = 0
        for (i in 0 until listKeranjang.size) {
            totalBayar += listKeranjang[i].subtotal!!
        }
        binding.tvTotal.text = "Rp. " + df.format(totalBayar)
    }

    fun setPelangganInfo() {
        if (SharedVariable.pelangganType.equals("guest")) {
            binding.tvNamaPelanggan.text = "Pelanggan Guest"
        } else {
//            id_pelanggan = SharedVariable.selectedCustomer?.id!!
            //todo set id pelanggan to firebase key record
            binding.tvNamaPelanggan.text = SharedVariable.selectedCustomer?.name
        }
    }

    fun checkValidationTambah() {
        val jmlBeli = binding.etJumlahBeli.text.toString()

        val isTotalEmpty = jmlBeli == "" || jmlBeli.isEmpty()
        val isSelectedItemEmpty = selectedBarang == null

        if (isTotalEmpty) {
            Toasty.error(
                requireContext(),
                getString(R.string.error_toast_qty_empty),
                Toast.LENGTH_SHORT,
                true
            ).show()
            return
        }
        if (isSelectedItemEmpty) {
            Toasty.error(
                requireContext(),
                getString(R.string.error_toast_item_not_selected),
                Toast.LENGTH_SHORT,
                true
            )
                .show()
            return
        }

        val jumlah = jmlBeli.toLong()
        val isQtyMoreThanStock = jumlah > (selectedBarang?.data?.stok ?: 0)
        if (isQtyMoreThanStock) {
            Toasty.error(
                requireContext(),
                getString(R.string.error_toast_insufficient_stock),
                Toast.LENGTH_SHORT,
                true
            )
                .show()
            return
        }

        val subtotal = jumlah * selectedBarang?.data?.harga_jual!!
        val subtotal_harga_beli = jumlah * selectedBarang?.data?.harga_beli!!
        val untung = subtotal - subtotal_harga_beli

        val keranjang = Keranjang(
            selectedBarang?.data?.name,
            jumlah,
            selectedBarang?.data?.harga_jual,
            selectedBarang?.data?.deskripsi,
            selectedBarang?.data?.picture,
            selectedBarang?.data?.satuan,
            subtotal,
            untung
        )

        saveToCart(keranjang)
        selectedBarang = null
        resetSelectedBarangInfo()
    }

    fun checkValidationOrder() {
        if (listKeranjang.size == 0) {
            Toasty.error(requireContext(), "Anda belum memilih barang", Toast.LENGTH_SHORT, true)
                .show()
        } else {
            orderInfo = OrderInfo(
                id_pelanggan,
                SharedVariable.pelangganType,
                totalBayar,
                listKeranjang
            )
            saveOrder()
        }
    }

    fun saveOrder() {
        showLoading()
        //TODO save order

    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetTransaksi() {
        listKeranjang.clear()
        adapterKeranjang.notifyDataSetChanged()

        selectedBarang = null
        SharedVariable.selectedCustomer = null
        SharedVariable.pelangganType = "guest"

        totalBayar = 0
        binding.tvTotal.text = "Rp. 0"

        resetSelectedBarangInfo()
        setPelangganInfo()
    }

    fun showSelectedBarangInfo(barang: Barang) {
        binding.tvNamaBarang.text = barang.name
        binding.tvHargaBarang.text = barang.harga_jual?.toPriceFormat()
    }

    fun resetSelectedBarangInfo() {
        binding.tvNamaBarang.text = "- - -"
        binding.tvHargaBarang.text = "Rp. -"
        binding.etJumlahBeli.setText("")
        binding.edSearchBarang.setText("")
        binding.rvTransaksi.requestFocus()

        (activity as HomeActivity?)?.hideKeyboard(_binding!!.root)
    }

    fun getDataBarang() {
        showLoading()

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
        adapter.notifyDataSetChanged()

        dismissLoading()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading() {
        pDialogLoading = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        pDialogLoading.titleText = getString(R.string.dialog_title_loading)
        pDialogLoading.setCancelable(false)

        pDialogLoading.show()
    }

    fun dismissLoading() {
        pDialogLoading.dismiss()
    }

    companion object {
        fun newInstance(): TransaksiFragment {
            val fragment = TransaksiFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}