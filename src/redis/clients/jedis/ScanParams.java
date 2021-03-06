package redis.clients.jedis;

import redis.clients.util.SafeEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static redis.clients.jedis.Protocol.Keyword.COUNT;
import static redis.clients.jedis.Protocol.Keyword.MATCH;

public class ScanParams {
    public final static String SCAN_POINTER_START = String.valueOf(0);
    public final static byte[] SCAN_POINTER_START_BINARY = SafeEncoder.encode(SCAN_POINTER_START);
    private List<byte[]> params = new ArrayList<byte[]>();

    public ScanParams match(final byte[] pattern) {
        params.add(MATCH.raw);
        params.add(pattern);
        return this;
    }

    public ScanParams match(final String pattern) {
        params.add(MATCH.raw);
        params.add(SafeEncoder.encode(pattern));
        return this;
    }

    public ScanParams count(final int count) {
        params.add(COUNT.raw);
        params.add(Protocol.toByteArray(count));
        return this;
    }

    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection(params);
    }
}
