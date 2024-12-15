package com.fastx.ai.llm.domains.api;

import com.fastx.ai.llm.domains.dto.ApplicationDTO;
import com.fastx.ai.llm.domains.dto.ApplicationVersionDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
public interface IDubboApplicationService {

    /**
     * create application
     * @param applicationDTO
     * @return
     */
    ApplicationDTO createApplication(ApplicationDTO applicationDTO);

    /**
     * get all application under org id
     * @param orgId
     * @return
     */
    List<ApplicationDTO> getApplications(Long orgId);

    /**
     * update a application
     * @param applicationDTO
     * @return
     */
    boolean updateApplication(ApplicationDTO applicationDTO);

    /**
     * delete a application, will remove all versions in application
     * @param applicationId
     * @return
     */
    boolean deleteApplication(Long applicationId);

    /**
     * get version for application
     * @param applicationId
     * @return
     */
    List<ApplicationVersionDTO> getApplicationVersions(Long applicationId);

    /**
     * create a new version for application
     * @param applicationId
     * @param versionData
     * @return
     */
    ApplicationVersionDTO createVersionData(Long applicationId, String versionData, String version);

    /**
     * update version data
     * @param applicationVersionId
     * @param versionData
     * @return
     */
    boolean updateVersionData(Long applicationVersionId, String versionData);

    /**
     * active a version and deactivate other version
     * @param applicationVersionId
     * @return
     */
    boolean activeVersion(Long applicationVersionId);

    /**
     * in active a version
     * @param applicationVersionId
     * @return
     */
    boolean inactiveVersion(Long applicationVersionId);

    /**
     * delete application version
     * @param applicationVersionId
     * @return
     */
    boolean deleteVersion(Long applicationVersionId);

}
