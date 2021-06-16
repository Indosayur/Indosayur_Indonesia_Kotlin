package activity

import adapter.AdapterKurir
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import app.ApiConfigAlamat
import com.example.indosayurindonesiakotlin.R
import helper.Helper
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.toolbar.*
import model.rajaongkir.Costs
import model.rajaongkir.ResponOngkir
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import room.MyDatabase
import util.ApiKey

class PengirimanActivity : AppCompatActivity() {
    lateinit var myDb : MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        Helper().setToolbar(this, toolbar,"Pengiriman")
        myDb = MyDatabase.getInstance(this)!!
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

    fun checkAlamat(){

        if (myDb.daoAlamat().getByStatus(true)!=null){
            div_alamat.visibility=View.VISIBLE
            div_kosong.visibility=View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = a.alamat +", "+a.kota+", "+a.kecamatan+", "+a.kodepos +", ("+a.type+")"
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
    private fun displayOngkir(kurir:String,arrayList : ArrayList<Costs>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_metode.adapter = AdapterKurir(arrayList,kurir,object : AdapterKurir.Listeners{

            override fun onClicked(data: Costs, index: Int) {

            }
        })
        rv_metode.layoutManager = layoutManager
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