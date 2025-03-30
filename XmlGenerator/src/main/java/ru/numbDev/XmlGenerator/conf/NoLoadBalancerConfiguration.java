package ru.numbDev.XmlGenerator.conf;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

@Configuration
public class NoLoadBalancerConfiguration {

    @Bean
    ServiceInstanceListSupplier serviceInstanceListSupplier() {
        return new ServiceInstanceListSupplier() {
            @Override
            public String getServiceId() {
                return "no-load-balancer";
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                return Flux.just(Collections.emptyList());
            }
        };
    }
} 