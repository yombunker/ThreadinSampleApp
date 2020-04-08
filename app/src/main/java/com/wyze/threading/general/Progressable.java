package com.wyze.threading.general;

public interface Progressable {
    void reset();

    void started();

    void updateProgress(float progress);

    void stopped();

    void error(Throwable error);
}
