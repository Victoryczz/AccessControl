package seu.vczz.ac.service.impl;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import seu.vczz.ac.common.CacheKeyConst;
import seu.vczz.ac.common.RedisPool;
import seu.vczz.ac.service.ISysCacheService;
import seu.vczz.ac.util.JsonUtil;
/**
 * CREATE by vczz on 2018/6/5
 */
@Service("iSysCacheService")
@Slf4j
public class SysCacheServiceImpl implements ISysCacheService {

    @Autowired
    private RedisPool redisPool;

    /**
     * 设置缓存
     * @param prefix
     * @param value
     * @param expireTime
     */
    public void saveCache(CacheKeyConst prefix, String value, int expireTime){
        saveCache(prefix, value, expireTime, null);
    }

    /**
     * 设置缓存
     * @param prefix
     * @param value
     * @param expireTime
     * @param keys
     */
    public void saveCache(CacheKeyConst prefix, String value, int expireTime, String... keys){
        if (value == null){
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis.setex(cacheKey, expireTime, value);
        }catch (Exception e){
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtil.obj2String(keys), e);
        }finally {
            //回收
            redisPool.safeClose(shardedJedis);
        }
    }
    /**
     * 从缓存中获取数据
     * @param prefix
     * @param keys
     * @return
     */
    public String getCache(CacheKeyConst prefix, String... keys){
        if (keys == null){
            return null;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            String cacheKey = generateCacheKey(prefix, keys);
            return shardedJedis.get(cacheKey);
        }catch (Exception e){
            log.error("get cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtil.obj2String(keys), e);
            return null;
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    //前缀产生
    private String generateCacheKey(CacheKeyConst prefix, String... keys){
        String key = prefix.name();
        if (keys != null && keys.length > 0){
            key += "_"+ Joiner.on("_").join(keys);
        }
        return key;
    }


}
