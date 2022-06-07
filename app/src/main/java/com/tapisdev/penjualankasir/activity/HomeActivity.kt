package com.tapisdev.penjualankasir.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tapisdev.lokamotor.base.BaseActivity
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.databinding.ActivityHomeBinding
import com.tapisdev.penjualankasir.fragment.*
import com.tapisdev.penjualankasir.model.SharedVariable
import com.tapisdev.penjualankasir.model.UserPreference

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

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }
}