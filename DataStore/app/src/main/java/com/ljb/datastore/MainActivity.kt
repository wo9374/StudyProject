package com.ljb.datastore

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ljb.datastore.databinding.ActivityMainBinding
import com.ljb.datastore.preferences.PreferencesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPreference.setOnClickListener { onClickStartActivity(it) }
    }

    private fun onClickStartActivity(view: View) {
        startActivity(
            Intent(
                applicationContext,
                when (view.id) {
                    binding.btnPreference.id -> PreferencesActivity::class.java
                    binding.btnProto.id -> MainActivity::class.java
                    else -> MainActivity::class.java
                }
            )
        )
    }
}