/**
 * @author: Luqiao Dai
 *  * Code similar to lab login system content
 */
package com.example.coursesearch;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    /*
    The User class* to store info related to users who have access to our app.
     */

    private String id;
    private String username;
    private String password;

    public User(String id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    static ArrayList<User> users = new ArrayList<>();

    public void addUser(){users.add(this);}

    public static String getPassword(String userName){
        for (User user: users){
            if (user.username.equals(userName)){
                return user.password;
            }
        }

        return "";
    }

    public static String getID(String username){
        for (User user: users){
            if (user.username.equals(username)){
                return user.id;
            }
        }

        return "";
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id + ", username=" + username + ", password=" + password + '}';
    }
}
