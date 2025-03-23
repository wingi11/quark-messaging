package org.acme;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CheckoutRequest(String bookId, String creditCardNumber, int amount) {
};
