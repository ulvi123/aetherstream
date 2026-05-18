package com.aetherstream.pulse.service;

import com.aetherstream.common.dto.PriceUpdate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExchangeEmulator {

    private final Flux<PriceUpdate> priceUpdateFlux;
    private final List<String> EXCHANGE_NAMES = List.of("Binance", "Coinbase", "Kraken");
    private double lastBasePrice = 65000;

    public ExchangeEmulator(Flux<PriceUpdate> priceUpdateFlux) {
        this.priceUpdateFlux = priceUpdateFlux;
    }


    public ExchangeEmulator() {
        this.priceUpdateFlux = Flux.interval(Duration.ofMillis(500))
                .flatMap(tick -> Flux.fromIterable(EXCHANGE_NAMES)
                        .map(exchangeName -> new PriceUpdate(
                                exchangeName,
                                "BTC/USDT",
                                generateRandomPrice(exchangeName),
                                System.currentTimeMillis()
                        ))
                )
                .share();
    }

    public double generateRandomPrice(String exchangeName){
        lastBasePrice += (ThreadLocalRandom.current().nextDouble() - 0.5) * 10;
        return switch (exchangeName){
            case "Binance" -> lastBasePrice + 0.50;
            case "Coinbase" -> lastBasePrice + 2.10;
            case "Kraken" -> lastBasePrice - 1.25;
            default -> lastBasePrice;
        };
    }


    public Flux<PriceUpdate> streamPrices(){
        return  priceUpdateFlux;
    }


}
