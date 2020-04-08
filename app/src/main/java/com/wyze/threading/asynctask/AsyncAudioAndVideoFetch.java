package com.wyze.threading.asynctask;

import com.wyze.threading.general.FetchVM;

public class AsyncAudioAndVideoFetch {
    private AsyncFetch videoFetch;
    private AsyncFetch audioFetch;

    public void fetchAudioAndVideo(FetchVM fetchVM) {
        videoFetch = new AsyncFetch(fetchVM.getVideoProgress());
        audioFetch = new AsyncFetch(fetchVM.getAudioProgress());

        videoFetch.execute((float) Math.max((Math.random() * 25), 5));
        audioFetch.execute((float) Math.max((Math.random() * 25), 5));
    }

    public void cancel() {
        if (videoFetch != null) {
            videoFetch.cancel(false);
        }

        if (audioFetch != null) {
            audioFetch.cancel(false);
        }
    }
}
