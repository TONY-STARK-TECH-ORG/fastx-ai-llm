package com.fastx.ai.llm.web.config;

import com.fastx.ai.llm.web.websocket.WebsocketIpStateListener;
import com.fastx.ai.llm.web.websocket.WebsocketMessageController;
import com.fastx.ai.llm.web.websocket.WebsocketServerListener;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.tio.server.TioServerConfig;
import org.tio.utils.time.Time;
import org.tio.websocket.server.WsServerStarter;

/**
 * @author stark
 */
@Configuration
public class WebsocketConfig {

    public static final String CHARSET = "utf-8";
    /**
     * null表示监听所有，并不指定ip
     */
    public static final String SERVER_IP = null;
    /**
     * 监听端口
     */
    public static final int SERVER_PORT = 3737;
    /**
     * 心跳超时时间，单位：毫秒
     */
    public static final int HEARTBEAT_TIMEOUT = 1000 * 60;
    /**
     * 协议名字(可以随便取，主要用于开发人员辨识)
     */
    public static final String PROTOCOL_NAME = "fastx";

    public interface IpStatDuration {
        Long DURATION_1 = Time.MINUTE_1 * 5;
        Long[] IPSTAT_DURATIONS = new Long[] { DURATION_1 };
    }

    private WsServerStarter wsServerStarter;
    private TioServerConfig serverTioConfig;

    @Autowired
    WebsocketIpStateListener ipStateListener;

    @Autowired
    WebsocketServerListener serverListener;

    @Autowired
    WebsocketMessageController messageController;

    @PostConstruct
    public void init() throws Exception {
        wsServerStarter = new WsServerStarter(SERVER_PORT, messageController);

        serverTioConfig = wsServerStarter.getTioServerConfig();
        serverTioConfig.setName(PROTOCOL_NAME);
        serverTioConfig.setTioServerListener(serverListener);
        serverTioConfig.setIpStatListener(ipStateListener);
        serverTioConfig.ipStats.addDurations(IpStatDuration.IPSTAT_DURATIONS);
        serverTioConfig.setHeartbeatTimeout(HEARTBEAT_TIMEOUT);

        // String keyStoreFile = "classpath:config/ssl/keystore.jks";
        // String trustStoreFile = "classpath:config/ssl/keystore.jks";
        // String keyStorePwd = "214323428310224";
        // @TODO (stark) : open websocket ssl support.
//        String keyStoreFile = P.get("ssl.keystore", null);
//        String trustStoreFile = P.get("ssl.truststore", null);
//        String keyStorePwd = P.get("ssl.pwd", null);
//        serverTioConfig.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);

        // stark websocket server.
        wsServerStarter.start();
    }

}
