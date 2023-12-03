package com.ljb.ktor

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ljb.ktor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val newsAdapter = NewsAdapter()

    private val _newsList = MutableStateFlow<List<NewsData>>(emptyList())
    val newsList : StateFlow<List<NewsData>> get() = _newsList

    private val newsRepository = NewRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null)
                        searchNews(query)
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean { return false }
            })

            recycler.adapter = newsAdapter
            recycler.addItemDecoration(NewsDecoration())
        }

        observeData()
        searchNews("안드로이드")
    }

    private fun observeData() = lifecycleScope.launch {
        newsList
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collectLatest {
                newsAdapter.submitList(it)
            }
    }

    fun searchNews(query: String) = lifecycleScope.launch {
        newsRepository.getSearchNews(query).flowOn(Dispatchers.IO).collectLatest { network ->
            when(network){
                is NetworkState.Success ->
                    _newsList.emit(network.data)

                is NetworkState.ApiError ->
                    showToast("Error Code: ${network.errorCode}, Error Message: ${network.message}")

                is NetworkState.NetworkError ->
                    throw Exception(network.throwable)
            }
        }
    }
}

fun Context.showToast(str:String?) =
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()

fun String.htmlToString() : String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    else
        Html.fromHtml(this).toString()

val Int.dp: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()