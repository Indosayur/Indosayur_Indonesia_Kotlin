package fragment

import activity.PengirimanActivity
import adapter.AdapterKeranjang
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import model.Produk
import room.MyDatabase

class KeranjangFragmentFragment : Fragment() {
    lateinit var myDb : MyDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View= inflater.inflate(R.layout.fragment_keranjang, container, false)
        myDb = MyDatabase.getInstance(requireActivity())!!
        init(view)
        mainbutton()
        return view
    }
    lateinit var adapter : AdapterKeranjang
    var listproduct = ArrayList<Produk>()
    private  fun displayProduk(){
        listproduct = myDb.daoKeranjang().getAll() as ArrayList
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterKeranjang(requireActivity(),listproduct,object :AdapterKeranjang.Listeners{
            override fun onUpdate() {
                hitungTotal()
            }

            override fun onDelete(position: Int) {
                listproduct.removeAt(position)
                adapter.notifyDataSetChanged()
                hitungTotal()
            }
        })

        rvProduk.adapter = adapter
        rvProduk.layoutManager = layoutManager
    }
    fun hitungTotal(){
        val listproduct = myDb.daoKeranjang().getAll() as ArrayList
        var totalHarga = 0
        var isSelectedAll = true
        for (produk in listproduct) {
            if (produk.selected) {
                val harga = Integer.valueOf(produk.harga)
                totalHarga += (harga * produk.jumlah)
            } else {
                isSelectedAll = false

            }
        }
        selectAll.isChecked = isSelectedAll
        tvTotal.text = helper.Helper().gantirupiah(totalHarga)
    }

    private fun mainbutton(){
        btnDelete.setOnClickListener{


        }
        btnBayar.setOnClickListener{
            startActivity(Intent(requireActivity(),PengirimanActivity::class.java))

        }
        
        selectAll.setOnClickListener {
            for (i in listproduct.indices){
                val produk =listproduct[i]
                produk.selected = selectAll.isChecked
                listproduct[i] = produk
            }
            adapter.notifyDataSetChanged()
        }
    }

    lateinit var btnDelete: ImageView
    lateinit var rvProduk: RecyclerView
    lateinit var tvTotal: TextView
    lateinit var btnBayar: TextView
    lateinit var selectAll: CheckBox

    private fun init(view: View) {
        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduk = view.findViewById(R.id.rv_produk)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBayar =view.findViewById(R.id.btn_bayar)
        selectAll = view.findViewById(R.id.cb_all)
    }

    override fun onResume() {
        displayProduk()
        hitungTotal()
        super.onResume()
    }
}