package com.aetherstream.catalyst.controller;

import com.aetherstream.catalyst.model.Arbitrage;
import com.aetherstream.catalyst.service.ArbitrageBroadcastingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1")
public class ArbitrageController {

    private final ArbitrageBroadcastingService arbitrageBroadcastingService;
    private static final Logger logger = LoggerFactory.getLogger(ArbitrageController.class);

    public ArbitrageController(ArbitrageBroadcastingService arbitrageBroadcastingService){
        this.arbitrageBroadcastingService = arbitrageBroadcastingService;
    }

    @GetMapping(value = "/arbitrage/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Arbitrage>> arbitrageStreamPropeller(){
        return arbitrageBroadcastingService.getArbitrages()
                .map(arbitrage -> ServerSentEvent.<Arbitrage>builder()
                        .event("arbitrage-opportunity")
                        .data(arbitrage)
                        .build())
                .doOnCancel(() -> logger.info("Client disconnected from arbitrage stream"));
    }
}