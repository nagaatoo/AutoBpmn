package ru.numbDev.XmlGenerator.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import ru.numbDev.XmlGenerator.conf.FeignConfig;
import ru.numbDev.XmlGenerator.model.AccessToken;

@FeignClient(name = "gigachat-auth-client", url = "https://ngw.devices.sberbank.ru:9443", configuration = FeignConfig.class)
public interface GigaChatAuthFeign {

        /**
     * Receive an access token for a given authorization token
     */
    @PostMapping(path = "/api/v2/oauth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AccessToken> getAccessToken( 
            @RequestHeader("Authorization") String auth,
            @RequestHeader("RqUID") String userId,
            Map<String, ?> formParams);

}
