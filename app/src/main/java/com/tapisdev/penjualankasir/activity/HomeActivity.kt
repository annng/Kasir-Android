package com.tapisdev.penjualankasir.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tapisdev.lokamotor.base.BaseActivity
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.adapter.AdapterPelanggan
import com.tapisdev.penjualankasir.databinding.ActivityHomeBinding
import com.tapisdev.penjualankasir.fragment.*
import com.tapisdev.penjualankasir.model.Pelanggan
import com.tapisdev.penjualankasir.model.SharedVariable
import com.tapisdev.penjualankasir.model.UserPreference
import com.tapisdev.penjualankasir.response.AllPelangganResponse
import com.tapisdev.penjualankasir.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    lateinit var nextFragment  : Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserPref = UserPreference(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (SharedVariable.nextFragment.equals("")){
            nextFragment = HomeFragment.newInstance()
        }else if (SharedVariable.nextFragment.equals("stok")){
            nextFragment = StokFragment.newInstance()
        }else if (SharedVariable.nextFragment.equals("hutang")){
            nextFragment = HutangFragment.newInstance()
        }else if (SharedVariable.nextFragment.equals("untung")){
            nextFragment = UntungFragment.newInstance()
        }else if (SharedVariable.nextFragment.equals("transaksi")){
            nextFragment = TransaksiFragment.newInstance()
        }

        addFragment(nextFragment)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = HomeFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_stok -> {
                val fragment = StokFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_hutang -> {
                val fragment = HutangFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_untung -> {
                val fragment = UntungFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_transaksi -> {
                val fragment = TransaksiFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }


        }
        false
    }


    override fun onResume() {
        super.onResume()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }
}