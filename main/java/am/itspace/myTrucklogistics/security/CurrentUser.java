package am.itspace.myTrucklogistics.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {

    private am.itspace.myTrucklogistics.models.User user;

    public CurrentUser(am.itspace.myTrucklogistics.models.User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public am.itspace.myTrucklogistics.models.User getCustomer() {
        return user;
    }
}
