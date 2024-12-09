package com.fastx.ai.llm.platform.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fastx.ai.llm.domains.api.IDubboDomainInfoService;
import com.fastx.ai.llm.platform.api.IPlatformBasicInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author stark
 */
@DubboService
public class PlatformBasicInfoServiceImpl implements IPlatformBasicInfoService {

    @DubboReference
    IDubboDomainInfoService domainInfoService;


    @Override
    @SentinelResource("platform.info")
    public String getBasicInfo() {
        return "platform: 0.0.1, domain: " + domainInfoService.getBasicInfo();
    }
}
