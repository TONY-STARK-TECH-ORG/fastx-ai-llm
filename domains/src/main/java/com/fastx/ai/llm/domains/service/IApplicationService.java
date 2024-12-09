package com.fastx.ai.llm.domains.service;

import com.fastx.ai.llm.domains.entity.Application;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IApplicationService extends IService<Application> {

    /**
     * find by org ids
     * @param orgIds org that user joined
     * @return app under org
     */
    List<Application> findByOrgIds(List<Long> orgIds);

}
