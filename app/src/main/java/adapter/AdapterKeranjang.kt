package adapter

import helper.Helper
import model.Produk
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import room.MyDatabase
import util.Config
import kotlin.collections.ArrayList

class AdapterKeranjang(var activity: Activity, var data:ArrayList<Produk>, private var listener : Listeners): RecyclerView.Adapter<AdapterKeranjang.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
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
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val produk = data[position]
        val harga = Integer.valueOf(produk.harga)

        holder.tvNama.text = produk.name
        holder.tvHarga.text = Helper().gantirupiah(harga*produk.jumlah)

        var jumlah = data[position].jumlah
        holder.tvjumlah.text = jumlah.toString()

        holder.checkbox.isChecked = produk.selected
        holder.checkbox.setOnCheckedChangeListener {buttonView, isChecked ->
        produk.selected = isChecked
        update(produk)
        }

        val image = Config.ProdukUrl + data[position].gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.logo_indosayurthumbnail)
            .error(R.drawable.logo_indosayurthumbnail)
            .into(holder.imgProduk)

        holder.btnAdd.setOnClickListener {
            jumlah++
            produk.jumlah = jumlah
            update(produk)
            holder.tvjumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().gantirupiah(harga * jumlah)
        }
        holder.btnRemove.setOnClickListener {
            if (jumlah <= 1) return@setOnClickListener
            jumlah--
            produk.jumlah = jumlah
            update(produk)
            holder.tvjumlah.text = jumlah.toString()
            holder.tvHarga.text = Helper().gantirupiah(harga * jumlah)

        }
        holder.btnDelete.setOnClickListener {
            delete(produk)
            listener.onDelete(position)

        }
    }

    interface Listeners{
        fun onUpdate ()
        fun onDelete(position: Int)
    }

    private fun update(data: Produk){
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    private fun delete(data: Produk){
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            })
    }



}


