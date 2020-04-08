package com.wyze.threading.eventbus;

import com.wyze.threading.general.FetchVM;
import com.wyze.threading.general.Progressable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Can't be cancelled
 */
public class AvController {
    private final FetchVM fetchVM;

    public AvController(FetchVM fetchVM) {
        this.fetchVM = fetchVM;
    }

    public void registerActivity() {
        EventBus.getDefault().register(this);
    }

    public void unregisterActivity() {
        EventBus.getDefault().unregister(this);
    }

    public void fetchAudioAndVideo() {
        fetchVM.started();
        final VideoController.VideoFetchEvent videoFetchEvent = new VideoController.VideoFetchEvent();
        videoFetchEvent.progressable = fetchVM.getVideoProgress();
        videoFetchEvent.startProgress = (float) (Math.max((Math.random() * 25), 5));

        final AudioController.AudioFetchEvent audioFetchEvent = new AudioController.AudioFetchEvent();
        audioFetchEvent.progressable = fetchVM.getAudioProgress();
        audioFetchEvent.startProgress = (float) (Math.max((Math.random() * 50), 5));

        final EventBus eventBus = EventBus.getDefault();
        eventBus.post(videoFetchEvent);
        eventBus.post(audioFetchEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateProgress(FetchProgress fetchProgress) {
        fetchProgress.progressable.updateProgress(fetchProgress.currentProgress);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void done(FetchDone fetchDone) {
        fetchDone.progressable.stopped();
    }

    static class FetchProgress {
        Progressable progressable;
        float currentProgress;
    }

    static class FetchDone {
        Progressable progressable;
    }
}
