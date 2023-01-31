package com.zj.architecture.mockapi

import com.google.gson.annotations.SerializedName

class BaseData<T> {
    @SerializedName("errorCode")
    var code = -1

    @SerializedName("errorMsg")
    var msg: String? = null
    var data: T? = null
}

data class Banner(
    var desc: String,
    var imagePath: String,
    var title: String,
    var url: String
)