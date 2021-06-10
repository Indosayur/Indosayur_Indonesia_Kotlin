package Activity

import Helper.SharedPref
import Model.Responmodel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.indosayurindonesiakotlin.MainActivity
import com.example.indosayurindonesiakotlin.R
import com.inyongtisto.tokoonline.app.ApiConfig
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_masuk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        s = SharedPref(this)


        btn_login.setOnClickListener {
            login()
        }
    }
    fun login (){
        if (edt_email.text.isEmpty()){
            edt_email.error="Kolom email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_kata_sandi.text.isEmpty()){
            edt_kata_sandi.error="Kolom kata sandi tidak boleh kosong"
            edt_kata_sandi.requestFocus()
            return
        }
        pb.visibility=View.VISIBLE
        ApiConfig.instanceRetrofit.login(edt_email.text.toString(),edt_kata_sandi.text.toString()).enqueue(object: Callback<Responmodel> {
            override fun onFailure(call: Call<Responmodel>, t: Throwable) {
                pb.visibility=View.GONE
                Toast.makeText(this@LoginActivity,"Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Responmodel>, response: Response<Responmodel>) {
                pb.visibility=View.GONE
                val respon = response.body()!!

                if (respon.success == 1){
                    s.setStatusLogin(true)
                    s.setUser(respon.user)

//                    s.setString(s.name,respon.user.name)
//                    s.setString(s.phone,respon.user.phone)
//                    s.setString(s.Email,respon.user.email)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@LoginActivity,"Selamat Datang, "+respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity,"Error:"+respon.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}