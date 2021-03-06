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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indosayurindonesiakotlin.R
import helper.SharedPref
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import model.Produk
import room.MyDatabase

class KeranjangFragmentFragment : Fragment() {
    private lateinit var myDb : MyDatabase
    lateinit var s:SharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View= inflater.inflate(R.layout.fragment_keranjang, container, false)
        myDb = MyDatabase.getInstance(requireActivity())!!
        s = SharedPref(requireActivity())
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

    private var totalHarga = 0
    fun hitungTotal(){
        val listproduct = myDb.daoKeranjang().getAll() as ArrayList
        totalHarga = 0
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
            val listDelete = ArrayList<Produk>()
            for (p in listproduct){
                if (p.selected) listDelete.add(p)
            }

            delete(listDelete)

        }
        btnBayar.setOnClickListener{

            if (s.getStatusLogin()){
                var isThereProduk = false
                for (p in listproduct){
                    if (p.selected) isThereProduk = true
                }

                if (isThereProduk) {val intent = Intent(requireActivity(),PengirimanActivity::class.java)
                    intent.putExtra("extra","" + totalHarga )
                    startActivity(intent)

                } else {
                    Toast.makeText(requireContext(),"Tidak ada produk yang terpilih", Toast.LENGTH_SHORT).show()
                }
            } else {
                requireActivity().startActivity(android.content.Intent(requireActivity(), activity.MasukActivity::class.java))
            }

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

    private fun delete(data: ArrayList<Produk>){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().delete(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listproduct.clear()
                listproduct.addAll(myDb.daoKeranjang().getAll() as ArrayList)
                adapter.notifyDataSetChanged()
            })
    }

    private lateinit var btnDelete: ImageView
    private lateinit var rvProduk: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnBayar: TextView
    private lateinit var selectAll: CheckBox

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