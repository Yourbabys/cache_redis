package com.qfedu.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qfedu.bean.Dict;
import com.qfedu.service.DictService;
import com.qfedu.mapper.DictMapper;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author adminor
* @description 针对表【dict】的数据库操作Service实现
* @createDate 2022-11-07 19:39:55
*/
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
    implements DictService{

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RBloomFilter rBloomFilter;

    /*@Override
    public List<Dict> getAllDict() {
        //先从redis中查找，redis中找不到，再从数据库查找
        BoundValueOperations<String, String> dicts = stringRedisTemplate.boundValueOps("dicts1");
        if(dicts.get()!=null && !dicts.get().equals("")){
            System.out.println("redis中查找");
            String s = dicts.get();
            List<Dict> dicts1 = JSONUtil.toList(s, Dict.class);
            return dicts1;
        }else{
            System.out.println("db中查找");
            List<Dict> dicts1 = this.baseMapper.selectList(null);
            dicts.set(JSONUtil.toJsonStr(dicts1));
            return dicts1;
        }
    }*/
    @Override
    public List<Dict> getAllDict() {
        //先从redis中查找，redis中找不到，再从数据库查找
        BoundValueOperations<String, String> dicts = stringRedisTemplate.boundValueOps("dicts11");
        if(StringUtils.isEmpty(dicts.get())){
            //加锁，单例运行，
            synchronized (this){
                //如果不在redis中，去查找数据库，查找到的放入redis
                if(StringUtils.isEmpty(dicts.get())){
                    System.out.println("db中查找");
                    List<Dict> dicts1 = this.baseMapper.selectList(null);
                    dicts.set(JSONUtil.toJsonStr(dicts1));
                    return dicts1;
                }else{
                    //其后访问则再redis中
                    System.out.println("redis中查找");
                    String s = dicts.get();
                    List<Dict> dicts1 = JSONUtil.toList(s, Dict.class);
                    return dicts1;
                }
            }
        }else{
            System.out.println("redis中查找");
            String s = dicts.get();
            List<Dict> dicts1 = JSONUtil.toList(s, Dict.class);
            return dicts1;
        }
    }


    @Override
    public String getCode(String code) {
        //先从redis中查找，redis中找不到，再从数据库查找
        BoundValueOperations<String, String> dicts = stringRedisTemplate.boundValueOps(code);
        if(StringUtils.isEmpty(dicts.get())) {
            if (rBloomFilter.contains(code)) {
                System.out.println("查找数据库");
                QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
                dictQueryWrapper.eq("dictcode", code);
                Dict dict = this.baseMapper.selectOne(dictQueryWrapper);

                if(dict!=null){
                    dicts.set(dict.getDictvalue());
                    return dict.getDictvalue();
                }
            }
                System.out.println("布隆过滤器工作了");
                return null;
            }else{
            System.out.println("redis查找");
            String s = dicts.get();
            return s;
        }

    }
}




