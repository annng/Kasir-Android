package com.artevak.kasirpos.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.artevak.lokamotor.base.BaseActivity
import com.artevak.kasirpos.MainActivity
import com.artevak.kasirpos.databinding.ActivitySplashBinding
import com.artevak.kasirpos.model.UserModel
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.DetailUserResponse
import com.artevak.kasirpos.util.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            i = Intent(this, MainActivity::class.java)
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
            Log.d(TAG_DETAIL,"token nya : "+mUserPref.getToken())
            getDetailUser()
        }
    }

    fun getDetailUser(){
        ApiMain().services.detailUser(mUserPref.getToken()!!).enqueue(
            object : Callback<DetailUserResponse> {
                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    showInfoMessage("gagal mendapatkan data user")
                    Log.d(TAG_DETAIL,t.message.toString())
                    goToLogin()
                }
                override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                    val responAPI = response.body()
                    val responseStatus = response.code()

                    Log.d(TAG_DETAIL,"status "+responseStatus.toString())

                    if(response.code() == 200) {
                        Log.d(TAG_DETAIL,"body "+responAPI!!.toString())
                        Log.d(TAG_DETAIL,"http code asli "+responseStatus.toString())
                        Log.d(TAG_DETAIL,"http code dari API "+responAPI!!.http_status)

                        val userModel = responAPI!!.userModel
                        setSession(userModel)

                        val intentt = Intent(this@SplashActivity, HomeActivity::class.java)
                        startActivity(intentt)
                        finish()
                    }else if (response.code() == 401){
                        showErrorMessage("Akses token ditolak, silahkan lagin dahulu")
                        goToLogin()
                        finish()
                    }
                    else{
                        showErrorMessage("Akses token ditolak, silahkan lagin dahulu")
                        goToLogin()
                        finish()
                    }
                }
            }
        )
    }

    fun goToLogin(){
        i = Intent(this,MainActivity::class.java)
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