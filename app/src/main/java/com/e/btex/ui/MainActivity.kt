package com.e.btex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.e.btex.BTExApp
import com.e.btex.R
import com.e.btex.data.MyEx
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var myEx: MyEx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as BTExApp)
            .getAppComponent()
            .inject(this@MainActivity)
        Timber.e("${myEx.date}")
    }

}
