package com.shailesh.reactorcommon.rest;

import com.shailesh.reactorcommon.ReactorCommonApplication;
import com.shailesh.reactorcommon.model.User;
import com.shailesh.reactorcommon.rest.UserController;
import com.shailesh.reactorcommon.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {UserController.class})
@ContextConfiguration(classes = {ReactorCommonApplication.class})
class ReactorCommonApplicationTests {

	@Autowired
	WebTestClient webClient;

	@MockBean
	UserService userService;

	@Test
	public void saveUser() {
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		Mockito.when(userService.saveUser(ArgumentMatchers.any(User.class))).thenAnswer(i -> Mono.just(i.getArguments()[0]));
		webClient
				.post().uri("/user")
				.body(BodyInserters.fromValue(user))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(User.class)
				.consumeWith(r -> {
					AssertionErrors.assertEquals("Unexpected user id in response", user.getId(),r.getResponseBody().getId());
				});
	}

	@Test
	public void getUser() {
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		Mockito.when(userService.getUser(ArgumentMatchers.anyInt())).thenReturn(Mono.just(user));
		webClient
				.get().uri("/user/1")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(User.class)
				.consumeWith(r -> {
					AssertionErrors.assertEquals("Unexpected user id in response", 1,r.getResponseBody().getId());
				});
	}

}
