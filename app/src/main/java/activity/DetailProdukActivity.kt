package activity

import android.content.Intent
import helper.Helper
import model.Produk
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.activity_detail_produk.tv_nama
import kotlinx.android.synthetic.main.toolbar_custom.*
import room.MyDatabase
import util.Config


class DetailProdukActivity : AppCompatActivity() {

    lateinit var produk: Produk
    private lateinit var myDb: MyDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        myDb = MyDatabase.getInstance(this)!! // call database

        getinfo()
        mainButton()
        checkkeranjang()

    }

    private fun mainButton(){
        btn_keranjang.setOnClickListener{
            val data = myDb.daoKeranjang().getProduk(produk.Id)
            if(data == null){
                insert()
            }else{
                data.jumlah += 1
                update(data)
            }


        }

        btn_favorit.setOnClickListener{
            val listdata = myDb.daoKeranjang().getAll() // get All data
            for(note :Produk in listdata){
                println("-----------------------")
                println(note.name)
                println(note.harga)
            }
        }
        btn_toKeranjang.setOnClickListener{
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }

    private fun insert(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkkeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this,"Item Ditambahkan ke Keranjang",Toast.LENGTH_SHORT).show()
            })
    }

    private fun update(data: Produk){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkkeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this,"Item Ditambahkan ke Keranjang",Toast.LENGTH_SHORT).show()
            })
    }

    private fun checkkeranjang(){
        val datakeranjang = myDb.daoKeranjang().getAll()
        if (datakeranjang.isNotEmpty()){
            div_angka.visibility = View.VISIBLE
            tv_angka.text = datakeranjang.size.toString()
        } else {
            div_angka.visibility = View.GONE
        }
    }

    private fun getinfo(){
        val data = intent.getStringExtra("extra")
         produk = Gson().fromJson(data, Produk::class.java)

        tv_nama.text = produk.name
        tv_harga.text = Helper().gantirupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi

        val img = Config.ProdukUrl + produk.gambar
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.logo_indosayurthumbnail)
            .error(R.drawable.logo_indosayurthumbnail)
            .resize(300,300)
            .into(image)

        setSupportActionBar(toolbar_custom)
        supportActionBar!!.title = produk.name
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}