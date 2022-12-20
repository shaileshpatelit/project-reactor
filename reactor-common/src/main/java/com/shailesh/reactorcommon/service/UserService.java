package com.shailesh.reactorcommon.service;

import com.shailesh.reactorcommon.exception.ResourceNotFoundException;
import com.shailesh.reactorcommon.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();

    /**
     * Save user
     * @param u user object
     * @return saved user object
     */
    public Mono<User> saveUser(User u) {
        users.add(u);
        return Mono.just(u);
    }

    /**
     * Get user by id
     * @param id id of the user
     * @return user matching given id
     */
    public Mono<User> getUser(int id) {
        return Flux.fromIterable(users).filter(u -> u.getId() == id)
                .next()//Get first matching object
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No user found with id "+id))) //Switch to error if no matching object found and stream is empty
                ;
    }

    /**
     * Get all users grouped by role
     * @return Map of role as key and users as value
     */
    public Mono<Map<String, List<User>>> getUsersGroupdByRole() {
        return Flux.fromIterable(users)
                .groupBy(User::getRole)//Group by role
                .flatMap(Flux::collectList)//Collect grouped list
                .collectMap(userList -> userList.get(0).getRole(), u -> u)//Collect key and value as map
                ;
    }

    /**
     * Get all users
     * @return
     */
    public Flux<User> getAllUsers() {
        return Flux.fromIterable(users);
    }

    /**
     * Get users with pagination
     * @param page page number starting from 0
     * @param size page size
     * @return
     */
    public Flux<User> getAllUsers(int page, int size) {
        return Flux.fromIterable(users).skip(page * size).take(size);
    }

}
