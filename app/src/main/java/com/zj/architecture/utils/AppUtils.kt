package com.zj.architecture.utils

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//const val BASE_URL = "https://your_api_endpoint.com/"
const val BASE_URL = "https://www.wanandroid.com/"

fun inflate(
    context: Context,
    viewId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(context).inflate(viewId, parent, attachToRoot)
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (!TextUtils.isEmpty(message)) {
        Toast.makeText(this, message, length).show()
    }
}

//LCE -> Loading/Content/Error
sealed class PageState<out T> {
    data class Success<T>(val data: T) : PageState<T>()
    data class Error<T>(val code: String, val message: String) : PageState<T>()
}

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}

