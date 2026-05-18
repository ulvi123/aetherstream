package com.aetherstream.catalyst.consumer;
import com.aetherstream.catalyst.model.Arbitrage;
import com.aetherstream.catalyst.repository.ArbitrageRepository;
import com.aetherstream.common.dto.PriceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;
import java.util.function.Consumer;

@Configuration
public class PriceConsumer {

    private final ArbitrageRepository arbitrageRepository;

    private final Logger logger = LoggerFactory.getLogger(PriceConsumer.class);

    public PriceConsumer(ArbitrageRepository arbitrageRepository) {
        this.arbitrageRepository = arbitrageRepository;
    }
    @Bean
    public Consumer<Flux<PriceUpdate>> pricesIn(){
        return flux -> flux
                .buffer(Duration.ofMillis(100))
                .filter(prices -> !prices.isEmpty())
                .flatMap(prices -> {
                    PriceUpdate lowest = prices.stream()
                            .min(Comparator.comparingDouble(PriceUpdate::price))
                            .orElseThrow(IllegalStateException::new);
                    PriceUpdate highest  = prices.stream()
                            .max(Comparator.comparingDouble(PriceUpdate::price))
                            .orElseThrow(IllegalStateException::new);

                    BigDecimal buyPrice = new BigDecimal(lowest.price());
                    BigDecimal sellPrice = new BigDecimal(highest.price());
                    BigDecimal grossSpread = sellPrice.subtract(buyPrice);

                    if(grossSpread.compareTo(BigDecimal.ZERO) > 0){
                        Arbitrage arbitrageOpportunity = new Arbitrage(
                                null,
                                "BTC/USDT",
                                buyPrice,
                                lowest.exchange(),
                                sellPrice,
                                highest.exchange(),
                                grossSpread,
                                null
                        );

                        return arbitrageRepository.save(arbitrageOpportunity)
                                .doOnSuccess(saved -> logger.info("💰 Saved Arbitrage: +${} ({} -> {})", saved.grossSpread(), saved.buyExchange(),saved.sellExchange())  )
                                .onErrorResume(e->{
                                    logger.error("Error while saving arbitrage", e);
                                    return Mono.empty();
                                });
                    }

                    return Mono.empty();
                })
                .doOnError(e -> logger.error("Error processing price updates", e))
                .subscribe();
    }
}
