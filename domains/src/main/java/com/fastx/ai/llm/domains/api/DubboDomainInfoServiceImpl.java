package com.fastx.ai.llm.domains.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author stark
 */
@DubboService
public class DubboDomainInfoServiceImpl extends DubboBaseDomainService implements IDubboDomainInfoService {

    @Override
    @SentinelResource("domain.info")
    public String getBasicInfo() {
        return "0.0.1, contains all domains now.";
    }

}
