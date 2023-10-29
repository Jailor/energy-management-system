package andrei.assignment1.security;

import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> userList= userRepository.findByUsername(username);
        if(userList.isEmpty()){
            throw new UsernameNotFoundException("User not found " + username);
        }
        if(userList.size() > 1){
            throw new UsernameNotFoundException("Multiple users found " + username);
        }
        User userInfo = userList.get(0);
        return new UserDetailsInfo(userInfo);
    }
}
