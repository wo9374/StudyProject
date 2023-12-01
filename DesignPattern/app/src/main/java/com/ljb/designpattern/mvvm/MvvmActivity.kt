package com.ljb.designpattern.mvvm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ljb.base.BaseActivity
import com.ljb.designpattern.NewsAdapter
import com.ljb.designpattern.NewsDecoration
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityPatternsBinding
import com.ljb.extension.UiState
import com.ljb.extension.setVisibility
import com.ljb.extension.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MvvmActivity: BaseActivity<ActivityPatternsBinding>(R.layout.activity_patterns) {

    private val viewModel: MvvmViewModel by viewModels()

    private val newsAdapter = NewsAdapter()

    private val queryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            lifecycleScope.launch(Dispatchers.IO){
                if (query != null)
                    viewModel.getNews(query)
            }
            return true     //키보드 내리지 않음
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MvvmActivity

            searchView.setOnQueryTextListener(queryTextListener)

            recycler.adapter = newsAdapter
            recycler.addItemDecoration(NewsDecoration())

            observeData()

            lifecycleScope.launch {
                viewModel.getNews("안드로이드")
            }
        }
    }

    private fun observeData() = lifecycleScope.launch {
        viewModel.newsList
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collectLatest { uiState ->
                when(uiState){
                    is UiState.Complete, is UiState.Empty, is UiState.Fail -> {
                        binding.progressCircular.setVisibility(false)

                        if (uiState is UiState.Complete){
                            newsAdapter.submitList(uiState.data)
                            binding.searchView.clearFocus()
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
}