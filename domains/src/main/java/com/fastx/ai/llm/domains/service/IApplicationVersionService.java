package com.fastx.ai.llm.domains.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fastx.ai.llm.domains.entity.ApplicationVersion;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IApplicationVersionService extends IService<ApplicationVersion> {

    /**
     * find all versions under app
     * @param appId app id
     * @return all version records
     */
    List<ApplicationVersion> findByAppId(Long appId);

    /**
     * active version
     * @param appVersionId version record id
     * @return update result
     */
    boolean activeVersion(Long appVersionId);

    /**
     * inactive version
     * @param appVersionId version record id
     * @return update result
     */
    boolean inactiveVersion(Long appVersionId);

    /**
     * create new version for application
     * @param applicationId application id
     * @param versionData version data
     * @return create result
     */
    ApplicationVersion createVersion(Long applicationId, String versionData, String version);

    /**
     * update version data to version
     * @param applicationVersionId application version record id
     * @param versionData version json string data
     * @return full object
     */
    boolean updateVersionData(Long applicationVersionId, String versionData);
}
