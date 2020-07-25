package com.sagaRock101.tmdb.paging

import androidx.paging.DataSource
import kotlin.reflect.KClass

class GenericFactory<K, R>(private val kClass: KClass<DataSource<K, R>>) : DataSource.Factory<K, R>() {
    override fun create(): DataSource<K, R> = kClass.java.newInstance()
}