package com.example.selftest.demo6;

/**
 * @author LMH
 */
public class SyncStateEvent {
    private float progress;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private int flag;

    public SyncStateEvent() {
    }

    public SyncStateEvent(float progress , int flag) {
        this.progress = progress;
        this.flag = flag;
    }
}
