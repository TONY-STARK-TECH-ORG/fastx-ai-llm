package com.fastx.ai.llm.domains.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fastx.ai.llm.domains.entity.Organization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    /**
     * find by user id
     * @param userId user id
     * @return return a list
     */
    List<Organization> findByUserId(@Param("userId") Long userId);
}
