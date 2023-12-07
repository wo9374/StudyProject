package com.ljb.datastore.proto

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ljb.datastore.Sample
import com.ljb.datastore.databinding.ActivityDatastoreBinding
import com.ljb.datastore.preferences.showToast
import com.ljb.datastore.proto.viewmodel.ProtoViewModel
import com.ljb.datastore.proto.viewmodel.ProtoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val Context.sampleDataStore: DataStore<Sample> by dataStore(
    fileName = "sample.pb",
    serializer = SampleSerializer
)

class ProtoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatastoreBinding
    private lateinit var viewModel: ProtoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatastoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
            ProtoViewModelFactory(SampleRepository(sampleDataStore))
        )[ProtoViewModel::class.java]

        setUi()
        observeData()
    }

    private fun setUi() {
        binding.apply {
            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val age = etAge.text.toString().toIntOrNull()
                val isMale = switchGender.isChecked

                if (name.isEmpty() || age == null) {
                    showToast("이름과 나이를 모두 입력하세요")
                } else {
                    CoroutineScope(Dispatchers.IO).launch { viewModel.setUserData(name, age, isMale) }
                }
            }

            btnClear.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch{ viewModel.clearUserData() }
            }
        }
    }

    private fun observeData() = lifecycleScope.launch {
        viewModel.flow.collectLatest { sampleProto ->
            if (sampleProto.initData){
                binding.apply {
                    tvAge.text = sampleProto.age.toString()
                    tvName.text = sampleProto.name
                    tvGender.text = if (sampleProto.gender) "여성" else "남성"
                }
            }else{
                binding.apply {
                    tvAge.text = ""
                    tvName.text = ""
                    tvGender.text = ""
                }
            }
        }
    }
}