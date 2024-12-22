package com.fastx.ai.llm.web.websocket;

import cn.dev33.satoken.stp.StpUtil;
import com.fastx.ai.llm.platform.api.IPlatformUserService;
import com.fastx.ai.llm.web.config.WebsocketConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Objects;

/**
 * @author stark
 */
@Component
public class WebsocketMessageController implements IWsMsgHandler {

    private Logger log = LoggerFactory.getLogger(WebsocketMessageController.class);

    @DubboReference
    private IPlatformUserService platformUserService;

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        // String clientIp = httpRequest.getClientIp();
        String userId = httpRequest.getParam("user_id");
        if (StringUtils.isEmpty(userId)) {
            Tio.close(channelContext, "token not validated!");
            return httpResponse;
        }
        // Validate token and set authentication
        if (StpUtil.isLogin(userId)) {
            // auth success ! bind userId to this socket channel context.
            Tio.bindUser(channelContext, String.valueOf(StpUtil.getLoginId()));
        } else {
            // auth failed !
            Tio.close(channelContext, "token not validated!");
        }
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        WsResponse wsResponse = WsResponse.fromText("welcome to fastx-ai rag system!", WebsocketConfig.CHARSET);
        // send a welcome message to user!
        Tio.send(channelContext, wsResponse);
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext, "");
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        if (Objects.equals("ping", text)) {
            Tio.send(channelContext, WsResponse.fromText("pong", WebsocketConfig.CHARSET));
            return null;
        }

        // receive message from client
        log.info("receive: {}, from user: {}", text, channelContext.userid);
        return null;
    }

}
