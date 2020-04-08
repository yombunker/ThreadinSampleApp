package com.wyze.threading.asynctask;

import android.os.AsyncTask;

import com.wyze.threading.general.Progressable;

import java.util.concurrent.TimeUnit;

public class AsyncFetch extends AsyncTask<Float, Float, Void> {
    private final Progressable progressable;

    AsyncFetch(Progressable progressable) {
        this.progressable = progressable;
    }

    @Override
    protected void onPreExecute() {
        progressable.started();
    }

    @Override
    protected Void doInBackground(Float... startProgress) {
        float currentProgress = startProgress[0];
        final int downloadSpeed = (int) Math.ceil(Math.random() * 2);

        progressable.started();
        while (Float.compare(currentProgress, 100F) < 0) {
            if (isCancelled()) {
                return null;
            }

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(downloadSpeed));
            } catch (InterruptedException e) {
                // STUB
            }

            publishProgress(currentProgress);
            currentProgress += (Math.random() * 10F);
        }

        publishProgress(100F);
        return null;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        progressable.updateProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aFloat) {
        progressable.stopped();
    }
}
