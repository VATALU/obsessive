package org.vatalu.Repository;

import org.obsessive.web.lang.annotation.Repository;
import org.vatalu.model.User;

@Repository
public class UserRepository {

    //simple demo
    public User findUserById(long id) {
        User user = new User();
        user.setUserName("VATALU");
        user.setUserId(id);
        return user;
    }
}
