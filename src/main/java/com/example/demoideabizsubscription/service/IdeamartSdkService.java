package com.example.demoideabizsubscription.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class IdeamartSdkService {

    @Value("${ideamart.base-url}")
    private String baseUrl;

    @Value("${ideamart.api-key}")
    private String apiKey;

    @Value("${ideamart.api-secret}")
    private String apiSecret;

    @Value("${ideamart.redirect-url}")
    private String redirectUrl;

    public URI buildSubscriptionAuthorizeUrl() {
        try {
            String requestId = String.valueOf(System.currentTimeMillis());
            String requestTime = ZonedDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));


            String dataToSign = apiKey + "|" + requestTime + "|" + apiSecret;
            String signature = sha512Hex(dataToSign);

            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/sdk/subscription/authorize")
                    .queryParam("apiKey", apiKey)
                    .queryParam("requestId", requestId)
                    .queryParam("requestTime", requestTime)
                    .queryParam("signature", signature)
                    .queryParam("redirectUrl", redirectUrl)
                    .build(true)
                    .toUri();

            System.out.println("====== Ideamart SDK Request ======");
            System.out.println("Base URL: " + baseUrl);
            System.out.println("Req Id: " + requestId);
            System.out.println("API Key: " + apiKey);
            System.out.println("Request Time: " + requestTime);
            System.out.println("Data to Sign: " + dataToSign);
            System.out.println("Generated Signature: " + signature);
            System.out.println("Redirect URL: " + redirectUrl);
            System.out.println("Final SDK URL: " + uri);
            System.out.println("==================================");

            return uri;

        } catch (Exception e) {
            throw new RuntimeException(" Failed to build Ideamart SDK URL", e);
        }
    }

    private String sha512Hex(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}