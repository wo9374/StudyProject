package com.ljb.ktor.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ljb.ktor.R
import com.ljb.ktor.databinding.ActivityNewsBinding
import com.ljb.ktor.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    private val newsAdapter = NewsAdapter()

    private val _newsList = MutableStateFlow<List<NewsData>>(emptyList())
    val newsList : StateFlow<List<NewsData>> get() = _newsList

    private val newsRepository = NewRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        binding.apply {
            lifecycleOwner = this@NewsActivity

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