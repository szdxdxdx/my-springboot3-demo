package team.project.util.expiringmap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 带有自动过期功能的键值对容器
 * <p> 每个键值对在放入容器时都会被赋予一个有效时间，过了有效时间的值视为无效
 * <p> 底层使用 ConcurrentHashMap 容器，保证线程安全
 * */
public class ExpiringMap<K, V> {

    /**
     * 创建一个空的键值对容器，并设定键值对的有效时间
     * @param liveTimeMillis 将键值对放入容器时赋予的有效时间，单位毫秒
     * */
    public ExpiringMap(long liveTimeMillis) {
        this.liveTimeMillis        = liveTimeMillis;
        this.cleanupIntervalMillis = liveTimeMillis + liveTimeMillis / 2;
        this.cleanupTime = System.currentTimeMillis() + cleanupIntervalMillis;
    }

    /**
     * 添加一个新的键值对（如果键已存在则更新值，并延长有效时间）
     * */
    public void put(K key, V value) {
        lazyCleanUpExpired();

        Item<V> item = new Item<>();
        item.value = value;
        item.expireTime = System.currentTimeMillis() + liveTimeMillis;

        map.put(key, item);
    }

    /**
     * 获取指定键对应的值（不延长有效时间），如果键不存在或已经过期则返回 null
     * */
    public V get(K key) {
        lazyCleanUpExpired();

        Item<V> item = map.get(key);

        if (item == null) {
            return null;
        }
        else if (item.isExpired()) {
            map.remove(key);
            return null;
        }
        else {
            return item.value;
        }
    }

    /**
     * 获取指定键对应键值对的剩余有效时间，如果键不存在或已经过期则返回 0
     * */
    public long getRemainingTime(K key) {
        lazyCleanUpExpired();

        Item<V> item = map.get(key);
        if (item == null) {
            return 0;
        }
        else if (item.isExpired()) {
            map.remove(key);
            return 0;
        }
        else {
            return item.expireTime - System.currentTimeMillis();
        }
    }

    /**
     * 移除指定键对应的键值对（不需要提前判断键存在）
     * */
    public void remove(K key) {
        lazyCleanUpExpired();
        map.remove(key);
    }

    /* --------- */

    private static class Item<V> {
        public V    value;
        public long expireTime;

        public boolean isExpired() { return expireTime < System.currentTimeMillis(); }
    }

    /* 每个键值对的有效时间 */
    private final long liveTimeMillis;

    /* 清理过期键值对的时间间隔 */
    private final long cleanupIntervalMillis;

    /* 清理时间 */
    private volatile long cleanupTime;

    /* 2024-05-19 szdxdxdx
       ---------
       懒清理策略：只有某个方法被调用时（如 put、get 等），且距离上次清理已经过去了一段时间，才执行清理操作
     */

    /* ConcurrentHashMap 确保线程安全 */
    private final Map<K, Item<V>> map = new ConcurrentHashMap<>();

    /**
     * 清理所有已过期的键值对（懒清理策略）
     * */
    private void lazyCleanUpExpired() {
        long currentTime = System.currentTimeMillis();

        if (currentTime <= cleanupTime) {
            return;
        }

        map.entrySet().removeIf(entry -> entry.getValue().isExpired());
        cleanupTime = currentTime + cleanupIntervalMillis;
    }
}
