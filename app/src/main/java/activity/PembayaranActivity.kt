package activity

import adapter.AdapterBank
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.ApiConfig
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import helper.Helper
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.toolbar.*
import model.Bank
import model.Checkout
import model.Responmodel
import model.Transaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        Helper().setToolbar(this, toolbar,"Pilih Bank")

        displayBank()
    }

    private fun displayBank(){
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA", "0640578190", "Indosayur Indonesia",R.drawable.bca))
        arrBank.add(Bank("BNI", "0640578191", "Indosayur Indonesia",R.drawable.bni))
        arrBank.add(Bank("BRI", "0640578192", "Indosayur Indonesia",R.drawable.bri))
        arrBank.add(Bank("Mandiri", "0640578193", "Indosayur Indonesia",R.drawable.mandiri))
        arrBank.add(Bank("Kaltimtara", "0640578194", "Indosayur Indonesia",R.drawable.kaltimtara))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_data.layoutManager = layoutManager
        rv_data.adapter = AdapterBank(arrBank,object :AdapterBank.Listeners{
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })
    }

    fun bayar(bank: Bank){
        val json = intent.getStringExtra("extra")!!.toString()
        val checkout = Gson().fromJson(json,Checkout::class.java)
        checkout.bank = bank.nama

        ApiConfig.instanceRetrofit.checkout(checkout).enqueue(object: Callback<Responmodel> {
            override fun onFailure(call: Call<Responmodel>, t: Throwable) {
//                Toast.makeText(this@PengirimanActivity,"Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Responmodel>, response: Response<Responmodel>) {

                val respon = response.body()!!
                if (respon.success == 1){

                    val jsBank = Gson().toJson(bank,Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.Transaksi,Transaksi::class.java)
                    val jsCheckout = Gson ().toJson(checkout,Checkout::class.java)

                    val intent = Intent(this@PembayaranActivity,SuccessActivity::class.java)
                    intent.putExtra("bank",jsBank)
                    intent.putExtra("transaksi",jsTransaksi)
                    intent.putExtra("checkout",jsCheckout)
                    startActivity(intent)



                } else {
                    Toast.makeText(this@PembayaranActivity,"Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}