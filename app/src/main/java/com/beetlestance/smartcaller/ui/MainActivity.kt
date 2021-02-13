package com.beetlestance.smartcaller.ui

import android.os.Bundle
import android.widget.Toast
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.utils.PhoneStateReceiver
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var phoneStateReceiver: PhoneStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        phoneStateReceiver.callState.observe(this) {
            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
        }
    }

}