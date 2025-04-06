package ru.numbDev.XmlGenerator.conf;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

@Configuration
@LoadBalancerClients(defaultConfiguration = NoLoadBalancerConfiguration.class)
public class FeignConfig {
    
    private static final Logger log = LoggerFactory.getLogger(FeignConfig.class);

    @Bean
    public Client feignClient() {
        log.warn("Creating insecure Feign client. DO NOT USE IN PRODUCTION!");
        return new Client.Default(createSSLSocketFactory(), (hostname, session) -> true);
    }

    private SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new TrustAllManager()}, null);
            return ctx.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Error creating SSL context", e);
        }
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
} 