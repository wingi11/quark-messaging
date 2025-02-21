package org.acme;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import jakarta.transaction.Transactional;

public class BookService {

	@Incoming("checkout-response")
	@Transactional
	public void processOrder(CheckoutResponse response) {
		if (!response.success()) {
			System.out.println("Recieved unsuccessful response");
			return;
		}

		if (!ObjectId.isValid(response.bookId())) {
			System.out.println("Recieved response with invalid id");
			return;
		}

		Book book = Book.findById(new ObjectId(response.bookId()));

		if (book == null) {
			throw new IllegalArgumentException("Book not found with ID: " + response.bookId());
		}

		book.stock = book.stock - response.amount();

		// Ship book to customer

		System.out.println("Book shipped. Book stock:" + book.stock);

		book.update();
	}
}
