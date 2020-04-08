package com.wyze.threading

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wyze.threading.asynctask.AsyncAudioAndVideoFetch
import com.wyze.threading.coroutines.AvFetcher
import com.wyze.threading.eventbus.AudioController
import com.wyze.threading.eventbus.AvController
import com.wyze.threading.eventbus.VideoController
import com.wyze.threading.general.FetchVM
import com.wyze.threading.leeroy.YoloFetcher
import com.wyze.threading.rxjava.RxFetcher
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    // General
    private lateinit var fetchVm: FetchVM

    // Leeroy
    private lateinit var yoloFetcher: YoloFetcher

    // AsyncTask
    private lateinit var asyncTaskFetcher: AsyncAudioAndVideoFetch

    // EventBus
    private lateinit var avController: AvController
    private lateinit var videoController: VideoController
    private lateinit var audioController: AudioController

    // RxJava
    private lateinit var rxFetcher: RxFetcher
    private var fetcherDisposable: Disposable? = null

    // Coroutines
    private lateinit var avFetcher: AvFetcher
    private var avFetchJob: Job? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<View>(R.id.content)
        findViewById<Button>(R.id.ui_interaction_tester).setOnTouchListener { _, event ->
            root.setBackgroundResource(
                when (event.actionMasked) {
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> R.color.background
                    else -> R.color.ui_works
                }
            )

            return@setOnTouchListener true
        }

        fetchVm = FetchVM(
            findViewById(R.id.video_progress),
            findViewById(R.id.video_icon),
            findViewById(R.id.audio_progress),
            findViewById(R.id.audio_icon)
        )

        // Leeroy
        yoloFetcher = YoloFetcher()

        // AsyncTask
        asyncTaskFetcher = AsyncAudioAndVideoFetch()

        // EventBus
        avController = AvController(fetchVm)
        videoController = VideoController()
        audioController = AudioController()

        //RxJava
        rxFetcher = RxFetcher()

        // Coroutines
        avFetcher = AvFetcher()
    }

    override fun onStart() {
        super.onStart()

        // Register listeners
        avController.registerActivity()
        videoController.registerActivity()
        audioController.registerActivity()
    }

    override fun onStop() {
        // Cancel pending tasks
        cancelAllTasks()


        // Unregister listeners
        avController.unregisterActivity()
        videoController.unregisterActivity()
        audioController.unregisterActivity()

        super.onStop()
    }

    fun executeLeeroy(@Suppress("UNUSED_PARAMETER") view: View) {
        cancelAllTasks()
        yoloFetcher.fetchAudioAndVideo(fetchVm)
    }

    fun executeAsyncTask(@Suppress("UNUSED_PARAMETER") view: View) {
        cancelAllTasks()
        asyncTaskFetcher.fetchAudioAndVideo(fetchVm)
    }

    fun executeEventBus(@Suppress("UNUSED_PARAMETER") view: View) {
        cancelAllTasks()
        avController.fetchAudioAndVideo()
    }

    fun executeRxJava(@Suppress("UNUSED_PARAMETER") view: View) {
        cancelAllTasks()

        fetcherDisposable = rxFetcher.fetchAudioAndVideo(fetchVm)
    }

    fun executeCoroutines(@Suppress("UNUSED_PARAMETER") view: View) {
        cancelAllTasks()

        CoroutineScope(Dispatchers.Default).apply {
            avFetchJob = launch {
                try {
                    avFetcher.fetchAudioAndVideo(fetchVm)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Done", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun cancelTasks(@Suppress("UNUSED_PARAMETER") view: View) {
        cancelAllTasks()
    }

    private fun cancelAllTasks() {
        // cancel AsyncTask
        asyncTaskFetcher.cancel()

        // cancel coroutines
        avFetchJob?.cancel()

        // Cancel rxJAva
        fetcherDisposable?.dispose()

        // Reset the views
        fetchVm.reset()
    }
}
