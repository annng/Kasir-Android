package com.artevak.kasirpos.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.ui.activity.auth.LoginActivity
import com.artevak.kasirpos.ui.activity.HomeActivity
import com.artevak.kasirpos.databinding.ActivitySplashBinding
import com.artevak.kasirpos.model.UserModel
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.ui.activity.transaction.print.TransactionPrintActivity

class SplashActivity : BaseActivity() {
    lateinit var binding : ActivitySplashBinding
    lateinit var i : Intent
    var TAG_DETAIL = "detailuser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mUserPref = UserPreference(this)
        if (mUserPref.getToken().equals("") || mUserPref.getToken() == null){
            i = Intent(this, HomeActivity::class.java)
            val timer: Thread = object : Thread() {
                override fun run() {

                    try {
                        sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } finally {
                        startActivity(i)
                        finish()
                    }
                }
            }
            timer.start()
        }else{
            //TODO get detail user and goto HomeActivity
        }
    }

    fun goToLogin(){
        i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    fun setSession(userModel : UserModel){
        mUserPref.saveEmail(userModel.email!!)
        mUserPref.saveNamaUmkm(userModel.nama_umkm!!)
        mUserPref.saveAlamat(userModel.alamat!!)
        mUserPref.saveName(userModel.name!!)
        mUserPref.savePhone(userModel.phone!!)
    }
}