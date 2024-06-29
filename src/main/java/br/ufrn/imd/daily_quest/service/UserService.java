package br.ufrn.imd.daily_quest.service;

import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.exception.UnauthorizedException;
import br.ufrn.imd.daily_quest.model.User;
import br.ufrn.imd.daily_quest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        String password = user.getPassword();
        user.setPassword(passwordService.hashPassword(password));
        return userRepository.save(user);
    }

    public User login(String username, String password) throws UnauthorizedException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordService.checkPassword(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return user;
    }

    public void deleteById(Long id) throws NotFoundException {
        Optional<User> user = getById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User update(Long id, User user) throws NotFoundException {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setUsername(user.getUsername());
                    existingUser.setUserType(user.getUserType());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new NotFoundException("User not found"));
    }
}