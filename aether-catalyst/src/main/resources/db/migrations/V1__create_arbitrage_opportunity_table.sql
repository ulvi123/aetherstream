CREATE TABLE arbitrage_opportunity (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    -- Market details
    market_pair VARCHAR(20) NOT NULL,          -- e.g., 'BTC/USDT', 'ETH/USDC'

    -- Buy execution side (The cheaper exchange where you buy)
    lowest_buy_price NUMERIC(18, 8) NOT NULL,   -- Supports high precision crypto decimals
    buy_exchange VARCHAR(50) NOT NULL,         -- e.g., 'Binance'

    -- Sell execution side (The pricier exchange where you sell)
    highest_sell_price NUMERIC(18, 8) NOT NULL,
    sell_exchange VARCHAR(50) NOT NULL,        -- e.g., 'Coinbase'

    -- Metrics
    gross_spread NUMERIC(18, 8) NOT NULL,      -- Absolute price difference ($ Delta)
    percentage_spread NUMERIC(5, 2) GENERATED ALWAYS AS
        (
        ((highest_sell_price - lowest_buy_price) / lowest_buy_price) * 100
        ) STORED, -- Optional: Auto-calculates the % return for easy filtering

    -- Audit
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Indexes for rapid querying and analysis
CREATE INDEX idx_arbitrage_market ON arbitrage_opportunity(market_pair);
CREATE INDEX idx_arbitrage_created_at ON arbitrage_opportunity(created_at DESC);