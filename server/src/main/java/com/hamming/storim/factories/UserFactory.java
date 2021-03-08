package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.model.User;
import com.hamming.storim.util.StringUtils;

import java.util.List;

public class UserFactory {
    private static UserFactory instance;

    private UserFactory() {
    };

    public static UserFactory getInstance() {
        if ( instance == null ) {
            instance = new UserFactory();
        }
        return instance;
    }

    public List<User> getUsers() {
        return Database.getInstance().getAll(User.class);
    }

    //This add user has an email within the constructor.
    public User addUser(String fullName, String username, String password, String email) {
        User u = addUser(fullName, username, password);
        u.setEmail(email);
        return u;
    }

    public User addUser(String name, String username, String password) {
        Long id = Database.getInstance().getNextID();
        User u = new User(id);
        u.setName(name);
        u.setUsername(username);
        u.setPassword(StringUtils.hashPassword(password));
        Database.getInstance().addBasicObject(u);
        return u;
    }

    public void deleteUser(User user) {
        Database.getInstance().removeBasicObject(user);
    }

    // NOTE this methods expect a HASHED password!
    public User validateUser(String username, String hashedPassword) {
        User u = findUserByUsername(username);
        if (u != null && !u.getPassword().equals(hashedPassword) ) {
            // Wrong password!
            u = null;
        }
        return u;
    }

    public User findUserByUsername( String username ) {
        List<User> users = Database.getInstance().getAll(User.class);
        User returnValue = null;
        for (User u : users ) {
            if (u.getUsername().equals(username)) {
                returnValue = u;
                break;
            }
        }
        return returnValue;
    }

    public User findUserById( Long id ) {
        return Database.getInstance().findById(User.class, id);
    }



}
