package com.aetherstream.catalyst.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Table("arbitrage_opportunity")
public record Arbitrage(
        @Id
        Long id, // R2DBC uses null/numeric wrapping to detect new records for auto-increment

        @Column("market_pair")
        String marketPair,

        @Column("lowest_buy_price")
        BigDecimal lowestBuyPrice,

        @Column("buy_exchange")
        String buyExchange,

        @Column("highest_sell_price")
        BigDecimal highestSellPrice,

        @Column("sell_exchange")
        String sellExchange,

        @Column("gross_spread")
        BigDecimal grossSpread,

        @Column("created_at")
        Instant createdAt
) {
    // Compact constructor to enforce defaults (like a timestamp) when creating new opportunities
    public Arbitrage {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}