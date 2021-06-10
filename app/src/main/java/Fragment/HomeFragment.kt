package Fragment

import Adapter.AdapterProduk
import Model.Produk
import Model.Responmodel
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.indosayurindonesiakotlin.MainActivity
import com.example.indosayurindonesiakotlin.R
import com.inyongtisto.tokoonline.app.ApiConfig
import com.inyongtisto.tutorial.adapter.AdapterSlider
import kotlinx.android.synthetic.main.activity_masuk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var vpslider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvTerlaris: RecyclerView
    lateinit var rvTambahan: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

    fun getproduct(){
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

    fun init(view: View){
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}