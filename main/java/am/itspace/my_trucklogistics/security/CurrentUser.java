package am.itspace.my_trucklogistics.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {

    private am.itspace.my_trucklogistics.models.User user;

    public CurrentUser(am.itspace.my_trucklogistics.models.User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public am.itspace.my_trucklogistics.models.User getCustomer() {
        return user;
    }
}
