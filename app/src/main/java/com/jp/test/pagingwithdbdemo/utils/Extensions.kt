package com.jp.test.pagingwithdbdemo.utils

import com.google.gson.Gson

fun <T> String.fromJson(classOfT: Class<T>): T = Gson().fromJson(this, classOfT)

fun Any?.toGson() = Gson().toJson(this)