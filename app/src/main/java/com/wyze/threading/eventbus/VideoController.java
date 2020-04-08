package com.wyze.threading.eventbus;

import com.wyze.threading.general.Progressable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

public class VideoController {

    public void registerActivity() {
        EventBus.getDefault().register(this);
    }

    public void unregisterActivity() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void fetchVideo(VideoFetchEvent fetchEvent) {
        float currentProgress = fetchEvent.startProgress;
        final int downloadSpeed = (int) Math.ceil(Math.random() * 4);

        final EventBus eventBus = EventBus.getDefault();
        while (Float.compare(currentProgress, 100F) < 0) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(downloadSpeed));
            } catch (InterruptedException e) {
                // STUB
            }

            final AvController.FetchProgress videoProgress = new AvController.FetchProgress();
            videoProgress.progressable = fetchEvent.progressable;
            videoProgress.currentProgress = currentProgress;
            eventBus.post(videoProgress);
            currentProgress += (Math.random() * 10F);
        }

        final AvController.FetchProgress videoProgress = new AvController.FetchProgress();
        videoProgress.progressable = fetchEvent.progressable;
        videoProgress.currentProgress = 100F;
        eventBus.post(videoProgress);
        final AvController.FetchDone event = new AvController.FetchDone();
        event.progressable = fetchEvent.progressable;
        eventBus.post(event);
    }

    static class VideoFetchEvent {
        Progressable progressable;
        float startProgress;
    }
}
