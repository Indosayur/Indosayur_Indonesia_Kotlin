package activity

import adapter.AdapterRiwayat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.ApiConfig
import com.example.indosayurindonesiakotlin.R
import helper.Helper
import helper.SharedPref
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import model.Responmodel
import model.Transaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        Helper().setToolbar(this, toolbar,"Riwayat Belanja")
        getRiwayat()

    }

    fun getRiwayat(){

        val id = SharedPref(this).getUser()!!.id

        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object:
        Callback<Responmodel> {
        override fun onFailure(call: Call<Responmodel>, t: Throwable) {
        }

        override fun onResponse(call: Call<Responmodel>, response: Response<Responmodel>) {
            val res = response.body()!!
            if(res.success == 1){
                displayRiwayat(res.Transaksis)
            }

        }
    })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat.adapter = AdapterRiwayat(transaksis, object : AdapterRiwayat.Listeners{
            override fun onClicked(data: Transaksi) {

            }

        })
        rv_riwayat.layoutManager = layoutManager

    }

    // tombol back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}