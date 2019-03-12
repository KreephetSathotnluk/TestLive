package com.mfec.live

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.mfec.live.common.CommonFunction
import com.mfec.live.common.Constant
import com.mfec.live.live.view.fragmemt.LiveFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_live -> {
                CommonFunction.replaceFragment(
                    supportFragmentManager,
                    LiveFragment.newInstance(Constant.FRAGMENT_TAG.LIVE),
                    R.id.container,
                    Constant.FRAGMENT_TAG.LIVE
                )
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_player -> {
                CommonFunction.replaceFragment(
                    supportFragmentManager,
                    LiveFragment.newInstance(Constant.FRAGMENT_TAG.PLAYER),
                    R.id.container,
                    Constant.FRAGMENT_TAG.PLAYER
                )
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        initSetOnClickView()
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


//    private fun initSetOnClickView() {
//        initOnClick(btnMainLine)
//    }
//
//    private fun initOnClick(view: View) {
//        view.setOnClickListener {
//            when (view.id) {
//                R.id.btnMainLine -> {
//                    val i = Intent(this, LiveActivity::class.java)
//                    startActivity(i)
//                }
//            }
//        }
//    }
}
