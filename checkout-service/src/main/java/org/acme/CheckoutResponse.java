package org.acme;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CheckoutResponse(String bookId, int amount, boolean success) {
};
