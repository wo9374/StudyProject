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

        //검색을 완료 하였을 경우 (키보드 '검색' 돋보기 버튼을 선택 하였을 경우)
        override fun onQueryTextSubmit(query: String?): Boolean {

            if (query != null) {
                lifecycleScope.launch{
                    presenter.loadData(query)
                }
            }

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