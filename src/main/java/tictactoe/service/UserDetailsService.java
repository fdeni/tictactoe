package tictactoe.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tictactoe.entity.User;
import tictactoe.repository.UserRepository;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final UserRepository repository;

    @Autowired
    public UserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = repository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("Invalid username: " + username);
        }
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER");
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
