package adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import helper.Helper
import model.Transaksi
import java.text.SimpleDateFormat

class AdapterRiwayat(private var data:ArrayList<Transaksi>, private var listener : Listeners):RecyclerView.Adapter<AdapterRiwayat.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        val tvNama: TextView = view.findViewById(R.id.tv_nama)
        val tvHarga: TextView = view.findViewById(R.id.tv_harga)
        val tvTanggal: TextView = view.findViewById(R.id.tv_tgl)
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val tvJumlah: TextView = view.findViewById(R.id.tv_jumlah)
        val layout: CardView = view.findViewById(R.id.layout)
    }

    lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View= LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a: Transaksi = data[position]


        val namaProduk: String = a.details[0].produk.name

        holder.tvNama.text = namaProduk
        holder.tvHarga.text = Helper().gantirupiah(a.total_transfer.toInt() + a.kode_unik)
        holder.tvJumlah.text = a.total_item + " Items"
        holder.tvStatus.text = a.status

//        val formatBaru = "d MMM yyyy"
//        val formatLama = "yyyy-MM-dd kk:mm:ss"
//
//        val dateFormat = SimpleDateFormat(formatLama)
//        val convert =  dateFormat.parse(a.created_at)
//        dateFormat.applyPattern(formatBaru)
//        val tanggalBaru = dateFormat.format(convert)
        holder.tvTanggal.text = a.created_at


        var color = context.getColor(R.color.Orange)
        if (a.status == "SELESAI") color = context.getColor(R.color.Hijau)
        else if (a.status == "BATAL") color = context.getColor(R.color.Merah)

        holder.tvStatus.setTextColor(color)

        holder.layout.setOnClickListener{
            listener.onClicked(a)
        }
    }

    interface Listeners{
        fun onClicked(data: Transaksi)
    }
}