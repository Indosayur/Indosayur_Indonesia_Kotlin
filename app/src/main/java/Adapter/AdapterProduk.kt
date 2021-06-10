package Adapter

import Model.Produk
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterProduk(var data:ArrayList<Produk>):RecyclerView.Adapter<AdapterProduk.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_Harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View= LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(Integer.valueOf(data[position].harga))
//        holder.imgProduk.setImageResource(data[position].gambar)
        val image = "http://192.168.1.6/indosayur/public/storage/produk/" + data[position].gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.logo_indosayurthumbnail)
            .error(R.drawable.logo_indosayurthumbnail)
            .into(holder.imgProduk)
    }

    override fun getItemCount(): Int {
        return data.size

    }

}