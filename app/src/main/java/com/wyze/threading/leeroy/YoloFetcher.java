package com.wyze.threading.leeroy;

import com.wyze.threading.general.FetchVM;
import com.wyze.threading.general.Progressable;

import java.util.concurrent.TimeUnit;

public class YoloFetcher {
    public void fetchAudioAndVideo(FetchVM fetchVM) {
        float startProgress = (float) Math.max((Math.random() * 25), 4);
        animateProgress(startProgress, fetchVM.getVideoProgress());

        startProgress = (float) Math.max((Math.random() * 25), 5);
        animateProgress(startProgress, fetchVM.getAudioProgress());
    }

    private void animateProgress(float startProgress, Progressable progressable) {
        float currentProgress = startProgress;
        final int downloadSpeed = (int) Math.ceil(Math.random() * 2);

        progressable.started();
        while (Float.compare(currentProgress, 100F) < 0) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(downloadSpeed));
            } catch (InterruptedException e) {
                // STUB
            }

            progressable.updateProgress(currentProgress);
            currentProgress += (Math.random() * 10F);
        }

        progressable.updateProgress(100F);
        progressable.stopped();
    }
}
