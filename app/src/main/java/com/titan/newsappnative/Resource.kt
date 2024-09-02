package com.titan.newsappnative

class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val error: String?
) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(error: String?): Resource<T> {
            return Resource(Status.ERROR, null, error)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}