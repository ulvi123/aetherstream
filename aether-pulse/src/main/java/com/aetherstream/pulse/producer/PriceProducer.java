package com.aetherstream.pulse.producer;

import com.aetherstream.common.dto.PriceUpdate;
import com.aetherstream.pulse.service.ExchangeEmulator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class PriceProducer {

    private static final ExchangeEmulator emulator = new ExchangeEmulator();


    @Bean
    public Supplier<Flux<PriceUpdate>> pricesOut(ExchangeEmulator emulator){
        return () -> emulator.streamPrices()
                .doOnSubscribe(e -> System.out.println("Kafka binder successfully subscribed to Pulse stream"));
    }
}
