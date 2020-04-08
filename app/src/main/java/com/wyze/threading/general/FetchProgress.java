package com.wyze.threading.general;

import android.widget.ImageView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.wyze.threading.R;

public class FetchProgress implements Progressable {
    private final RoundCornerProgressBar progressBar;
    private final ImageView icon;
    private final int defaultColor;

    FetchProgress(RoundCornerProgressBar progressBar, ImageView icon) {
        this.progressBar = progressBar;
        this.icon = icon;
        this.defaultColor = progressBar.getProgressColor();
    }

    @Override
    public void reset() {
        progressBar.setProgress(0F);
        progressBar.setProgressColor(defaultColor);
        icon.setImageResource(R.drawable.ic_video_black_18dp);
    }

    @Override
    public void started() {
        icon.setImageResource(R.drawable.ic_play_circle_outline_black_18dp);
    }

    @Override
    public void updateProgress(float progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void stopped() {
        icon.setImageResource(R.drawable.ic_stop_black_18dp);
    }

    @Override
    public void error(Throwable error) {
        progressBar.setProgressColor(R.color.error);
    }
}
