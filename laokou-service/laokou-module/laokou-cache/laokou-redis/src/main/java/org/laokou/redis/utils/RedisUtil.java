/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.redis.utils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
/**
 * Redis工具类
 * @author  Kou Shenhai
 */
@Component
public final class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RBloomFilter<String> bloomFilter;

    /**  默认过期时长为24小时，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;

    /**  过期时长为1小时，单位：秒 */
    public final static long HOUR_ONE_EXPIRE = 60 * 60 * 1L;

    /**  过期时长为6小时，单位：秒 */
    public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6L;

    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1L;

    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    public RLock getFairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    public RLock getReadLock(String key) {
        return redissonClient.getReadWriteLock(key).readLock();
    }

    public RLock getWriteLock(String key) {
        return redissonClient.getReadWriteLock(key).writeLock();
    }

    public Boolean tryLock(RLock lock, long expire, long timeout) throws InterruptedException {
        return lock.tryLock(timeout, expire, TimeUnit.MILLISECONDS);
    }

    public Boolean tryLock(String key, long expire, long timeout) throws InterruptedException {
        return tryLock(getLock(key),expire,timeout);
    }

    public void unlock(String key) {
        unlock(getLock(key));
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

    public void lock(String key) {
        lock(getLock(key));
    }

    public void lock(RLock lock) {
        lock.lock();
    }

    public Boolean isLocked(String key) {
        return isLocked(getLock(key));
    }

    public Boolean isLocked(RLock lock) {
        return lock.isLocked();
    }

    public Boolean isHeldByCurrentThread(String key) {
        return isHeldByCurrentThread(getLock(key));
    }

    public Boolean isHeldByCurrentThread(RLock lock) {
        return lock.isHeldByCurrentThread();
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public void set(String key, Object value, long expire){
        bloomFilter.add(key);
        bloomFilter.expire(Duration.ofSeconds(expire));
        redissonClient.getBucket(key).set(value,expire, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        if (!bloomFilter.contains(key)) {
            return null;
        }
        return redissonClient.getBucket(key).get();
    }

    public void delete(String key) {
        redissonClient.getKeys().delete(key);
    }

    public void hSet(String key,String field, Object value,long expire) {
        RMap<Object, Object> map = redissonClient.getMap(key);
        map.put(field,value);
        map.expire(Duration.ofSeconds(expire));
    }

    public void hSet(String key,String field, Object value) {
        hSet(key, field, value,NOT_EXPIRE);
    }

    public Object hGet(String key,String field) {
        return redissonClient.getMap(key).get(field);
    }

    public Long getKeysSize() {
        final Object obj = redisTemplate.execute((RedisCallback) connection -> connection.dbSize());
        return obj == null ? 0 : Long.valueOf(obj.toString());
    }

    public List<Map<String, String>> getCommandStatus() {
        final Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        return pieList;
    }

    public Map<String, String> getInfo() {
        final Properties properties = (Properties) redisTemplate.execute((RedisCallback) connection -> connection.info());
        final Set<String> set = properties.stringPropertyNames();
        final Iterator<String> iterator = set.iterator();
        Map<String,String> dataMap = new HashMap<>(set.size());
        while (iterator.hasNext()) {
            final String key = iterator.next();
            final String value = properties.getProperty(key);
            dataMap.put(key, value);
        }
        return dataMap;
    }

}
