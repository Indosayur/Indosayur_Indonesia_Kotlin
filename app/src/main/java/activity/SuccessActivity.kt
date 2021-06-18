package activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.indosayurindonesiakotlin.MainActivity
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import helper.Helper
import kotlinx.android.synthetic.main.activity_success.*
import kotlinx.android.synthetic.main.toolbar.*
import model.Bank
import model.Checkout
import model.Transaksi
import room.MyDatabase

class SuccessActivity : AppCompatActivity() {

    private var nominal = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        Helper().setToolbar(this, toolbar,"Bank Transfer")
        setValues()
        mainButton()
    }

    private fun mainButton(){
        btn_copyNoRek.setOnClickListener{
            copyText(tv_nomorRekening.text.toString())
        }

        btn_copyNominal.setOnClickListener{
            copyText(nominal.toString())
        }

        btn_cekStatus.setOnClickListener{
            startActivity(Intent(this,RiwayatActivity::class.java))

        }

    }

    private fun copyText(text: String){
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)
        Toast.makeText(this,"Teks Berhasil DiCopy",Toast.LENGTH_LONG).show()

    }

    private fun setValues(){

        //tangkap data dari pembayaran activity
        val jsBank = intent.getStringExtra("bank")
        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("checkout")


        //konversi data
        val bank = Gson().fromJson(jsBank,Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi,Transaksi::class.java)
        val checkout = Gson().fromJson(jsCheckout,Checkout::class.java)

        //hapus keranjang
        val myDb = MyDatabase.getInstance(this)!!
        for(produk in checkout.produks){
            myDb.daoKeranjang().deleteByID(produk.id)
        }

        tv_nomorRekening.text = bank.rekening
        tv_namaPenerima.text = bank.penerima
        image_bank.setImageResource(bank.image)

        nominal = Integer.valueOf(transaksi.total_transfer) + transaksi.kode_unik
        tv_nominal.text = Helper().gantirupiah(nominal)
    }

    // tombol back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    //setting tombol back jump ke mainactivity
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}