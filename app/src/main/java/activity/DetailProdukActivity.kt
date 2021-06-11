package activity

import helper.Helper
import model.Produk
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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


class DetailProdukActivity : AppCompatActivity() {

    lateinit var produk: Produk


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        getinfo()
        mainButton()

    }

    private fun mainButton(){
        btn_keranjang.setOnClickListener{
            insert()
        }

        btn_favorit.setOnClickListener{
            val myDb: MyDatabase = MyDatabase.getInstance(this)!! // call database
            val listdata = myDb.daoKeranjang().getAll() // get All data
            for(note :Produk in listdata){
                println("-----------------------")
                println(note.name)
                println(note.harga)
            }
        }
    }

    fun insert(){
        val myDb: MyDatabase = MyDatabase.getInstance(this)!! // call database
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data inserted")
            })
    }
    private fun getinfo(){
        val data = intent.getStringExtra("extra")
         produk = Gson().fromJson<Produk>(data, Produk::class.java)

        tv_nama.text = produk.name
        tv_harga.text = Helper().gantirupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi

        val img = "http://192.168.1.11/indosayur/public/storage/produk/" + produk.gambar
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.logo_indosayurthumbnail)
            .error(R.drawable.logo_indosayurthumbnail)
            .resize(300,300)
            .into(image)

        //set toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = produk.name
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}