package com.kkbox.githuber_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kkbox.githuber_android.ui.SearchListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SearchListFragment.newInstance())
            .commit()
    }
}
