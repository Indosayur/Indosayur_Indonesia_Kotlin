package adapter

import activity.DetailProdukActivity
import helper.Helper
import model.Produk
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import util.Config
import kotlin.collections.ArrayList

class AdapterProduk(var activity: Activity, private var data:ArrayList<Produk>):RecyclerView.Adapter<AdapterProduk.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama: TextView = view.findViewById(R.id.tv_nama)
        val tvHarga: TextView = view.findViewById(R.id.tv_Harga)
        val imgProduk: ImageView = view.findViewById(R.id.img_produk)
        val tvHargaAsli: TextView = view.findViewById(R.id.tv_HargaAsli)
        val layout: CardView = view.findViewById(R.id.layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View= LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        val hargaAsli = Integer.valueOf(a.harga)
        var harga = Integer.valueOf(a.harga)

        if(a.discount !=0){
            harga -= a.discount
        }

        holder.tvHargaAsli.text = Helper().gantirupiah(hargaAsli)
        holder.tvHargaAsli.paintFlags = holder.tvHargaAsli.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().gantirupiah(harga)
        val image = Config.ProdukUrl + data[position].gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.logo_indosayurthumbnail)
            .error(R.drawable.logo_indosayurthumbnail)
            .into(holder.imgProduk)

        holder.layout.setOnClickListener{
            val activiti = Intent(activity, DetailProdukActivity::class.java)
            val str = Gson().toJson(data[position], Produk::class.java)
            activiti.putExtra("extra",str)
            activity.startActivity(activiti)

        }
    }

    override fun getItemCount(): Int {
        return data.size

    }

}