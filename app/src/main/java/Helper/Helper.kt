package Helper

import java.text.NumberFormat
import java.util.*

class Helper {
    fun gantirupiah(string: String) :String{
        return NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(string))
    }
}