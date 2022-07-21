package com.artevak.kasirpos.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivitySplashBinding
import com.artevak.kasirpos.ui.activity.HomeActivity
import com.artevak.kasirpos.ui.activity.auth.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {
    lateinit var binding : ActivitySplashBinding
    private val viewModel : SplashViewModel by viewModel()
    lateinit var i : Intent
    var TAG_DETAIL = "detailuser"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (viewModel.getUsername().isEmpty()){
            i = Intent(this, LoginActivity::class.java)
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
            gotoHome()
        }
    }

    private fun gotoHome(){
        i = Intent(this, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
}