package activity

import adapter.AdapterProdukTransaksi
import adapter.AdapterRiwayat
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.ApiConfig
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import helper.Helper
import kotlinx.android.synthetic.main.activity_detail_transaksi.*
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import model.DetailTransaksi
import model.Responmodel
import model.Transaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTransaksiActivity : AppCompatActivity() {

    var transaksi = Transaksi()

    //bagian ini merupakan rumah, jadi semua fungsi dipanggil disini
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi) //layout
        Helper().setToolbar(this, toolbar,"Invoice") //mengaktifkan toolbar dan tombol back

        val json = intent.getStringExtra("transaksi")
        transaksi = Gson().fromJson(json, Transaksi::class.java) //terima data dari riwayat activity

        setData(transaksi)
        displayProduk(transaksi.details)
        mainButton()
    }

    fun mainButton(){
        btn_batal.setOnClickListener{
            batalTransaksi()
        }

    }

    fun batalTransaksi(){
        ApiConfig.instanceRetrofit.batalCheckout(transaksi.id).enqueue(object:
            Callback<Responmodel> {
            override fun onFailure(call: Call<Responmodel>, t: Throwable) {
            }

            override fun onResponse(call: Call<Responmodel>, response: Response<Responmodel>) {
                val res = response.body()!!
                if(res.success == 1){
                    Toast.makeText(this@DetailTransaksiActivity,"Transaksi Dibatalkan",Toast.LENGTH_SHORT).show()
                    onBackPressed()
//                    displayRiwayat(res.Transaksis)
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun setData (t: Transaksi){
        tv_status.text = t.status
        tv_tgl.text = t.created_at

//        val formatBaru = "dd MMMM yyyy, kk:mm:ss"
//        tv_tgl.text = Helper().convertTanggal(t.created_at, formatBaru)

        tv_penerima.text = t.name + " - " + t.phone
        tv_alamat.text = t.detail_lokasi
        tv_kodeUnik.text = t.kode_unik.toString()
        tv_totalBelanja.text =Helper().gantirupiah(t.total_harga)
        tv_total.text = Helper().gantirupiah(t.total_transfer.toInt() + t.kode_unik)
        tv_ongkir.text = Helper().gantirupiah(t.ongkir)

        if (t.status != "MENUNGGU") div_footer.visibility = View.GONE

        var color = getColor(R.color.Orange)
        if (t.status == "SELESAI") color = getColor(R.color.Hijau)
        else if (t.status == "BATAL") color = getColor(R.color.Merah)
        tv_status.setTextColor(color)
    }
    private fun displayProduk(transaksis: ArrayList<DetailTransaksi>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_produk.adapter = AdapterProdukTransaksi(transaksis)
        rv_produk.layoutManager = layoutManager

    }

    //tombol back diaktifkan di oncreate
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}