package fragment

import adapter.AdapterProduk
import model.Produk
import model.Responmodel
import adapter.AdapterSlider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.indosayurindonesiakotlin.R
import app.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var vpslider: ViewPager
    private lateinit var rvProduk: RecyclerView
    private lateinit var rvTerlaris: RecyclerView
    private lateinit var rvTambahan: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view: View= inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        getproduct()

        return view
    }

    fun displayproduct(){

        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider (arrSlider, activity)
        vpslider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        rvProduk.adapter = AdapterProduk(requireActivity(),listproduct)
        rvProduk.layoutManager = layoutManager

        rvTerlaris.adapter = AdapterProduk(requireActivity(),listproduct)
        rvTerlaris.layoutManager = layoutManager2

        rvTambahan.adapter = AdapterProduk(requireActivity(),listproduct)
        rvTambahan.layoutManager = layoutManager3

    }

    private var listproduct: ArrayList<Produk> = ArrayList()

    private fun getproduct(){
        ApiConfig.instanceRetrofit.getproduct().enqueue(object:
            Callback<Responmodel> {
            override fun onFailure(call: Call<Responmodel>, t: Throwable) {
            }

            override fun onResponse(call: Call<Responmodel>, response: Response<Responmodel>) {
                val res = response.body()!!
                if(res.success == 1){
                    listproduct = res.produk
                    displayproduct()
                }

            }
        })

    }

    private fun init(view: View){
        vpslider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvTerlaris = view.findViewById(R.id.rv_Terlaris)
        rvTambahan = view.findViewById(R.id.rv_tambahan)
    }
//        val arrProduk: ArrayList<Produk>get(){
//            val arr = ArrayList<Produk>()
//            val P1 = Produk()
//            P1.nama="Sayur Asem"
//            P1.Harga="Rp.15.000"
//            P1.Gambar=R.drawable.sayur_asem
//
//            val P2 = Produk()
//            P2.nama="Sayur Bayam"
//            P2.Harga="Rp.15.000"
//            P2.Gambar=R.drawable.sayur_bayam
//
//            val P3 = Produk()
//            P3.nama="Sayur Nangka"
//            P3.Harga="Rp.15.000"
//            P3.Gambar=R.drawable.sayur_nangka
//
//            arr.add(P1)
//            arr.add(P2)
//            arr.add(P3)
//
//            return arr
//        }
//        val arrTerlaris: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val P1 = Produk()
//        P1.nama="Sayur Asem"
//        P1.Harga="Rp.15.000"
//        P1.Gambar=R.drawable.sayur_asem
//
//        val P2 = Produk()
//        P2.nama="Sayur Bayam"
//        P2.Harga="Rp.15.000"
//        P2.Gambar=R.drawable.sayur_bayam
//
//        val P3 = Produk()
//        P3.nama="Sayur Nangka"
//        P3.Harga="Rp.15.000"
//        P3.Gambar=R.drawable.sayur_nangka
//
//        arr.add(P1)
//        arr.add(P2)
//        arr.add(P3)
//
//        return arr
//    }
//        val arrTambahan: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val P1 = Produk()
//        P1.nama="Sayur Asem"
//        P1.Harga="Rp.15.000"
//        P1.Gambar=R.drawable.sayur_asem
//
//        val P2 = Produk()
//        P2.nama="Sayur Bayam"
//        P2.Harga="Rp.15.000"
//        P2.Gambar=R.drawable.sayur_bayam
//
//        val P3 = Produk()
//        P3.nama="Sayur Nangka"
//        P3.Harga="Rp.15.000"
//        P3.Gambar=R.drawable.sayur_nangka
//
//        arr.add(P1)
//        arr.add(P2)
//        arr.add(P3)
//
//        return arr
//    }
}