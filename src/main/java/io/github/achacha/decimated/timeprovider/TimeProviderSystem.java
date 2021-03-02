package io.github.achacha.decimated.timeprovider;

public class TimeProviderSystem implements TimeProvider {
    @Override
    public long getMillis() {
        return System.currentTimeMillis();
    }
}
