package com.ljb.designpattern.mvc

import android.os.Bundle
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ljb.base.BaseActivity
import com.ljb.designpattern.NewsAdapter
import com.ljb.designpattern.NewsData
import com.ljb.designpattern.NewsDecoration
import com.ljb.designpattern.NewsRepository
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityPatternsBinding
import com.ljb.extension.NetworkState
import com.ljb.extension.UiState
import com.ljb.extension.checkEmptyData
import com.ljb.extension.setVisibility
import com.ljb.extension.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MvcActivity : BaseActivity<ActivityPatternsBinding>(R.layout.activity_patterns) {


    private val newsRepository = NewsRepository()

    private val newsAdapter = NewsAdapter()

    private var _newsList = MutableStateFlow<UiState<List<NewsData>>>(UiState.Loading)
    private val newsList: StateFlow<UiState<List<NewsData>>> get() = _newsList

    private val queryTextListener = object : OnQueryTextListener {

        //검색을 완료 하였을 경우 (키보드 '검색' 돋보기 버튼을 선택 하였을 경우)
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null)
                searchNews(query)

            //return false   //키보드 내림
            return true     //키보드 내리지 않음
        }

        //검색어를 변경할 때마다 실행
        override fun onQueryTextChange(newText: String?): Boolean {
            return false
            //제안을 표시하는 기본 작업을 수행해야 하는 경우 false
            //해당 작업이 리스너에 의해 직접 처리하는 경우 true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MvcActivity

            searchView.setOnQueryTextListener(queryTextListener)

            recycler.adapter = newsAdapter
            recycler.addItemDecoration(NewsDecoration())
        }

        observeData()

        //초기 "안드로이드" 검색
        searchNews("안드로이드")
    }

    private fun observeData() = lifecycleScope.launch {
        newsList.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collectLatest { uiState ->
                when (uiState) {
                    is UiState.Complete, is UiState.Empty, is UiState.Fail -> {
                        binding.progressCircular.setVisibility(false)

                        if (uiState is UiState.Complete){
                            newsAdapter.submitList(uiState.data)
                            binding.searchView.clearFocus()
                            showToast(getString(R.string.result_success))
                        }

                        if (uiState is UiState.Empty)
                            showToast(getString(R.string.result_empty))

                        if (uiState is UiState.Fail)
                            showToast(uiState.message)
                    }

                    else -> binding.progressCircular.setVisibility(true)
                }
            }
    }

    fun searchNews(query: String) = lifecycleScope.launch {
        _newsList.emit(UiState.Loading)

        newsRepository.getSearchNews(query).flowOn(Dispatchers.IO).collectLatest { networkState->
            when(networkState){
                is NetworkState.Success ->
                    _newsList.emit(checkEmptyData(networkState.data))

                is NetworkState.Error ->
                    _newsList.emit(UiState.Fail(networkState.message))
            }
        }
    }
}