package net.vokhmin.ba.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BitcoinAverageV2ManualExample {

    private static final String TICKER_URI = "/websocket/v2/ticker?ticket=$TICKET&public_key=$PUB_KEY";

    public static String getWsUrl(String host, String ticket, String key) {
        return "wss://" + host + TICKER_URI.replace("$TICKET", ticket).replace("$PUB_KEY", key);
    }

    @Bean
    private BitcoinAverageAuthV2 getAuthenithicatorV2() {
        return new BitcoinAverageAuthV2(config);
    }

    @Autowired
    private BitcoinAverageConfig config;

    public void run(String... args) throws Exception {
        log.info("EXECUTING : command line runner");

        final String ticket = getAuthenithicatorV2().auth(config.getSecretKey());
        final WebSocketClient client = new WebSocketClient();
        client.run(getWsUrl(config.getHost(), ticket, config.getPublicKey()));
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(
                        BitcoinAverageV2ManualExample.class,
                        BitcoinAverageConfig.class);
        BitcoinAverageV2ManualExample main = ctx.getBean(BitcoinAverageV2ManualExample.class);
        main.run();
    }

}
