package com.proximity.practicaljanak.ui.home

import android.content.Context
import android.content.Intent
import com.proximity.practicaljanak.R
import com.proximity.practicaljanak.base.BaseActivity
import com.proximity.practicaljanak.databinding.ActivityHomeScreenBinding
import com.proximity.practicaljanak.extensions.getClassName
import com.proximity.practicaljanak.ui.home.fragment.AirQualityListFragment
import com.proximity.practicaljanak.viewmodels.HomeScreenViewModel
import kotlin.reflect.KClass

class HomeScreenActivity : BaseActivity<HomeScreenViewModel, ActivityHomeScreenBinding>() {

    override val modelClass: KClass<HomeScreenViewModel>
        get() = HomeScreenViewModel::class

    override fun getViewBinding(): ActivityHomeScreenBinding =
        ActivityHomeScreenBinding.inflate(layoutInflater)


    override fun initView() {
        replaceFragment()
    }

    override fun listeners() { //No listener to be handle here
    }

    private fun replaceFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.flContainer,
            AirQualityListFragment.newInstance(),
            AirQualityListFragment.getClassName()
        ) //fragmentTransaction.addToBackStack(AirQualityListFragment.getClassName())
        fragmentTransaction.commit()
    }

    companion object {
        fun startActivity(context: Context) {
            val mIntent = Intent(context, HomeScreenActivity::class.java)
            context.startActivity(mIntent)
        }
    }
}