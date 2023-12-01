package com.ljb.designpattern.mvp

import android.os.Bundle
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.lifecycleScope
import com.ljb.base.BaseActivity
import com.ljb.designpattern.NewsAdapter
import com.ljb.designpattern.NewsData
import com.ljb.designpattern.NewsDecoration
import com.ljb.designpattern.NewsRepository
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityPatternsBinding
import com.ljb.extension.UiState
import com.ljb.extension.setVisibility
import com.ljb.extension.showToast
import kotlinx.coroutines.launch

class MvpActivity: BaseActivity<ActivityPatternsBinding>(R.layout.activity_patterns), MvpContract.View {

    private var repository = NewsRepository()
    var presenter = MvpPresenter(this@MvpActivity, repository)

    private val newsAdapter = NewsAdapter()

    private val queryTextListener = object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {

            if (query != null) {
                lifecycleScope.launch{
                    presenter.loadData(query)
                }
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MvpActivity

            searchView.setOnQueryTextListener(queryTextListener)

            recycler.adapter = newsAdapter
            recycler.addItemDecoration(NewsDecoration())
        }

        lifecycleScope.launch{
            presenter.loadData("안드로이드")
        }
    }

    override fun setData(uiState: UiState<List<NewsData>>) {
        when (uiState) {
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