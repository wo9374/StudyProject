package com.ljb.designpattern.mvi

import androidx.lifecycle.ViewModel
import com.ljb.designpattern.NewsData
import com.ljb.designpattern.NewsRepository
import com.ljb.extension.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class MviViewModel : ContainerHost<NewsState, NewsSideEffect>, ViewModel() {

    //한 개의 State와 한 개의 SideEffect를 만듬
    override val container: Container<NewsState, NewsSideEffect> = container(NewsState())

    private val newsRepository = NewsRepository()

    fun getSearchNews(query: String) = intent {
        reduce { state.copy(loading = true) }

        newsRepository.getSearchNews(query).collectLatest { networkState ->
            when (networkState) {
                is NetworkState.Success -> {
                    reduce { state.copy(loading = false) }

                    if (networkState.data.isEmpty()) {
                        postSideEffect(NewsSideEffect.Empty)
                    } else {
                        reduce { state.copy(list = networkState.data, loading = false) }
                        postSideEffect(NewsSideEffect.Success)
                    }
                }

                is NetworkState.Error -> {
                    reduce { state.copy(exception = Exception("Error Code : ${networkState.errorCode} - ${networkState.message}")) }
                    postSideEffect(NewsSideEffect.Error(networkState.message))
                }
            }
        }
    }
}

data class NewsState(
    val list: List<NewsData> = emptyList(),
    val loading: Boolean = true,
    val exception: Exception? = null,
)

sealed class NewsSideEffect {
    data object Success : NewsSideEffect()
    data object Empty : NewsSideEffect()
    data class Error(val errorMessage: String?) : NewsSideEffect()
}