package com.wyze.threading.coroutines

import com.wyze.threading.general.FetchVM
import com.wyze.threading.general.Progressable
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.math.ceil
import kotlin.math.max

internal class AvFetcher {
    suspend fun fetchAudioAndVideo(fetchVM: FetchVM) {
        coroutineScope {
            val videoFetch = async {
                Fetcher().fetch(
                    max((Math.random() * 25F), 5.toDouble()).toFloat(),
                    4,
                    fetchVM.videoProgress
                )
            }

            val audioFetch = async {
                Fetcher().fetch(
                    max((Math.random() * 50F), 5.toDouble()).toFloat(),
                    2,
                    fetchVM.audioProgress
                )
            }

            videoFetch.await()
            audioFetch.await()
        }
    }
}

internal class Fetcher {
    suspend fun fetch(
        startProgress: Float,
        maxDelay: Int,
        progressable: Progressable
    ) {
        var currentProgress = startProgress
        val downloadSpeed = ceil(Math.random() * maxDelay).toInt()

        progressable.started()
        while (currentProgress.compareTo(100f) < 0) {
            yield()

            delay(TimeUnit.SECONDS.toMillis(downloadSpeed.toLong()))

//            // Enable this to test error handling
//            if (currentProgress > 50F) {
//                throw IllegalStateException("Crashing just for fun")
//            }

            withContext(Dispatchers.Main) {
                progressable.updateProgress(currentProgress)
            }

            currentProgress += (Math.random() * 10F).toFloat()
        }

        withContext(Dispatchers.Main) {
            progressable.updateProgress(100F)
            progressable.stopped()
        }
    }
}