package com.jaydev.github.base

import androidx.lifecycle.MutableLiveData
import com.jaydev.github.common.cancelIfActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    fun handleContentLoading(
        loadingLiveData: MutableLiveData<Boolean>,
        isLoading: Boolean
    ) {
        if (isLoading) showProgress(loadingLiveData) else hideProgress(loadingLiveData)
    }

    fun showProgress(loadingLiveData: MutableLiveData<Boolean>) {
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
                    loadingLiveData.value = true
                }
            }
            postedLoadingShow = true
        }
    }

    fun hideProgress(loadingLiveData: MutableLiveData<Boolean>) {
        loadingDismissed = true
        delayedShowJob?.cancelIfActive()
        postedLoadingShow = false
        val diff: Long = System.currentTimeMillis() - startTime
        if (diff >= MIN_LOADING_TIME || startTime == -1L) {
            loadingLiveData.value = false
        } else {
            if (!postedLoadingHide) {
                delayedHideJob?.cancelIfActive()
                delayedHideJob = coroutineScope.launch {
                    delay(MIN_LOADING_TIME - diff)
                    postedLoadingHide = false
                    startTime = -1
                    loadingLiveData.value = false
                }
                postedLoadingHide = true
            }
        }
    }
}