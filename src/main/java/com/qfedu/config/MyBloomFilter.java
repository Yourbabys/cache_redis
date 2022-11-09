package com.qfedu.config;

import com.qfedu.bean.Dict;
import com.qfedu.mapper.DictMapper;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MyBloomFilter {
    @Autowired
    DictMapper dictMapper;

    @Bean
    public RBloomFilter getRedissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.86.100:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RBloomFilter<Object> bloomDicts = redissonClient.getBloomFilter("dicts");
        bloomDicts.tryInit(1000000L, 0.03);
        List<Dict> dicts = dictMapper.selectList(null);
        dicts.forEach(dict -> {
            bloomDicts.add(dict.getDictcode());
        });
        return bloomDicts;
    }
}
