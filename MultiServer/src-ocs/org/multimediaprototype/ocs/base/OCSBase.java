package org.multimediaprototype.ocs.base;

import net.spy.memcached.internal.OperationFuture;

public interface OCSBase {
    Object get(String key);
    OperationFuture set(String key, int exp, Object o);
}
