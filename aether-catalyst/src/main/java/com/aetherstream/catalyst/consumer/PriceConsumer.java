package com.aetherstream.catalyst.consumer;
import com.aetherstream.common.dto.PriceUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class PriceConsumer {
    @Bean
    public Consumer<Flux<PriceUpdate>> pricesIn(){
        return flux ->
                flux
                        .doOnNext(price -> System.out.println("Main brain revceived: " +  price.price()))
                        .doOnError(e -> System.out.println("Error in stream: " + e.getMessage()))
                        .subscribe();
    }
}
