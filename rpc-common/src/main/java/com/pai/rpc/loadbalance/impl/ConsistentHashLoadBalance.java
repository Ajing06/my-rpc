package com.pai.rpc.loadbalance.impl;

import com.pai.rpc.entity.RpcRequest;
import com.pai.rpc.loadbalance.AbstractLoadBalance;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConsistentLoadBalance extends AbstractLoadBalance {

    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getInterfaceName() + rpcRequest.getServiceVersion();
        int identityHashCode = System.identityHashCode(serviceUrlList);
        ConsistentHashSelector selector = selectors.get(serviceName);
        if(selector == null || selector.identityHashCode != identityHashCode) {
            selectors.put(serviceName, new ConsistentHashSelector(serviceUrlList, identityHashCode, 160));
            selector = selectors.get(serviceName);
        }
        return selector.select(serviceName + Arrays.stream(rpcRequest.getParameters()));
    }

    /**
     * Consistent Hash Selector
     * 一致性哈希函数，详解：https://www.jianshu.com/p/528ce5cd7e8f
     */
    static class ConsistentHashSelector {

        private final int identityHashCode;
        private final TreeMap<Long, String> virtualInvokers;

        public ConsistentHashSelector(List<String> serviceUrlList, int identityHashCode, int replicaNumber) {
            this.identityHashCode = identityHashCode;
            this.virtualInvokers = new TreeMap<>();

            for (String invoker : serviceUrlList) {
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(invoker + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        static byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            return md.digest();
        }

        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }

        public String select(String rpcServiceKey) {
            byte[] digest = md5(rpcServiceKey);
            return selectForKey(hash(digest, 0));
        }

        public String selectForKey(long hashCode) {
            Map.Entry<Long, String> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();

            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }

            return entry.getValue();
        }

    }
}
