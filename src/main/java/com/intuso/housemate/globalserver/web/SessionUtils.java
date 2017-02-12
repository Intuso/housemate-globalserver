package com.intuso.housemate.globalserver.web;

import com.intuso.housemate.globalserver.database.model.Client;
import com.intuso.housemate.globalserver.database.model.User;

import javax.servlet.http.HttpSession;

/**
 * Created by tomc on 12/02/17.
 */
public class SessionUtils {

    public final static String USER = "user";
    public final static String CLIENT = "client";

    public static User getUser(HttpSession session) {
        return (User) session.getAttribute(USER);
    }

    public static void setUser(HttpSession session, User user) {
        session.setAttribute(USER, user);
    }

    public static Client getClient(HttpSession session) {
        return (Client) session.getAttribute(CLIENT);
    }

    public static void setClient(HttpSession session, Client client) {
        session.setAttribute(CLIENT, client);
    }
}
