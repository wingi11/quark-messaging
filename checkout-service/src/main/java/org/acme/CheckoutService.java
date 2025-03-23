package org.acme;

import java.util.regex.Pattern;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CheckoutService {
	private static final String CREDIT_CARD_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?" + // Visa
			"|5[1-5][0-9]{14}" + // MasterCard
			"|6(?:011|5[0-9]{2})[0-9]{12}" + // Discover
			"|3[47][0-9]{13}" + // American Express
			"|3(?:0[0-5]|[68][0-9])[0-9]{11}" + // Diners Club
			"|(?:2131|1800|35\\d{3})\\d{11})$"; // JCB

	@Inject
	Logger log;

	@Incoming("checkout-request")
	@Outgoing("checkout-response")
	public Multi<CheckoutResponse> processCheckout(Multi<CheckoutRequest> requests) {
		return requests
				.onItem().transform(request -> {
					log.info("Processing checkout request for book: " + request.bookId());
					boolean valid = isValidCreditCard(request.creditCardNumber());

					if (!valid) {
						log.warn("Invalid credit card number");
					}

					return new CheckoutResponse(request.bookId(), request.amount(), valid);
				});
	}

	private boolean isValidCreditCard(String cardNumber) {
		return Pattern.matches(CREDIT_CARD_REGEX, cardNumber);
	}
}
