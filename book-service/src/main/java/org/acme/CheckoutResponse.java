package org.acme;

public record CheckoutResponse(String bookId, int amount, boolean success) {
};
