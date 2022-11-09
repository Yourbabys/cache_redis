package com.qfedu.controller;

import com.qfedu.bean.Dict;
import com.qfedu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictController {
    @Autowired
    private DictService dictService;

    @GetMapping("getAllDict")
    public List<Dict> getAllDict(){
        List<Dict> allDict = dictService.getAllDict();
        return allDict;
    }
    @GetMapping("getOneDict")
    public String getOneDict(String code){
        String code1 = dictService.getCode(code);
        return code1;
    }

}
