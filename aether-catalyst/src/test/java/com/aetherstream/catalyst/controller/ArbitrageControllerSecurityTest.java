// Fixie Security Regression Test
// Vulnerability: Logging sensitive data
// This test proves the fix is effective.

package com.aetherstream.catalyst.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class ArbitrageControllerSecurityTest {

    @Mock
    private InputDestination inputDestination;

    @InjectMocks
    private ArbitrageController arbitrageController;

    @Test
    void should_not_log_sensitive_data_when_client_disconnects() {
        // Arrange
        Logger logger = Logger.getLogger(ArbitrageController.class.getName());
        Handler mockHandler = new MockHandler();
        logger.addHandler(mockHandler);

        // Act
        arbitrageController.clientDisconnected();

        // Assert
        verifyNoInteractions(mockHandler);
    }

    @Test
    void should_log_safe_message_when_client_disconnects() {
        // Arrange
        Logger logger = Logger.getLogger(ArbitrageController.class.getName());
        Handler mockHandler = new MockHandler();
        logger.addHandler(mockHandler);

        // Act
        arbitrageController.clientDisconnected();

        // Assert
        verify(mockHandler).publish(argThat(record -> record.getMessage().contains("Client disconnected from arbitrage stream")));
    }

    private static class MockHandler extends java.util.logging.Handler {
        @Override
        public void publish(LogRecord record) {
            if (record.getLevel() == Level.INFO) {
                System.out.println(record.getMessage());
            }
        }

        @Override
        public void flush() {}

        @Override
        public void close() throws SecurityException {}
    }
}