package org.acme;

import java.util.regex.Pattern;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public class CheckoutService {
	private static final String CREDIT_CARD_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?" + // Visa
			"|5[1-5][0-9]{14}" + // MasterCard
			"|6(?:011|5[0-9]{2})[0-9]{12}" + // Discover
			"|3[47][0-9]{13}" + // American Express
			"|3(?:0[0-5]|[68][0-9])[0-9]{11}" + // Diners Club
			"|(?:2131|1800|35\\d{3})\\d{11})$"; // JCB

	@Incoming("checkout-request")
	@Outgoing("checkout-response")
	public Multi<CheckoutResponse> processCheckout(Multi<CheckoutRequest> requests) {
		return requests
				.onItem().transform(request -> {
					System.out.println("Recieved request");
					boolean valid = isValidCreditCard(request.creditCardNumber());

					return new CheckoutResponse(request.bookId(), request.amount(), valid);
				});
	}

	private boolean isValidCreditCard(String cardNumber) {
		return Pattern.matches(CREDIT_CARD_REGEX, cardNumber);
	}
}
