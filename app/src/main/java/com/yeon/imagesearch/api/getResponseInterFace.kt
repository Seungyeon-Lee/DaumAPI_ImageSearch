package com.yeon.imagesearch.api

import io.reactivex.Single
import java.util.LinkedHashMap

interface getResponseInterFace<T> {
    fun getResponse(): Single<T>
    fun queryValuesMap(): LinkedHashMap<String, String>
}