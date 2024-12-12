package com.fastx.ai.llm.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsTioServerListener;

/**
 * @author stark
 */
@Component
public class WebsocketServerListener extends WsTioServerListener {

    private Logger log = LoggerFactory.getLogger(WebsocketServerListener.class);

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        super.onBeforeClose(channelContext, throwable, remark, isRemove);
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();
        if (wsSessionContext != null && wsSessionContext.isHandshaked()) {
            int count = Tio.getAll(channelContext.tioConfig).getObj().size();
            log.info("{}", channelContext.getClientNode().toString() + " 离开了，现在共有【" + count + "】人在线");
        }
    }
}
