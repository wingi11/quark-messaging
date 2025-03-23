package org.acme;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/books")
public class BookResource {

	@Inject
	@Channel("checkout-request")
	Emitter<CheckoutRequest> checkoutEmitter;

	@Inject
	Logger log;

	@POST
	@Path("/checkout")
	public Response checkoutBook(CheckoutRequest request) {
		log.info("Checkout request recieved for book: " + request.bookId());

		get(request.bookId());
		checkoutEmitter.send(request);

		return Response.accepted().build();
	}

	@POST
	// @Transactional
	public List<Book> addBook(Book book) {
		log.info("Adding book: " + book.title);
		book.id = ObjectId.get();
		book.persist();

		return list();
	}

	@GET
	public List<Book> list() {
		log.info("Listing books");
		return Book.listAll();
	}

	@GET
	@Path("{id}")
	public Book get(@RestPath String id) {
		log.info("Getting book: " + id);

		if (!ObjectId.isValid(id)) {
			log.warn("Invalid id: " + id);
			throw new BadRequestException("Invalid id");
		}

		Optional<Book> book = Book.findByIdOptional(new ObjectId(id));

		return book.orElseThrow(() -> new NotFoundException());
	}
}
