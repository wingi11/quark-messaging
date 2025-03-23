package org.acme;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BookService {

	@Inject
	Logger log;

	@Incoming("checkout-response")
	public void processOrder(CheckoutResponse response) {
		log.info("Processing order for book: " + response.bookId());
		if (!response.success()) {
			log.warn("Recieved unsuccessful response");
			return;
		}

		if (!ObjectId.isValid(response.bookId())) {
			log.warn("Recieved response with invalid id");
			return;
		}

		Book book = Book.findById(new ObjectId(response.bookId()));

		if (book == null) {
			log.warn("Book not found");
			return;
		}

		if (book.stock < response.amount()) {
			log.warn("Not enough stock");
			return;
		}

		book.stock = book.stock - response.amount();

		log.info("Book shipped. Book stock:" + book.stock);

		book.update();
	}
}
