package model

class Responmodel {
    var success = 0
    lateinit var message:String
    var user = User()
    var produk:ArrayList<Produk> = ArrayList()
    var Transaksis:ArrayList<Transaksi> = ArrayList()

    var rajaongkir = ModelAlamat()
    val Transaksi = Transaksi()

    var provinsi:ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten:ArrayList<ModelAlamat> = ArrayList()
    var kecamatan:ArrayList<ModelAlamat> = ArrayList()

}