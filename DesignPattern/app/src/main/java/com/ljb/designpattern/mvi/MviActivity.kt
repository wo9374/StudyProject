package com.ljb.designpattern.mvi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.ljb.base.BaseActivity
import com.ljb.designpattern.NewsAdapter
import com.ljb.designpattern.NewsDecoration
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityPatternsBinding
import com.ljb.extension.setVisibility
import com.ljb.extension.showToast
import org.orbitmvi.orbit.viewmodel.observe

/**
 * 2023-12-02
 * Orbit 을 이용한 MVVM 기반 MVI 패턴 구현
 * */
class MviActivity: BaseActivity<ActivityPatternsBinding>(R.layout.activity_patterns) {

    val viewModel : MviViewModel by viewModels()

    private val newsAdapter = NewsAdapter()

    private val queryTextListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null)
                viewModel.getSearchNews(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MviActivity

            searchView.setOnQueryTextListener(queryTextListener)

            recycler.adapter = newsAdapter
            recycler.addItemDecoration(NewsDecoration())
        }

        // State 와 SideEffect 를 관찰
        viewModel.observe(
            lifecycleOwner = this,
            state = ::handleState,
            sideEffect = ::handleSideEffect
        )

        viewModel.setUpUI()
    }


    /**
     * [MviViewModel]의 container의 state가 변할때 마다 호출
     * */
    private fun handleState(state: NewsState) {
        if (!state.loading) {
            binding.progressCircular.setVisibility(false)

            state.exception?.let { e -> //오류 처리
                throw Exception(e.message)
            } ?: run { //성공 처리
                newsAdapter.submitList(state.list)
            }
        } else {
            //로딩 표시
            binding.progressCircular.setVisibility(true)
        }
    }

    /**
     * [MviViewModel]의 sideEffect가 보내질 때 마다 호출
     * */
    private fun handleSideEffect(sideEffect: NewsSideEffect) {
        when (sideEffect) {
            is NewsSideEffect.Success -> {
                showToast(getString(R.string.result_success))
            }
            is NewsSideEffect.Empty ->
                showToast(getString(R.string.result_empty))

            is NewsSideEffect.Error ->
                showToast(sideEffect.errorMessage)
        }
    }
}