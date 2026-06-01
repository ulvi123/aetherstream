package com.aetherstream.catalyst.service;


import com.aetherstream.catalyst.model.Arbitrage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class ArbitrageBroadcastingService {
    private final Sinks.Many<Arbitrage> arbitrageSink = Sinks.many().multicast().onBackpressureBuffer();

    public void broadcast(Arbitrage arbitrage) {
        arbitrageSink.emitNext(arbitrage, (signalType, emitResult) -> {
            return emitResult == Sinks.EmitResult.FAIL_NON_SERIALIZED;
        });
    }

    public Flux<Arbitrage> getArbitrages() {
        return  arbitrageSink.asFlux();
    }


}
