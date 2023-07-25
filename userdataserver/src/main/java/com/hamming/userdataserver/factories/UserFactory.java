package com.hamming.userdataserver.factories;


import com.hamming.storim.common.util.StringUtils;
import com.hamming.storim.server.Database;
import com.hamming.userdataserver.model.User;

import java.util.List;

public class UserFactory {
    private static UserFactory instance;

    private static User ROOT_USER;
    private static String ROOT_USERNAME = "root";

    private UserFactory() {
        setRootUser();
    }

    private void setRootUser() {
        ROOT_USER = findUserByUsername(ROOT_USERNAME);
        if ( ROOT_USER == null ) {
            ROOT_USER = addUser(ROOT_USERNAME, ROOT_USERNAME, StringUtils.hashPassword(ROOT_USERNAME));
        }
    }

    public static UserFactory getInstance() {
        if ( instance == null ) {
            instance = new UserFactory();
        }
        return instance;
    }

    public User getRootUser() {
        return ROOT_USER;
    }

    public List<User> getUsers() {
        return Database.getInstance().getAll(User.class);
    }

    //This add user has an email within the constructor.
    public User addUser(User creator, String fullName, String username, String password, String email) {
        User u = addUser(fullName, username, password);
        u.setEmail(email);
        if ( creator != null ) {
            u.setCreatorId(creator.getId());
            u.setOwnerId(creator.getId());
        }
        return u;
    }

    public User addUser(String name, String username, String password) {
        Long id = Database.getInstance().getNextID();
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setUsername(username);
        u.setPassword(password);
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
