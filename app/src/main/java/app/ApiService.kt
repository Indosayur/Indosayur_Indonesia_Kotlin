package app

import model.Checkout
import model.Responmodel
import model.rajaongkir.ResponOngkir
import retrofit2.Call
import retrofit2.http.*

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

    @POST("checkout")
    fun checkout(
        @Body data : Checkout
    ) :Call<Responmodel>

     @GET("produk")
    fun getproduct() :Call<Responmodel>

    @GET("province")
    fun getprovinsi(
        @Header("key") key : String
    ) :Call<Responmodel>

    @GET("city")
    fun getkota(
        @Header("key") key : String,
        @Query("province") id: String
    ) :Call<Responmodel>

    @GET("kecamatan")
    fun getkecamatan(
        @Query("id_kota") id: Int
    ) :Call<Responmodel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
        @Header("key") key : String,
        @Field("origin") origin : String,
        @Field("destination") destination : String,
        @Field("weight") weight : Int,
        @Field("courier") courier : String

    ) :Call<ResponOngkir>

    @GET("checkout/user/{id}")
    fun getRiwayat(
        @Path("id") id: Int
    ) :Call<Responmodel>

    @POST("checkout/batal/{id}")
    fun batalCheckout(
        @Path("id") id: Int
    ) :Call<Responmodel>

}