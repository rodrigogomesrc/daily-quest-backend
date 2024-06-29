package br.ufrn.imd.daily_quest.controller;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.exception.UnauthorizedException;
import br.ufrn.imd.daily_quest.model.PathUser;
import br.ufrn.imd.daily_quest.model.User;
import br.ufrn.imd.daily_quest.service.PathService;
import br.ufrn.imd.daily_quest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PathService pathService;

    public UserController(UserService userService, PathService pathService) {
        this.userService = userService;
        this.pathService = pathService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id)
            throws NotFoundException {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
            throws BadRequestException {
        User createdUser = userService.save(user);
        return ResponseEntity.ok().body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String username, @RequestParam String password)
            throws UnauthorizedException {
        User user = userService.login(username, password);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user)
            throws NotFoundException, BadRequestException {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
            throws NotFoundException {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/paths/{pathId}")
    public ResponseEntity<PathUser> addPathToUser(@PathVariable Long userId, @PathVariable Long pathId)
            throws NotFoundException, BadRequestException {
        PathUser pathUser = pathService.addUserToPath(pathId, userId);
        return ResponseEntity.ok().body(pathUser);
    }
}