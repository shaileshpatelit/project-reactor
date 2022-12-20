package com.shailesh.springsecurity;

import com.shailesh.springsecurity.config.SecurityConfig;
import com.shailesh.springsecurity.rest.AdminController;
import com.shailesh.springsecurity.rest.DBAController;
import com.shailesh.springsecurity.rest.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {UserController.class, AdminController.class, DBAController.class})
@ContextConfiguration(classes = {SpringSecurityApplication.class, SecurityConfig.class})
class SpringSecurityApplicationTests {

	@Autowired
	WebTestClient webClient;

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
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
	@WithMockUser(username = "user", roles = {"DBA"})
	public void invalidUser() {
		webClient
				.get().uri("/user/sayHello")
				.exchange()
				.expectStatus()
				.isForbidden();
	}

	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
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
	@WithMockUser(username = "admin", roles = {"DBA"})
	public void invalidAdmin() {
		webClient
				.get().uri("/admin/sayHello")
				.exchange()
				.expectStatus()
				.isForbidden();
	}

	@Test
	@WithMockUser(username = "dba", roles = {"ADMIN", "DBA"})
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
	@WithMockUser(username = "dba", roles = {"USER"})
	public void invalidDba() {
		webClient
				.get().uri("/dba/sayHello")
				.exchange()
				.expectStatus()
				.isForbidden();
	}
}
