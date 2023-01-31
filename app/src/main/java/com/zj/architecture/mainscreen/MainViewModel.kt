package com.zj.architecture.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.zj.architecture.mockapi.Banner
import com.zj.architecture.network.rxLaunch
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.repository.NewsRepository
import com.zj.architecture.utils.FetchStatus
import com.zj.architecture.utils.PageState
import com.zj.architecture.utils.asLiveData
import com.zj.mvi.core.LiveEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private var count: Int = 0
    private val repository: NewsRepository = NewsRepository.getInstance()
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData(MainViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: LiveEvents<MainViewEvent> = LiveEvents() //一次性的事件，与页面状态分开管理
    val viewEvents = _viewEvents.asLiveData()

    fun dispatch(viewAction: MainViewAction) {
        when (viewAction) {
            is MainViewAction.NewsItemClicked -> newsItemClicked(viewAction.newsItem)
            MainViewAction.FabClicked -> fabClicked()
            MainViewAction.OnSwipeRefresh -> fetchNews()
            MainViewAction.FetchNews -> fetchNews()
            MainViewAction.FetchBanner -> fetchBanners()
        }
    }

    private fun newsItemClicked(newsItem: NewsItem) {
        _viewEvents.setEvent(MainViewEvent.ShowSnackbar(newsItem.title))
    }

    private fun fabClicked() {
        count++
        _viewEvents.setEvent(MainViewEvent.ShowToast(message = "Fab clicked count $count"))
    }

    private fun fetchNews() {
        _viewStates.setState {
            copy(fetchStatus = FetchStatus.Fetching)
        }
        viewModelScope.launch {
            when (val result = repository.getMockApiResponse()) {
                is PageState.Error -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched)
                    }
                    _viewEvents.setEvent(MainViewEvent.ShowToast(message = result.message))
                }
                is PageState.Success -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched, newsList = result.data)
                    }
                }
            }
        }
    }

    private fun fetchBanners() {
        _viewStates.setState {
            copy(fetchStatus = FetchStatus.Fetching)
        }
        viewModelScope.rxLaunch<PageState<List<Banner>>> {
            onRequest = {
                repository.getBannerResponse()
            }
            onSuccess = {
                _viewStates.setState {
                    copy(fetchStatus = FetchStatus.Fetched)
                }
                when (it) {
                    is PageState.Error -> {
                        _viewEvents.setEvent(
                            MainViewEvent.ShowToast(message = it.message)
                        )
                    }
                    is PageState.Success -> {
                        _viewStates.setState { copy(bannerList = it.data) }
                        _viewEvents.setEvent(
                            MainViewEvent.ShowToast(message = Gson().toJson(it.data))
                        )
                    }
                }
            }
            onError = { code, msg ->
                _viewStates.setState {
                    copy(fetchStatus = FetchStatus.Fetched)
                }
                _viewEvents.setEvent(MainViewEvent.ShowToast(message = msg))
            }
        }
    }
}