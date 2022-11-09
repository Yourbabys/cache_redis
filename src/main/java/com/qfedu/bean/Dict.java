package com.qfedu.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName dict
 */
@Data
public class Dict implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String dictcode;

    /**
     * 
     */
    private String dictdesc;

    /**
     * 
     */
    private String dictvalue;

    private static final long serialVersionUID = 1L;
}