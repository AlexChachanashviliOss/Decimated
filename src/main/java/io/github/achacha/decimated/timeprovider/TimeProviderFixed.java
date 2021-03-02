package io.github.achacha.decimated.timeprovider;

public class TimeProviderFixed implements TimeProvider {
    private long now;

    public TimeProviderFixed() {
    }

    public void setMillis(long now) {
        this.now = now;
    }

    public void addMillis(long millis) {
        this.now += millis;
    }

    @Override
    public long getMillis() {
        return now;
    }
}
