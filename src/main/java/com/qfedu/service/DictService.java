package com.qfedu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qfedu.bean.Dict;

import java.util.List;

/**
* @author adminor
* @description 针对表【dict】的数据库操作Service
* @createDate 2022-11-07 19:39:55
*/
public interface DictService extends IService<Dict> {
    List<Dict> getAllDict();
    String getCode(String code);

}
