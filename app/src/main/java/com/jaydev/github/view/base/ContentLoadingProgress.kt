package com.jaydev.github.view.base

import com.jaydev.github.common.cancelIfActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContentLoadingProgress(
    val coroutineScope: CoroutineScope
) {
    companion object {
        private const val MIN_LOADING_TIME = 1000L
        private const val MIN_LOADING_DELAY = 500L
    }

    private var postedLoadingHide = false
    private var postedLoadingShow = false
    private var loadingDismissed = false
    private var startTime: Long = -1

    private var delayedHideJob: Job? = null
    private var delayedShowJob: Job? = null

    suspend fun handleContentLoading(
        loading: MutableStateFlow<Boolean>,
        isLoading: Boolean
    ) {
        if (isLoading) showProgress(loading) else hideProgress(loading)
    }

    suspend fun showProgress(loading: MutableStateFlow<Boolean>) {
        startTime = -1
        loadingDismissed = false
        delayedHideJob?.cancelIfActive()
        postedLoadingHide = false
        if (!postedLoadingShow) {
            delayedShowJob?.cancelIfActive()
            delayedShowJob = coroutineScope.launch {
                delay(MIN_LOADING_DELAY)
                postedLoadingShow = false
                if (!loadingDismissed) {
                    startTime = System.currentTimeMillis()
                    loading.value = true
                }
            }
            postedLoadingShow = true
        }
    }

    suspend fun hideProgress(loading: MutableStateFlow<Boolean>) {
        loadingDismissed = true
        delayedShowJob?.cancelIfActive()
        postedLoadingShow = false
        val diff: Long = System.currentTimeMillis() - startTime
        if (diff >= MIN_LOADING_TIME || startTime == -1L) {
            loading.value = false
        } else {
            if (!postedLoadingHide) {
                delayedHideJob?.cancelIfActive()
                delayedHideJob = coroutineScope.launch {
                    delay(MIN_LOADING_TIME - diff)
                    postedLoadingHide = false
                    startTime = -1
                    loading.value = false
                }
                postedLoadingHide = true
            }
        }
    }
}