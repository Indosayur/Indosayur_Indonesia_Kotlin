package Activity

import Helper.SharedPref
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indosayurindonesiakotlin.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_masuk.*

class MasukActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)

        mainbutton()

    }

   private fun mainbutton(){
        btn_proseslogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

        }

        btn_register.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

}