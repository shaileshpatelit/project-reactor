package com.shailesh.reactorcommon.rest;

import com.shailesh.reactorcommon.model.User;
import com.shailesh.reactorcommon.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


/**
 * User controller meant to be accessed only by the users having USER, ADMIN role
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Operation(description = "Api to save user")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully saved")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<User> saveUser(@RequestBody @Validated User user) {
    return userService.saveUser(user);
  }

  @Operation(description = "Api to get user by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
          @ApiResponse(responseCode = "404", description = "Not found")
  })
  @GetMapping(value = "/{id}")
  public Mono<User> getUser(@PathVariable int id) {
    return userService.getUser(id);
  }

  @Operation(description = "Api to get all users")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  })
  @GetMapping()
  public Flux<User> getAllUser() {
    return userService.getAllUsers();
  }

  @Operation(description = "Api to get users grouped by their roles")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  })
  @GetMapping(value = "/groupbyrole")
  public Mono<Map<String, List<User>>> getAllUsersGroupedByRole() {
    return userService.getUsersGroupdByRole();
  }

  @Operation(description = "Api to get users with pagination")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  })
  @GetMapping(value = "/page")
  public Flux<User> getUsersWithPagination(@RequestParam int page, @RequestParam int size) {
    return userService.getAllUsers(page, size);
  }
}