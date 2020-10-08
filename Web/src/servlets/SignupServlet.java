package servlets;

import constants.Constants;
import engine.api.UserManager;
import engine.enums.UserRole;
import engine.managers.SDMUserManager;
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
        try (PrintWriter out = res.getWriter()) {
            String usernameFromSession = SessionUtils.getUsername(req);
            UserManager userManager = SDMUserManager.getInstance();

            if (usernameFromSession == null) {
                String usernameFromParameter = req.getParameter(Constants.USERNAME);
                String userRoleFromParameter = req.getParameter(Constants.USER_ROLE);

                if (usernameFromParameter == null) {
                    out.print(Constants.SIGNUP_URL);
                } else if (usernameFromParameter.trim().isEmpty()) {
                    out.print("Please enter at least one valid character.");
                } else {
                    usernameFromParameter = usernameFromParameter.trim();
                    synchronized (this) {
                        if (userManager.isUserExists(usernameFromParameter)) {
                            out.print("This username is already taken.");
                        } else {
                            switch (userRoleFromParameter) {
                                case Constants.STORE_OWNER:
                                    userManager.addUser(usernameFromParameter, UserRole.valueOf(Constants.STORE_OWNER.toUpperCase()));
                                    break;

                                case Constants.CUSTOMER:
                                    userManager.addUser(usernameFromParameter, UserRole.valueOf(Constants.CUSTOMER.toUpperCase()));
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
