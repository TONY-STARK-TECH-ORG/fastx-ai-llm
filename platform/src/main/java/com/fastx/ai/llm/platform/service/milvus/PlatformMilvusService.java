package com.fastx.ai.llm.platform.service.milvus;

import com.fastx.ai.llm.domains.api.IDubboMilvusService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author stark
 */
@DubboService
public class PlatformMilvusService {

    @DubboReference
    IDubboMilvusService milvusService;

}
