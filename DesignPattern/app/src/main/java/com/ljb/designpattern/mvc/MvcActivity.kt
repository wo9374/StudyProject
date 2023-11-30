package com.ljb.designpattern.mvc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ljb.base.BaseActivity
import com.ljb.designpattern.NewsAdapter
import com.ljb.designpattern.NewsDecoration
import com.ljb.designpattern.NewsResponse
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityMvcBinding
import com.ljb.extension.setVisibility
import com.ljb.network.RetrofitService.naverService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MvcActivity : BaseActivity<ActivityMvcBinding>(R.layout.activity_mvc) {

    private var _newsList = MutableStateFlow<UiState<NewsResponse>>(UiState.Loading)
    private val newsList: StateFlow<UiState<NewsResponse>> get() = _newsList
    private val newsAdapter = NewsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

            //검색을 완료 하였을 경우 (키보드 '검색' 돋보기 버튼을 선택 하였을 경우)
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query.isNullOrEmpty()) {
                    Toast.makeText(this@MvcActivity, getString(R.string.query_hint), Toast.LENGTH_SHORT).show()
                    true    //키보드 내리지 않음
                } else {
                    getSearchNaverNews(query)
                    false   //키보드 내림
                }
            }

            //검색어를 변경할 때마다 실행
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        binding.recycler.adapter = newsAdapter
        binding.recycler.addItemDecoration(NewsDecoration())

        observeData()

        getSearchNaverNews("안드로이드")
    }

    private fun observeData() = lifecycleScope.launch {
        newsList
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collectLatest { uiState ->

                when (uiState) {
                    is UiState.Complete, is UiState.Empty, is UiState.Fail -> {
                        binding.progressCircular.setVisibility(false)

                        if (uiState is UiState.Complete)
                            newsAdapter.submitList(uiState.data.items)

                        if (uiState is UiState.Empty)
                            Toast.makeText(this@MvcActivity, getString(R.string.result_empty), Toast.LENGTH_SHORT).show()

                        if (uiState is UiState.Fail)
                            Toast.makeText(this@MvcActivity, uiState.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> binding.progressCircular.setVisibility(true)
                }
            }
    }


    fun getSearchNaverNews(keyword: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                _newsList.emit(UiState.Loading)

                naverService.getSearchNews(query = keyword, sort = "sim").run {
                    if (isSuccessful) {
                        body()?.let { response ->
                            if (response.items.isEmpty())
                                _newsList.emit(UiState.Empty)
                            else
                                _newsList.emit(UiState.Complete(response))
                        }
                    } else
                        _newsList.emit(UiState.Fail(message()))
                }
            }
        }
    }
}

sealed class UiState<out T> {
    data object Empty : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Complete<out T>(val data: T) : UiState<T>()
    data class Fail(val message: String?) : UiState<Nothing>()
}
