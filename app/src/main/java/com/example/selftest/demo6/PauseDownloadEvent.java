package com.example.selftest.demo6;

/**
 * @author LMH
 */
public class PauseDownloadEvent {
    public PauseDownloadEvent(float progress) {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    float progress;
    public PauseDownloadEvent() {
    }
}
