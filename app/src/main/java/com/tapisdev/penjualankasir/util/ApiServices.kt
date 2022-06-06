package com.tapisdev.penjualankasir.util


import com.tapisdev.penjualankasir.model.LoginInfo
import com.tapisdev.penjualankasir.model.RegisterInfo
import com.tapisdev.penjualankasir.response.BarangResponse
import com.tapisdev.penjualankasir.response.CommonResponse
import com.tapisdev.penjualankasir.response.DetailUserResponse
import com.tapisdev.penjualankasir.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("login")
    fun loginUser(@Body userData: LoginInfo) : Call<LoginResponse>

    @GET("logout")
    fun logoutUser(@Query("token") token : String) : Call<CommonResponse>

    @POST("register")
    fun registerUser(@Body userData: RegisterInfo) : Call<CommonResponse>

    @GET("user/detail")
    fun detailUser(@Query("token") token : String) : Call<DetailUserResponse>

    @POST("barang/add")
    fun addBarang(@Query("token") token: String?,@Body file : RequestBody) : Call<CommonResponse>

    @GET("barang/data")
    fun getBarang(@Query("token") token: String?,@Query("page") page : Int, @Query("search") search : String) : Call<BarangResponse>

    @GET("barang/data/stok-tipis")
    fun getBarangStokTipis(@Query("token") token: String?,@Query("page") page : Int, @Query("search") search : String) : Call<BarangResponse>

    @GET("barang/delete")
    fun deleteBarang(@Query("token") token: String?,@Query("id_barang") id_barang : String) : Call<CommonResponse>



}


