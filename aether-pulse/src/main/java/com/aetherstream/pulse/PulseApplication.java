package com.aetherstream.pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aetherstream.pulse.service.ExchangeEmulator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;import java.time.Duration;


@SpringBootApplication
public class PulseApplication {
    public static void main(String[] args) {
        SpringApplication.run(PulseApplication.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(PulseApplication.class);

    @Bean
    public CommandLineRunner runDiscovery(ExchangeEmulator emulator) {
        return args -> {
            // Internal Auditor subscription
            emulator.streamPrices().subscribe(price ->
                    log.info("[Auditor] Mapped price: {}", price.price()));

            // Simulate the Analytics engine joining 2.5 seconds later
            Flux.just(1)
                    .delayElements(Duration.ofMillis(2500))
                    .subscribe(i -> {
                        emulator.streamPrices().subscribe(price ->
                                log.info("[Analytics] Processing SAME price: {}", price.price()));
                    });
        };
    }
}