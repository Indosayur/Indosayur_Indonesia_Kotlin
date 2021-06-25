package adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import com.squareup.picasso.Picasso
import helper.Helper
import model.DetailTransaksi
import util.Config

class AdapterProdukTransaksi(private var data:ArrayList<DetailTransaksi>):RecyclerView.Adapter<AdapterProdukTransaksi.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val imgProduk: ImageView = view.findViewById(R.id.img_produk)
        val tvNama: TextView = view.findViewById(R.id.tv_nama)
        val tvHarga: TextView = view.findViewById(R.id.tv_harga)
        val tvTotalHarga: TextView = view.findViewById(R.id.tv_totalHarga)
        val tvJumlah: TextView = view.findViewById(R.id.tv_jumlah)
        val layout: CardView = view.findViewById(R.id.layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View= LayoutInflater.from(parent.context).inflate(R.layout.item_produk_transaksi, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]
        val name = a.produk.name
        val p = a.produk

        holder.tvNama.text = name
        holder.tvHarga.text = Helper().gantirupiah(p.harga)
        holder.tvTotalHarga.text = Helper().gantirupiah(a.total_harga)
        holder.tvJumlah.text = a.total_item.toString() + " Items"

        holder.layout.setOnClickListener{
//            listener.onClicked(a)
        }

        val image = Config.ProdukUrl + p.gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.logo_indosayurthumbnail)
            .error(R.drawable.logo_indosayurthumbnail)
            .into(holder.imgProduk)
    }



    interface Listeners{
        fun onClicked(data: DetailTransaksi)
    }
}