package com.example.selftest.demo6;

/**
 * @author LMH
 */
public class UpdateProgressEvent {
    public UpdateProgressEvent(Float progress) {
        this.progress = progress;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    Float progress;

    public UpdateProgressEvent() {
    }
}
