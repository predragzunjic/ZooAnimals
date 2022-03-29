package com.example.randomzooanimals.util

import kotlinx.coroutines.CoroutineDispatcher

//when we test coroutines, we need special dispatchers, so we dont hardcode it
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}