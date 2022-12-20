package com.shailesh.reactorcommon;

import com.shailesh.reactorcommon.rest.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {UserController.class})
@ContextConfiguration(classes = {ReactorCommonApplication.class})
class ReactorCommonApplicationTests {

	@Autowired
	WebTestClient webClient;

	@Test
	public void validUser() {
		webClient
				.get().uri("/user/sayHello")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.consumeWith(r -> {
					AssertionErrors.assertTrue("Unexpected response",r.getResponseBody().startsWith("Hello user"));
				});
	}

	@Test
	public void invalidUser() {
		webClient
				.get().uri("/user/sayHello")
				.exchange()
				.expectStatus()
				.isForbidden();
	}

	@Test
	public void validAdmin() {
		webClient
				.get().uri("/admin/sayHello")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.consumeWith(r -> {
					AssertionErrors.assertTrue("Unexpected response",r.getResponseBody().startsWith("Hello admin"));
				});
	}

	@Test
	public void invalidAdmin() {
		webClient
				.get().uri("/admin/sayHello")
				.exchange()
				.expectStatus()
				.isForbidden();
	}

	@Test
	public void validDba() {
		webClient
				.get().uri("/dba/sayHello")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.consumeWith(r -> {
					AssertionErrors.assertTrue("Unexpected response",r.getResponseBody().startsWith("Hello dba"));
				});
	}

	@Test
	public void invalidDba() {
		webClient
				.get().uri("/dba/sayHello")
				.exchange()
				.expectStatus()
				.isForbidden();
	}
}
