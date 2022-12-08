package com.test.cdcn_appmobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.extension.replaceFragment
import com.test.cdcn_appmobile.ui.launch.login.LoginFragment
import com.test.cdcn_appmobile.ui.main.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        replaceFragment(R.id.ctReplaceFragment, LoginFragment())
//        replaceFragment(R.id.ctReplaceFragment, HomeFragment())
    }
}