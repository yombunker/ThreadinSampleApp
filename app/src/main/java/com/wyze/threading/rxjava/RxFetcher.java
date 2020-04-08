package com.wyze.threading.rxjava;

import androidx.core.util.Pair;

import com.wyze.threading.general.FetchVM;
import com.wyze.threading.general.Progressable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxFetcher {
    public Disposable fetchAudioAndVideo(FetchVM fetchVM) {
        float startProgress = (float) Math.max((Math.random() * 25), 5);
        final Observable<Float> videoFetchObservable = animateProgress(startProgress, 4, fetchVM.getVideoProgress());

        startProgress = (float) Math.max((Math.random() * 25), 5);
        final Observable<Float> audioFetchObservable = animateProgress(startProgress, 2, fetchVM.getAudioProgress());

        return Observable
                .combineLatest(videoFetchObservable, audioFetchObservable, Pair::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchProgress -> {
                        },
                        fetchVM::error,
                        fetchVM::stopped);
    }

    private Observable<Float> animateProgress(float startProgress, float maxDelay, Progressable progressable) {
        @NonNull final Observable<Long> downloadSpeed = Observable.interval((int) Math.ceil(Math.random() * maxDelay), TimeUnit.SECONDS);

        final Observable<Float> fetchJob = Observable
                .just(startProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aFloat -> {
                    progressable.started();
                    return aFloat;
                })
                .observeOn(Schedulers.computation())
                .concatMap(progress -> {
                    List<Float> progressTicks = new ArrayList<>();
                    float currentProgress = progress;
                    while (Float.compare(currentProgress, 100F) < 0) {
                        progressTicks.add(currentProgress);
                        currentProgress += (Math.random() * 10F);
                    }
                    progressTicks.add(100F);

                    return Observable.fromIterable(progressTicks);
                });

        return Observable.zip(fetchJob, downloadSpeed, (progress, tick) -> progress)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(new Observer<Float>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Float aFloat) {
                        // Enable this to test error handling
//                        if (aFloat > 20F) {
//                            throw new IllegalStateException("sucks");
//                        }

                        progressable.updateProgress(aFloat);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressable.error(e);
                    }

                    @Override
                    public void onComplete() {
                        progressable.stopped();
                    }
                });
    }
}
