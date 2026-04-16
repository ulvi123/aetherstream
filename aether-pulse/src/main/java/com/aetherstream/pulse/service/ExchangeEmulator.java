package com.aetherstream.pulse.service;

import com.aetherstream.common.dto.PriceUpdate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExchangeEmulator {

    private final Flux<PriceUpdate> priceUpdateFlux;

    public ExchangeEmulator(Flux<PriceUpdate> priceUpdateFlux) {
        this.priceUpdateFlux = priceUpdateFlux;
    }


    public ExchangeEmulator() {
        this.priceUpdateFlux = Flux.interval(Duration.ofMillis(500))
                .map(i -> generateRandomPrice())
                .publish()
                .refCount();
    }


    public Flux<PriceUpdate> streamPrices(){
        return  priceUpdateFlux;
    }

    public PriceUpdate generateRandomPrice(){
      var price = ThreadLocalRandom.current().nextDouble(60000,66000);
      return new com.aetherstream.common.dto.PriceUpdate(
              "BTC/USDT",
              BigDecimal.valueOf(price),
              "Binance",
              Instant.now()
              );
    }
}
