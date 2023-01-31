package com.zj.architecture.repository

import com.zj.architecture.mockapi.Banner
import com.zj.architecture.mockapi.MockApi
import com.zj.architecture.mockapi.TestApi
import com.zj.architecture.utils.PageState
import kotlinx.coroutines.delay

class NewsRepository {

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: NewsRepository().also { instance = it }
            }
    }

    suspend fun getMockApiResponse(): PageState<List<NewsItem>> {
        val articlesApiResult = try {
            delay(2000)
            MockApi.create().getLatestNews()
        } catch (e: Exception) {
            return PageState.Error("-1","Error")
        }

        articlesApiResult.articles?.let { list ->
            return PageState.Success(data = list)
        } ?: run {
            return PageState.Error("-2","Failed to get News")
        }
    }

    suspend fun getBannerResponse(): PageState<List<Banner>> {
        val response = try {
            TestApi.create().getBanner()
        } catch (e: Exception) {
            return PageState.Error("-1","Failed to get Banner")
        }

        response.let { result ->
//            if (result.code == 200) {
            if (result.code == 0) {
                return PageState.Success(data = (result.data as List<Banner>))
            } else {
                return PageState.Error(result.code.toString(), result.msg ?: "")
            }
        }
    }
}