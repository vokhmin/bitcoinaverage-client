package net.vokhmin.ba.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "application.properties",
        "private.properties"
})
public class BitcoinAverageConfig {

    public static final String HOST = "apiv2.bitcoinaverage.com";

    @Value("${host}")
    private String host;

    @Value("${public.key}")
    private String publicKey;

    // definition of the property is expected in /src/main/resources/private.properties
    @Value("${secret.key}")
    private String secretKey;

    public String getPublicKey() {
        return publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getHost() {
        return host;
    }
}
