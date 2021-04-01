package am.itspace.my_trucklogistics.security;

import am.itspace.my_trucklogistics.models.User;
import am.itspace.my_trucklogistics.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(s).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new CurrentUser(user);
    }
}
