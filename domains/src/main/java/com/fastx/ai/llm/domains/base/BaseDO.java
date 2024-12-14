package com.fastx.ai.llm.domains.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @author stark
 */
@Data
public class BaseDO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String createTime;

    private String updateTime;

    @TableLogic
    private Integer deleted;
}
