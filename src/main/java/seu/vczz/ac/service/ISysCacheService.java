package seu.vczz.ac.service;

import seu.vczz.ac.common.CacheKeyConst;

/**
 * CREATE by vczz on 2018/6/5
 */
public interface ISysCacheService {
    /**
     * 设置缓存
     * @param prefix
     * @param value
     * @param expireTime
     */
    void saveCache(CacheKeyConst prefix, String value, int expireTime);
    /**
     * 设置缓存
     * @param prefix
     * @param value
     * @param expireTime
     * @param keys
     */
    void saveCache(CacheKeyConst prefix, String value, int expireTime, String... keys);
    /**
     * 从缓存中获取数据
     * @param prefix
     * @param keys
     * @return
     */
    String getCache(CacheKeyConst prefix, String... keys);

}
