package com.shailesh.springsecurity.rest;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Admin controller meant to be accessed only by the users having ADMIN role
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

  @GetMapping("/sayHello")
  Mono<String> sayHello() {
    return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> "Hello "+((User) ctx.getAuthentication().getPrincipal()).getUsername()+", Only users with ADMIN roles allowed to access this API");
  }

}