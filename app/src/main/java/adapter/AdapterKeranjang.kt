package adapter

import activity.DetailProdukActivity
import helper.Helper
import model.Produk
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class AdapterKeranjang(var activity: Activity, private var data:ArrayList<Produk>):RecyclerView.Adapter<AdapterKeranjang.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama: TextView = view.findViewById(R.id.tv_nama)
        val tvHarga: TextView = view.findViewById(R.id.tv_harga)
        val imgProduk: ImageView = view.findViewById(R.id.img_produk)
        val btnAdd: ImageView = view.findViewById(R.id.btn_add)
        val btnRemove: ImageView = view.findViewById(R.id.btn_remove)
        val btnDelete: ImageView = view.findViewById(R.id.btn_delete)
        val checkbox: CheckBox = view.findViewById(R.id.checkBox)
        val tvjumlah: TextView = view.findViewById(R.id.tv_jumlah)
        val layout: CardView = view.findViewById(R.id.layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View= LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var jumlah = data[position].jumlah

        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().gantirupiah(data[position].harga)
        holder.tvjumlah.text = jumlah.toString()
        val image = "http://192.168.1.11/indosayur/public/storage/produk/" + data[position].gambar
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