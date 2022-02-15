package com.proximity.practicaljanak.common

//Generic class that contains data, status and error while handling response from repository.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val isLoadingShow: Boolean? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading<T>(isLoadingShow: Boolean) : Resource<T>(isLoadingShow = isLoadingShow)
    class Error<T>(message: String) : Resource<T>(message = message)
    class NoInternetError<T>(message: String) : Resource<T>(message = message)
}