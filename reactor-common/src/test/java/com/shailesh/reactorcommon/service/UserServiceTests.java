package com.shailesh.reactorcommon.service;

import com.shailesh.reactorcommon.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
class UserServiceTests {

	UserService userService = new UserService();
	List<User> users = new ArrayList<>();

	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(userService, "users", users);
	}

	@Test
	public void saveUser() {
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		Mono<User> resp = userService.saveUser(user);
		StepVerifier.create(resp)
				.expectSubscription()
				.consumeNextWith(u -> {
					AssertionErrors.assertEquals("Invalid user", user.getId(), u.getId());
				})
				.verifyComplete();
	}

	@Test
	public void getUser() {
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		users.add(user);
		Mono<User> resp = userService.getUser(user.getId());
		StepVerifier.create(resp)
				.expectSubscription()
				.consumeNextWith(u -> {
					AssertionErrors.assertEquals("Invalid user", user.getId(), u.getId());
				})
				.verifyComplete();
	}

	@Test
	public void getAllUsers() {
		users.clear();
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		users.add(user);
		user = User.builder()
				.id(2).name("test2").role("user")
				.build();
		users.add(user);
		Flux<User> resp = userService.getAllUsers();
		StepVerifier.create(resp)
				.expectSubscription()
				.consumeNextWith(u -> {
					AssertionErrors.assertEquals("Invalid user", 1, u.getId());
				})
				.consumeNextWith(u -> {
					AssertionErrors.assertEquals("Invalid user2", 2, u.getId());
				})
				.verifyComplete();
	}

	@Test
	public void getAllUsersWithPaging() {
		users.clear();
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		users.add(user);
		user = User.builder()
				.id(2).name("test2").role("user")
				.build();
		users.add(user);
		user = User.builder()
				.id(3).name("test3").role("user")
				.build();
		users.add(user);
		Flux<User> resp = userService.getAllUsers(0, 2);
		StepVerifier.create(resp)
				.expectSubscription()
				.consumeNextWith(u -> {
					AssertionErrors.assertEquals("Invalid user", 1, u.getId());
				})
				.consumeNextWith(u -> {
					AssertionErrors.assertEquals("Invalid user2", 2, u.getId());
				})
				.verifyComplete();
	}

	@Test
	public void getUsersGroupdByRole() {
		users.clear();
		User user = User.builder()
				.id(1).name("test").role("user")
				.build();
		users.add(user);
		user = User.builder()
				.id(2).name("test2").role("user")
				.build();
		users.add(user);
		user = User.builder()
				.id(3).name("test3").role("admin")
				.build();
		users.add(user);
		Mono<Map<String, List<User>>> resp = userService.getUsersGroupdByRole();
		StepVerifier.create(resp)
				.expectSubscription()
				.consumeNextWith(map -> {
					AssertionErrors.assertEquals("Invalid user size for role user", 2, map.get("user").size());
					AssertionErrors.assertEquals("Invalid user size for role admin", 1, map.get("admin").size());
				})
				.verifyComplete();
	}

}
