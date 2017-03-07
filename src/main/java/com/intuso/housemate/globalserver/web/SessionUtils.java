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
        return session != null ? (User) session.getAttribute(USER) : null;
    }

    public static void setUser(HttpSession session, User user) {
        if(session != null)
            session.setAttribute(USER, user);
    }

    public static Client getClient(HttpSession session) {
        return session != null ? (Client) session.getAttribute(CLIENT) : null;
    }

    public static void setClient(HttpSession session, Client client) {
        if(session != null)
            session.setAttribute(CLIENT, client);
    }
}
