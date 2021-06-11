package activity

import helper.SharedPref
import model.Responmodel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.indosayurindonesiakotlin.MainActivity
import com.example.indosayurindonesiakotlin.R
import app.ApiConfig
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        s = SharedPref(this)

        btn_register.setOnClickListener{
            register()
        }
        btn_google.setOnClickListener{
            datadummy()

        }
    }
    private fun datadummy (){
        edt_nama.setText("Akhyar")
        edt_email.setText("Akhyar.asadullah@gmail.com")
        edt_nomor_telpon.setText("082234036161")
        edt_kata_sandi.setText("12345678")
    }

    private fun register (){
        if (edt_nama.text.isEmpty()){
            edt_nama.error="Kolom nama tidak boleh kosong"
            edt_nama.requestFocus()
            return
        } else if (edt_email.text.isEmpty()){
            edt_email.error="Kolom email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_nomor_telpon.text.isEmpty()){
            edt_nomor_telpon.error="Kolom nomor telepon tidak boleh kosong"
            edt_nomor_telpon.requestFocus()
            return
        } else if (edt_kata_sandi.text.isEmpty()){
            edt_kata_sandi.error="Kolom kata sandi tidak boleh kosong"
            edt_kata_sandi.requestFocus()
            return
        }
        pb.visibility=View.VISIBLE
        ApiConfig.instanceRetrofit.register(edt_nama.text.toString(),edt_email.text.toString(),edt_nomor_telpon.text.toString(),edt_kata_sandi.text.toString()).enqueue(object:Callback<Responmodel>{
            override fun onFailure(call: Call<Responmodel>, t: Throwable) {
                pb.visibility=View.GONE
                Toast.makeText(this@RegisterActivity,"Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Responmodel>, response: Response<Responmodel>) {
                pb.visibility=View.GONE
                val respon = response.body()!!
                if (respon.success == 1){
                    s.setStatusLogin(true)
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity,"Selamat Datang, "+respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity,"Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }

            }

        })

    }
}