package com.fastx.ai.llm.platform.api;

import com.fastx.ai.llm.platform.dto.AppDTO;
import com.fastx.ai.llm.platform.dto.AppVersionDTO;

import java.util.List;

/**
 * @author stark
 */
public interface IPlatformAppService {

    /**
     * list app under user
     * @param userId user
     * @return app list
     */
    List<AppDTO> getAppList(Long userId);
    /**
     * create an llm application
     * @param app app
     * @return create app result dto
     */
    AppDTO createApp(AppDTO app);

    /**
     * update app info
     * @param appDTO app info
     * @return updated app
     */
    Boolean updateApp(AppDTO appDTO);

    /**
     * create a app version data
     * @param appVersionDTO version info
     * @return version
     */
    AppVersionDTO createNewAppVersion(AppVersionDTO appVersionDTO);

    /**
     * update version data
     * @param appVersionDTO version info
     * @return true or false
     */
    Boolean updateAppVersion(AppVersionDTO appVersionDTO);

    /**
     * bump a version online
     * @param appVersionDTO app version info
     * @return true or false
     */
    Boolean activateAppVersion(AppVersionDTO appVersionDTO);

    /**
     * offline activate a version online
     * @param appVersionDTO app version info
     * @return true or false
     */
    Boolean inactivateAppVersion(AppVersionDTO appVersionDTO);

    /**
     * delete a application
     * @param appId app
     * @return true or false
     */
    Boolean deleteApp(Long appId);

    /**
     * delete a version of app
     * @param appVersionId version id
     * @return true or false
     */
    Boolean deleteAppVersion(Long appVersionId);

    /**
     * get version list for a app
     * @param appId app id
     * @return version list
     */
    List<AppVersionDTO> getAppVersionList(Long appId);
}
