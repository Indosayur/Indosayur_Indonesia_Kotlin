package activity

import adapter.AdapterKurir
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.ApiConfigAlamat
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import helper.Helper
import helper.SharedPref
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar.*
import model.Checkout
import model.rajaongkir.Costs
import model.rajaongkir.ResponOngkir
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import room.MyDatabase
import util.ApiKey

class PengirimanActivity : AppCompatActivity() {
    private lateinit var myDb : MyDatabase
    private var totalharga = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        Helper().setToolbar(this, toolbar,"Pengiriman")
        myDb = MyDatabase.getInstance(this)!!

        totalharga = Integer.valueOf(intent.getStringExtra("extra")!!)

        tv_totalBelanja.text = Helper().gantirupiah(totalharga)
        mainButton()
        setSpinner()
    }

    private fun setSpinner(){
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this,R.layout.item_spinner,arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position !=0) {

                    getOngkir(spn_kurir.selectedItem.toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkAlamat(){

        if (myDb.daoAlamat().getByStatus(true)!=null){
            div_alamat.visibility=View.VISIBLE
            div_kosong.visibility=View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = a.alamat +", "+a.kota+","+a.kodepos +", ("+a.type+")"
            btn_tambahAlamat.text = "Ubah Alamat"


            getOngkir("JNE")

        } else{
            div_alamat.visibility=View.GONE
            div_kosong.visibility=View.VISIBLE
            btn_tambahAlamat.text = "Tambah Alamat"
        }
    }
    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener{
            startActivity(Intent(this,ListAlamatActivity::class.java))
        }
        btn_bayar.setOnClickListener{
            bayar()

        }
    }

    private fun bayar(){
        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAlamat().getByStatus(true)!!

        val listproduct = myDb.daoKeranjang().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
        val produks = ArrayList<Checkout.Item>()

        for (p in listproduct){
            if(p.selected){
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga))

                val produk = Checkout.Item()
                produk.id = "" + p.Id
                produk.total_item = "" +p.jumlah
                produk.total_harga = "" + (p.jumlah * Integer.valueOf(p.harga))
                produk.catatan = "Catatan Baru"

                produks.add(produk)
            }
        }

        val checkout = Checkout()
        checkout.user_id = "" + user.id
        checkout.total_item = "" + totalItem
        checkout.total_harga = "" + totalHarga
        checkout.name = a.name
        checkout.phone = a.phone
        checkout.jasa_pengiriman = jasakirim
        checkout.ongkir = ongkir
        checkout.kurir = kurir
        checkout.detail_lokasi = tv_alamat.text.toString()
        checkout.total_transfer = "" + (totalHarga + Integer.valueOf(ongkir))
        checkout.produks = produks

        val json = Gson().toJson(checkout,Checkout::class.java)
        Log.d("respon", "json$json")
        val intent = Intent(this, PembayaranActivity::class.java)
        intent.putExtra("extra",json)

        startActivity(intent)
    }

    private fun getOngkir(kurir: String){

        val alamat = (myDb.daoAlamat().getByStatus(true))

        val origin = "467"
        val destination = "" + alamat!!.id_kota
        val weight = 1000
        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key,origin,destination,weight,kurir.lowercase()).enqueue(object:
            Callback<ResponOngkir> {
            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {

                Log.d("Error","Gagal memuat data " + t.message)
            }

            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {

                if (response.isSuccessful) {

                    Log.d("Success","Berhasil memuat data " + response.message())
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()){
                        displayOngkir(result[0].code.uppercase(),result[0].costs)
                    }

                }else {
                    Log.d("Error","Gagal memuat data " + response.message())
                }
            }
        })

    }

    var ongkir = ""
    var kurir = ""
    var jasakirim = ""

    private fun displayOngkir(_kurir: String,arrayList : ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices){
            val ongkir = arrayList[i]
            if (i==0){
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }

        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        kurir = _kurir
        jasakirim = arrayOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter : AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir,_kurir,object : AdapterKurir.Listeners{


            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for(ongkir in arrayOngkir) {
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }

                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                kurir = _kurir
                jasakirim = data.service
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    fun setTotal(ongkir: String){
        tv_ongkir.text = Helper().gantirupiah(ongkir)
        tv_total.text = Helper().gantirupiah(Integer.valueOf(ongkir)+totalharga)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        checkAlamat()
        super.onResume()
    }
}