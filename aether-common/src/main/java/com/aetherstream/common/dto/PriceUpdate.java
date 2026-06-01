package com.aetherstream.common.dto;

public record PriceUpdate(
        String exchange,
        String symbol,
        double price,
        long timestamp
) {
    public PriceUpdate {
      if(exchange == null || exchange.isEmpty()) {
          throw new IllegalArgumentException("Exchange cannot be null or empty");
      }
      if(price<=0){
          throw new IllegalArgumentException("Price cannot be less than or equal to price");
      }
    }
}
