package net.vokhmin.ba.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BitcoinAverageManualExample {

    private static final String TICKER_URI = "/websocket/multiple/ticker?ticket=$TICKET&public_key=$PUB_KEY";

    public static String getWsUrl(String host, String ticket, String key) {
        return "wss://" + host + TICKER_URI.replace("$TICKET", ticket).replace("$PUB_KEY", key);
    }

    @Bean
    private BitcoinAverageAuthV1 getAuthenithicator() {
        return new BitcoinAverageAuthV1(config);
    }

    @Autowired
    private BitcoinAverageConfig config;

    public void run(String... args) throws Exception {
        log.info("EXECUTING : command line runner");

        final String ticket = getAuthenithicator().auth(config.getSecretKey());
        final WebSocketClient client = new WebSocketClient();
        client.run(getWsUrl(config.getHost(), ticket, config.getPublicKey()));
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(
                        BitcoinAverageManualExample.class,
                        BitcoinAverageConfig.class);
        BitcoinAverageManualExample main = ctx.getBean(BitcoinAverageManualExample.class);
        main.run();
    }

}
