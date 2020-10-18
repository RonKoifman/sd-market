package utils;

import constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionUtils {

    public static String getUsername(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;

        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void setUsername(HttpServletRequest req, String username) {
        HttpSession session = req.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.USERNAME, username);
        }
    }

    public static String getUserRole(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USER_ROLE) : null;

        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void setUserRole(HttpServletRequest req, String userRole) {
        HttpSession session = req.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.USER_ROLE, userRole);
        }
    }
}