package com.ljb.datastore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ljb.datastore.databinding.ActivityMainBinding
import com.ljb.datastore.preferences.PreferencesActivity
import com.ljb.datastore.proto.ProtoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPreference.setOnClickListener(onclick)
            btnProto.setOnClickListener(onclick)
        }
    }

    private val onclick = OnClickListener{ view ->
        startActivity(
            Intent(
                applicationContext,
                when (view.id) {
                    binding.btnPreference.id -> PreferencesActivity::class.java
                    binding.btnProto.id -> ProtoActivity::class.java
                    else -> MainActivity::class.java
                }
            )
        )
    }
}