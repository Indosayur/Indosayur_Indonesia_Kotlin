package app

import model.Responmodel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("phone") phone : String,
        @Field("password") password : String
    ) :Call<Responmodel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) :Call<Responmodel>

     @GET("produk")
    fun getproduct() :Call<Responmodel>

}