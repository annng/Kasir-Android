package com.tapisdev.penjualankasir.fragment

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tapisdev.penjualankasir.MainActivity
import com.tapisdev.penjualankasir.activity.HomeActivity
import com.tapisdev.penjualankasir.activity.SelectPelangganActivity
import com.tapisdev.penjualankasir.activity.TambahBarangActivity
import com.tapisdev.penjualankasir.adapter.AdapterBarang
import com.tapisdev.penjualankasir.adapter.AdapterKeranjang
import com.tapisdev.penjualankasir.adapter.AdapterPelanggan
import com.tapisdev.penjualankasir.databinding.*
import com.tapisdev.penjualankasir.model.*
import com.tapisdev.penjualankasir.response.AllBarangResponse
import com.tapisdev.penjualankasir.response.AllPelangganResponse
import com.tapisdev.penjualankasir.response.TransaksiResponse
import com.tapisdev.penjualankasir.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TransaksiFragment : Fragment() {

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
            val i = Intent(requireContext(),SelectPelangganActivity::class.java)
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
        if (jmlBeli.equals("") || jmlBeli.length == 0){
            Toasty.error(requireContext(), "Jumlah pembelian belum diiisi", Toast.LENGTH_SHORT, true).show()
        }else if (selectedBarang == null){
            Toasty.error(requireContext(), "Anda belum memilih barang", Toast.LENGTH_SHORT, true).show()
        }
        else{
            val jumlah = jmlBeli.toInt()
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
        Log.d(TAG_ORDER,orderInfo.toString())

        ApiMain().services.saveTransaksi(mUserPref.getToken(),orderInfo).enqueue(
            object : Callback<TransaksiResponse> {
                override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                    Toasty.error(requireContext(), "gagal simpan transaksi, coba lagi nanti", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_ORDER,t.message.toString())
                    dismissLoading()
                }
                override fun onResponse(call: Call<TransaksiResponse>, response: Response<TransaksiResponse>) {
                    val responAPI = response.body()
                    val responseStatus = response.code()


                    dismissLoading()
                    if(response.code() == 200) {
                        Log.d(TAG_ORDER,"body "+responAPI!!.toString())
                        Log.d(TAG_ORDER,"http code asli "+responseStatus.toString())
                        Log.d(TAG_ORDER,"http code dari API "+responAPI!!.http_status)

                        Toasty.success(requireContext(), "Transaksi berhasil !", Toast.LENGTH_SHORT, true).show()
                        resetTransaksi()

                    }else if (response.code() == 202){
                        Toasty.error(requireContext(), "gagal simpan transaksi, coba lagi nanti", Toast.LENGTH_SHORT, true).show()
                    }
                }
            }
        )

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

        ApiMain().services.getAllBarang(mUserPref.getToken()).enqueue(object :
            retrofit2.Callback<AllBarangResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<AllBarangResponse>, response: Response<AllBarangResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_BARANG,response.toString())
                Log.d(TAG_GET_BARANG,"http status : "+response.code())

                if(response.code() == 200) {
                    listBarang.clear()
                    listNamaBarang.clear()
                    response.body()?.data_barang?.let {
                        Log.d(TAG_GET_BARANG,"dari API : "+it)
                        Log.d(TAG_GET_BARANG,"jumlah dari API : "+it.size)
                        listBarang.addAll(it)
                        adapter.notifyDataSetChanged()

                        Log.d(TAG_GET_BARANG,"isi adapter  : "+adapter.itemCount)
                    }

                    for (c in 0 until listBarang.size){
                        val barang  = listBarang.get(c)
                        listNamaBarang.add(barang.name.toString())
                    }

                    dismissLoading()

                }else {
                    Toasty.error(requireContext(), "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_BARANG,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<AllBarangResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_BARANG,"rusak nya gpapa kok  ")
                    dismissLoading()
                }else{
                    Toasty.error(requireContext(), "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_BARANG,"rusak : "+t.message.toString())
                }
            }
        })

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