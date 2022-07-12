package com.artevak.kasirpos.ui.fragment

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.artevak.kasirpos.base.BaseFragment
import com.artevak.kasirpos.databinding.FragmentTransaksiBinding
import com.artevak.kasirpos.model.*
import com.artevak.kasirpos.ui.activity.HomeActivity
import com.artevak.kasirpos.ui.activity.customer.SelectPelangganActivity
import com.artevak.kasirpos.ui.adapter.AdapterBarang
import com.artevak.kasirpos.ui.adapter.AdapterKeranjang
import com.artevak.kasirpos.ui.adapter.AdapterPelanggan
import es.dmoral.toasty.Toasty
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class TransaksiFragment : BaseFragment() {

    private var _binding: FragmentTransaksiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var listBarang = ArrayList<Barang>()
    var listPelanggan = ArrayList<Pelanggan>()
    var listNamaBarang = ArrayList<String>()
    var listKeranjang = ArrayList<Keranjang>()

    var selectedBarang : Barang? = null
    lateinit var adapter : AdapterBarang
    lateinit var adapterKeranjang : AdapterKeranjang
    lateinit var adapterPelanggan : AdapterPelanggan
    lateinit var mUserPref : UserPreference
    lateinit var pDialogLoading : SweetAlertDialog
    lateinit var orderInfo: OrderInfo

    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat

    var totalBayar = 0
    var TAG_GET_BARANG = "barang"
    var TAG_ORDER = "order"
    var id_pelanggan = "0"
    var TAG_TEXT_AUTOCOMPLETE = "autocomplete"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mUserPref = UserPreference(requireContext())
        adapter = AdapterBarang(listBarang)
        adapterKeranjang = AdapterKeranjang(listKeranjang,this)
        adapterPelanggan = AdapterPelanggan(listPelanggan)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTransaksi.setHasFixedSize(true)
        binding.rvTransaksi.layoutManager = layoutManager
        binding.rvTransaksi.adapter = adapterKeranjang

        val adapterAutcomplete = ArrayAdapter(requireContext(),
            R.layout.simple_list_item_1, listNamaBarang)
        binding.edSearchBarang.setAdapter(adapterAutcomplete)
        binding.edSearchBarang.setOnItemClickListener { adapterView, view, i, l ->

            val selected = adapterView.getItemAtPosition(i) as String
            val pos: Int = listNamaBarang.indexOf(selected)
            Log.d(TAG_TEXT_AUTOCOMPLETE,"item selected : "+selected)
            Log.d(TAG_TEXT_AUTOCOMPLETE,"item position : "+pos)

            selectedBarang = listBarang.get(pos)
            showSelectedBarangInfo(listBarang.get(pos))
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


        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (listBarang.size == 0){
            getDataBarang()
        }
        setPelangganInfo()
        //totalBayar = 0
        //listKeranjang.clear()
        //adapterKeranjang.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun saveToCart(keranjang: Keranjang){
        listKeranjang.add(keranjang)
        adapterKeranjang.notifyDataSetChanged()

        //count total
        totalBayar = 0
        for (i in 0..listKeranjang.size - 1){
            totalBayar+=listKeranjang.get(i).subtotal!!
        }
        binding.tvTotal.setText("Rp. "+df.format(totalBayar))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeFromCart(keranjang: Keranjang){
        listKeranjang.remove(keranjang)
        adapterKeranjang.notifyDataSetChanged()

        //count total
        totalBayar = 0
        for (i in 0..listKeranjang.size - 1){
            totalBayar+=listKeranjang.get(i).subtotal!!
        }
        binding.tvTotal.setText("Rp. "+df.format(totalBayar))
    }

    fun setPelangganInfo(){
        if (SharedVariable.pelangganType.equals("guest")){
            binding.tvNamaPelanggan.setText("Pelanggan Guest")
        }else{
            id_pelanggan = SharedVariable.selectedPelanggan?.id!!
            binding.tvNamaPelanggan.setText(SharedVariable.selectedPelanggan?.name)
        }
    }

    fun checkValidationTambah(){
        val jmlBeli = binding.etJumlahBeli.text.toString()
        val jumlah = jmlBeli.toInt()
        if (jmlBeli.equals("") || jmlBeli.length == 0){
            Toasty.error(requireContext(), "Jumlah pembelian belum diiisi", Toast.LENGTH_SHORT, true).show()
        }else if (selectedBarang == null){
            Toasty.error(requireContext(), "Anda belum memilih barang", Toast.LENGTH_SHORT, true).show()
        }else if (jumlah > selectedBarang!!.stok!!){
            Toasty.error(requireContext(), "Stok barang tidak cukup..", Toast.LENGTH_SHORT, true).show()
        }
        else{
            val subtotal = jumlah * selectedBarang?.harga_jual!!
            val subtotal_harga_beli = jumlah * selectedBarang?.harga_beli!!
            val untung = subtotal - subtotal_harga_beli

            val keranjang = Keranjang(
                selectedBarang?.name,
                jumlah,
                selectedBarang?.harga_jual,
                selectedBarang?.deskripsi,
                selectedBarang?.picture,
                selectedBarang?.satuan,
                subtotal,
                selectedBarang?.id,
                untung
            )

            saveToCart(keranjang)
            selectedBarang = null
            resetSelectedBarangInfo()
        }
    }

    fun checkValidationOrder(){
        if (listKeranjang.size == 0){
            Toasty.error(requireContext(), "Anda belum memilih barang", Toast.LENGTH_SHORT, true).show()
        }else{
            orderInfo = OrderInfo(
                id_pelanggan,
                SharedVariable.pelangganType,
                totalBayar,
                listKeranjang
            )
            saveOrder()
        }
    }

    fun saveOrder(){
        showLoading()
        //TODO save order

    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetTransaksi(){
        listKeranjang.clear()
        adapterKeranjang.notifyDataSetChanged()

        selectedBarang = null
        SharedVariable.selectedPelanggan = null
        SharedVariable.pelangganType = "guest"

        totalBayar = 0
        binding.tvTotal.setText("Rp. 0")

        resetSelectedBarangInfo()
        setPelangganInfo()
    }

    fun showSelectedBarangInfo(barang : Barang){
        binding.tvNamaBarang.setText(barang.name)
        binding.tvHargaBarang.setText("Rp. "+df.format(barang.harga_jual))
    }

    fun resetSelectedBarangInfo(){
        binding.tvNamaBarang.setText("- - -")
        binding.tvHargaBarang.setText("Rp. -")
        binding.etJumlahBeli.setText("")
        binding.edSearchBarang.setText("")
        binding.rvTransaksi.requestFocus()

        (activity as HomeActivity?)?.hideKeyboard(_binding!!.root)
    }

    fun getDataBarang(){
        showLoading()

        listBarang.add(
            Barang(
                name = "Macbook Pro 2020",
                harga_beli = 210000,
                harga_jual = 2500000,
                deskripsi = "Mahal boss"
            )
        )
        adapter.notifyDataSetChanged()

        dismissLoading()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading(){
        pDialogLoading = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialogLoading.setTitleText("Loading..")
        pDialogLoading.setCancelable(false)

        pDialogLoading.show()
    }

    fun dismissLoading(){
        pDialogLoading.dismiss()
    }

    companion object {
        fun newInstance(): TransaksiFragment{
            val fragment = TransaksiFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}