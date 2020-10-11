package servlets;

import constants.Constants;
import engine.managers.UsersManager;
import engine.enums.UserRole;
import engine.managers.SDMUsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SignupServlet", urlPatterns = {"/signup"})
public class SignupServlet extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        try (PrintWriter out = res.getWriter()) {
            String usernameFromSession = SessionUtils.getUsername(req);
            UsersManager usersManager = SDMUsersManager.getInstance();

            if (usernameFromSession == null) {
                String usernameFromParameter = req.getParameter(Constants.USERNAME);
                String userRoleFromParameter = req.getParameter(Constants.USER_ROLE);

                if (usernameFromParameter == null) {
                    out.print(Constants.SIGNUP_URL);
                } else if (usernameFromParameter.trim().isEmpty()) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("Please enter at least one character.");
                } else if (!usernameFromParameter.trim().matches("[a-zA-Z0-9]+")) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("Please enter only english letters and digits.");
                } else {
                    usernameFromParameter = usernameFromParameter.trim();
                    synchronized (this) {
                        if (usersManager.isUserExists(usernameFromParameter)) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.print("This username is already taken.");
                        } else {
                            switch (userRoleFromParameter) {
                                case Constants.STORE_OWNER:
                                    usersManager.addUser(usernameFromParameter, UserRole.valueOf(Constants.STORE_OWNER.toUpperCase()));
                                    break;

                                case Constants.CUSTOMER:
                                    usersManager.addUser(usernameFromParameter, UserRole.valueOf(Constants.CUSTOMER.toUpperCase()));
                                    break;
                            }

                            req.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                            req.getSession(true).setAttribute(Constants.USER_ROLE, userRoleFromParameter);
                            out.print(Constants.HOME_URL);
                        }
                    }
                }
            } else {
                out.print(Constants.HOME_URL);
            }

            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }
}
