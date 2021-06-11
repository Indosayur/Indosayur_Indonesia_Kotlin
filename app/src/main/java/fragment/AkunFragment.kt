package fragment

import activity.LoginActivity
import helper.SharedPref
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.indosayurindonesiakotlin.R


class AkunFragment : Fragment() {

    private lateinit var s:SharedPref
    private lateinit var btnLogout:TextView
    private lateinit var tvnama:TextView
    private lateinit var tvphone:TextView
    private lateinit var tvemail:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View= inflater.inflate(R.layout.fragment_akun, container, false)

        init(view)

        s = SharedPref(requireActivity())
        btnLogout.setOnClickListener{
            s.setStatusLogin(false)
        }
        setData()
        return view
    }

    private fun setData(){

        if(s.getUser()==null){
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = s.getUser()!!

        tvnama.text = user.name
        tvemail.text = user.email
        tvphone.text = user.phone
    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvnama = view.findViewById(R.id.tvnama)
        tvemail = view.findViewById(R.id.tvemail)
        tvphone = view.findViewById(R.id.tvphone)

    }

}