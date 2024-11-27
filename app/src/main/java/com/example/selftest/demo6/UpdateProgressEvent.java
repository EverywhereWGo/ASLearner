package com.example.selftest.demo6;

/**
 * @author LMH
 */
public class UpdateProgressEvent {
    public UpdateProgressEvent(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    int progress;

    public UpdateProgressEvent() {
    }
}
