package org.multimediaprototype.ocs.base;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OCSBase")
public class OCSBaseImpl implements OCSBase {

    @Autowired
    private MemcachedClient memcachedClient;
       
    @Override
    public Object get(String key) {
        // TODO Auto-generated method stub
        return memcachedClient.get(key);
    }

    @Override
    public OperationFuture set(String key, int exp, Object o) {
        // TODO Auto-generated method stub
        return memcachedClient.set(key, exp, o);
    }

}
