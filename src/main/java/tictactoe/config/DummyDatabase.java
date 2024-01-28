package tictactoe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tictactoe.service.UserDetailsService;
import tictactoe.entity.User;
import tictactoe.repository.UserRepository;

@Component
public class DummyDatabase implements CommandLineRunner {
    private final UserRepository repository;

    private static final String USER1_USERNAME = "john";
    private static final String USER1_PASSWORD = "john";

    private static final String USER2_USERNAME = "doe";
    private static final String USER2_PASSWORD = "doe";

    @Autowired
    public DummyDatabase(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        createUser(USER1_USERNAME, USER1_PASSWORD);
        createUser(USER2_USERNAME, USER2_PASSWORD);
    }

    private void createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(UserDetailsService.PASSWORD_ENCODER.encode(password));

        repository.save(user);

    }
}
