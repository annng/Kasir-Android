package com.artevak.kasirpos.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.ui.activity.auth.LoginActivity
import com.artevak.kasirpos.databinding.ActivityProfileBinding
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.util.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : BaseActivity() {
    lateinit var binding : ActivityProfileBinding
    var TAG_LOGOUT = "logout"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        binding.lineLogout.setOnClickListener {
            sendLogoutRequest()
            logout()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
        binding.tvEditProfile.setOnClickListener {

        }

        updateUI()
    }

    fun updateUI(){
        val firstChara = mUserPref.getName()?.take(1)
        binding.tvInisial.setText(firstChara)
        binding.tvName.setText(mUserPref.getName())
        binding.tvEmail.setText(mUserPref.getEmail())
    }

    fun sendLogoutRequest(){
        ApiMain().services.logoutUser(mUserPref.getToken()!!).enqueue(
            object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    showInfoMessage("gagal logout user")
                    Log.d(TAG_LOGOUT,t.message.toString())
                }
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    val responAPI = response.body()
                    val responseStatus = response.code()

                    Log.d(TAG_LOGOUT,"status "+responseStatus.toString())

                    if(response.code() == 200) {
                        Log.d(TAG_LOGOUT,"body "+responAPI!!.toString())
                        Log.d(TAG_LOGOUT,"http code asli "+responseStatus.toString())
                        Log.d(TAG_LOGOUT,"http code dari API "+responAPI!!.http_status)
                        Log.d(TAG_LOGOUT,"Logout request berhasil ")

                    }
                    else{
                        Log.d(TAG_LOGOUT,"Logout request gagal ")
                    }
                }
            }
        )
    }
}