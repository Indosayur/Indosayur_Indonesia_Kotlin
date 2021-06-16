package helper

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.NumberFormat
import java.util.*

class Helper {
    fun gantirupiah(string: String) :String{
        return NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(string))
    }

    fun gantirupiah(value: Int) :String{
        return NumberFormat.getCurrencyInstance(Locale("in","ID")).format(value)
    }

    fun setToolbar(activity: Activity, toolbar: Toolbar, title:String){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}