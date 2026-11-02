package nl.daveit.qrforward.domain.config;

import com.github.benmanes.caffeine.cache.Expiry;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;

import java.util.concurrent.TimeUnit;

public class PerEntryExpiry implements Expiry<Object, Object> {

    private static final long DEFAULT_TIMEOUT = TimeUnit.HOURS.toNanos(1);

    @Override
    public long expireAfterCreate(Object key, Object value, long currentTime) {
        if (value instanceof RedirectConfiguration configuration) {
            return redictConfigurationDuration(configuration);
        }
        return DEFAULT_TIMEOUT;
    }

    @Override
    public long expireAfterUpdate(Object key, Object value, long currentTime, long currentDuration) {
        if (value instanceof RedirectConfiguration configuration) {
            return redictConfigurationDuration(configuration);
        }
        return DEFAULT_TIMEOUT;
    }

    @Override
    public long expireAfterRead(Object key, Object value, long currentTime, long currentDuration) {
        if (value instanceof RedirectConfiguration configuration) {
            return redictConfigurationDuration(configuration);
        }
        return DEFAULT_TIMEOUT;
    }

    private long redictConfigurationDuration(RedirectConfiguration configuration) {
        return switch (configuration) {
            case SimpleRedirectConfiguration simpleConfig -> simpleConfigDuration();
        };
    }

    private long simpleConfigDuration() {
        return TimeUnit.HOURS.toNanos(1);
    }
}
