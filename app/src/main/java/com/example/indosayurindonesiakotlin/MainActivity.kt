package com.example.indosayurindonesiakotlin

import activity.MasukActivity
import fragment.AkunFragment
import fragment.HomeFragment
import fragment.KeranjangFragmentFragment
import helper.SharedPref
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentAkun: Fragment = AkunFragment()
    private val fragmentKeranjang: Fragment = KeranjangFragmentFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private val statuslogin = false
    private lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharedPref(this)

        setUpBottomNav()
    }
    private fun setUpBottomNav(){
        fm.beginTransaction().add(R.id.container,fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container,fragmentAkun).hide(fragmentAkun).commit()
        fm.beginTransaction().add(R.id.container,fragmentKeranjang).hide(fragmentKeranjang).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.navigation_home->{

                    callfragment(0, fragmentHome )
                }
                R.id.navigation_Keranjang->{

                    callfragment(1, fragmentKeranjang )
                }
                R.id.navigation_Akun->{
                    if (s.getStatusLogin()){
                        callfragment(2, fragmentAkun )
                    } else {
                        startActivity(Intent(this,MasukActivity::class.java))
                    }


                }
            }

            false
        }

    }

    private fun callfragment (int: Int, fragment: Fragment){
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active=fragment

    }
}