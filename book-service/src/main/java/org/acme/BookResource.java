package org.acme;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.RestPath;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/books")
public class BookResource {

	@Inject
	@Channel("checkout-request")
	Emitter<CheckoutRequest> checkoutEmitter;

	@POST
	@Path("/checkout")
	public Response checkoutBook(CheckoutRequest request) {
		get(request.bookId());

		checkoutEmitter.send(request);

		return Response.accepted().build();
	}

	@POST
	// @Transactional
	public List<Book> addBook(Book book) {
		book.id = ObjectId.get();
		book.persist();

		return list();
	}

	@GET
	public List<Book> list() {
		return Book.listAll();
	}

	@GET
	@Path("{id}")
	public Book get(@RestPath String id) {
		if (!ObjectId.isValid(id))
			throw new BadRequestException("Invalid id");

		Optional<Book> book = Book.findByIdOptional(new ObjectId(id));

		return book.orElseThrow(() -> new NotFoundException());
	}
}
