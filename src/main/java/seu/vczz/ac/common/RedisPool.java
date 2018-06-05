package seu.vczz.ac.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * CREATE by vczz on 2018/6/5
 * redis 连接池工具
 */
@Service("redisPool")
@Slf4j
public class RedisPool {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    /**
     * 获取一个连接
     * @return
     */
    public ShardedJedis instance(){
        return shardedJedisPool.getResource();
    }

    /**
     * 关闭一个连接
     * @param shardedJedis
     */
    public void safeClose(ShardedJedis shardedJedis){
        try {
            if (shardedJedis != null){
                shardedJedis.close();
            }
        }catch (Exception e){
            log.error("return redis resource exception {}", e);
        }
    }


}
