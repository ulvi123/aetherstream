package com.aetherstream.common.dto;


import java.math.BigDecimal;
import java.time.Instant;

public record PriceUpdate(
   String symbol,
   BigDecimal price,
   String exchange,
   Instant timestamp
) {
    //compact controller-no need for this. call/ java does this for me automatically
    public PriceUpdate {
         if(symbol.isBlank()){
             throw new IllegalArgumentException("Symbol cannot be null");
         }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if(timestamp == null){
             timestamp = Instant.now();
         }
    }
}
