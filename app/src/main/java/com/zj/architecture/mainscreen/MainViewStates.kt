package com.zj.architecture.mainscreen

import com.zj.architecture.mockapi.Banner
import com.zj.architecture.repository.NewsItem
import com.zj.architecture.utils.FetchStatus


data class MainViewState(
    val fetchStatus: FetchStatus = FetchStatus.NotFetched,
    val newsList: List<NewsItem> = emptyList(),
    val bannerList: List<Banner> = emptyList()
)

sealed class MainViewEvent {
    data class ShowSnackbar(val message: String) : MainViewEvent()
    data class ShowToast(val message: String) : MainViewEvent()
}

sealed class MainViewAction {
    data class NewsItemClicked(val newsItem: NewsItem) : MainViewAction()
    object FabClicked : MainViewAction()
    object OnSwipeRefresh : MainViewAction()
    object FetchNews : MainViewAction()
    object FetchBanner : MainViewAction()
}