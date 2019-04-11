package net.vokhmin.ba.client;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "application.properties",
        "private.properties"
})
public class BitcoinAverageConfig {

    public static final String SIGNATURE_HEADER = "X-signature";
    public static final String HOST = "apiv2.bitcoinaverage.com";

    static final String GET_TICKET_URI = "/websocket/get_ticket";
    static final String GET_TICKET_V2_URI = "/websocket/v2/get_ticket";
    static final String TICKER_URI = "/websocket/ticker?ticket=$TICKET&public_key=$PUB_KEY";
    static final String TICKER_V2_URI = "/websocket/v2/get_ticket?ticket=$TICKET&public_key=$PUB_KEY";

    @Value("${host}")
    private String host;

    @Value("${public.key}")
    private String publicKey;

    @Value("${secret.key}")
    private String secretKey;

    public static String getTicketUri() {
        return GET_TICKET_URI;
    }

    public static String getTicketV2Uri() {
        return GET_TICKET_V2_URI;
    }

    static String getSignature(String secretKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException {

        long timestamp = System.currentTimeMillis() / 1000L;
        String payload = timestamp + "." + publicKey;

        Mac sha256_Mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_Mac.init(secretKeySpec);
        String hashHex = DatatypeConverter
                .printHexBinary(sha256_Mac.doFinal(payload.getBytes()))
                .toLowerCase();
        String signature = payload + "." + hashHex;
        return signature;
    }

    public String getSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        return getSignature(secretKey, publicKey);
    }

//    public String getSignature() {
//        return "$UNIX_EPOCH.$PUB_KEY"
//                .replace("$UNIX_EPOCH", "" + getUnixEpoch())
//                .replace("$PUB_KEY", getPublicKey());
//    }

    private static int getUnixEpoch() {
        return (int) (System.currentTimeMillis() / 1000L);
    }


    public static String getBaseUrl(String host) {
        return "https://" + host;
    }

    public String getBaseUrl() {
        return getBaseUrl(this.getHost());
    }

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
