package utils;

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

    public static String getRegionName(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.REGION_NAME) : null;

        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void setRegionName(HttpServletRequest req, String regionName) {
        HttpSession session = req.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.REGION_NAME, regionName);
        }
    }

    public static int getNotificationsVersion(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.NOTIFICATIONS_VERSION) : null;

        return sessionAttribute != null ? Integer.parseInt(sessionAttribute.toString()) : 0;
    }

    public static void setNotificationsVersion(HttpServletRequest req, int notificationsVersion) {
        HttpSession session = req.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.NOTIFICATIONS_VERSION, notificationsVersion);
        }
    }

    public static int getChatVersion(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.CHAT_VERSION) : null;

        return sessionAttribute != null ? Integer.parseInt(sessionAttribute.toString()) : 0;
    }

    public static void setChatVersion(HttpServletRequest req, int chatVersion) {
        HttpSession session = req.getSession(true);

        if (session != null) {
            session.setAttribute(Constants.CHAT_VERSION, chatVersion);
        }
    }
}