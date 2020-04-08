package com.wyze.threading.general;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

public class FetchVM implements Progressable {
    private final Context context;
    private final FetchProgress videoProgress;
    private final FetchProgress audioProgress;

    public FetchVM(RoundCornerProgressBar videoProgressBar, ImageView videoIcon, RoundCornerProgressBar audioProgressBar, ImageView audioIcon) {
        this.context = videoIcon.getContext();
        this.videoProgress = new FetchProgress(videoProgressBar, videoIcon);
        this.audioProgress = new FetchProgress(audioProgressBar, audioIcon);
    }

    @Override
    public void reset() {
        videoProgress.reset();
        audioProgress.reset();
    }

    @Override
    public void started() {
        videoProgress.started();
        audioProgress.started();
    }

    @Override
    public void updateProgress(float progress) {
        videoProgress.updateProgress(progress);
        audioProgress.updateProgress(progress);
    }

    @Override
    public void stopped() {
        videoProgress.stopped();
        audioProgress.stopped();
        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(Throwable error) {
        Toast.makeText(context, "Error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public Progressable getVideoProgress() {
        return videoProgress;
    }

    public Progressable getAudioProgress() {
        return audioProgress;
    }
}
