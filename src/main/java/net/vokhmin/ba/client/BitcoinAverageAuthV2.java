package net.vokhmin.ba.client;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BitcoinAverageAuthV2 {

    static final String GET_TICKET_V2_URI = "/websocket/v2/get_ticket";
    static final String SIGNATURE_HEADER = "X-signature";

    private BitcoinAverageConfig bitcoinAverage;

    @Autowired
    public BitcoinAverageAuthV2(BitcoinAverageConfig bitcoinAverage) {
        this.bitcoinAverage = bitcoinAverage;
    }

    public String auth(String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://" + bitcoinAverage.getHost())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(SIGNATURE_HEADER, getSignature(secretKey, bitcoinAverage.getPublicKey()))
                .build();
        WebClient.RequestBodySpec request =
                client.method(GET).uri(GET_TICKET_V2_URI);
        final BATicketRes res =
                request.contentType(APPLICATION_JSON_UTF8)
                        .retrieve()
                        .bodyToMono(BATicketRes.class)
                        .block(Duration.ofSeconds(10));
        log.info("The ticket has been got from the http-response is: '{}'", res.getTicket());
        return res.getTicket();
    }

    public String getSignature(String secretKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException {
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

}
