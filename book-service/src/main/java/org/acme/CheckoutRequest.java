package org.acme;

public record CheckoutRequest(String bookId, String creditCardNumber, int amount) {
};
