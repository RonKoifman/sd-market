package servlets.signup;

import utils.Constants;
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

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String usernameFromSession = SessionUtils.getUsername(req);

            if (usernameFromSession == null) {
                String usernameFromParameter = req.getParameter(Constants.USERNAME);
                String userRoleFromParameter = req.getParameter(Constants.USER_ROLE);

                if (usernameFromParameter == null) {
                    out.print(Constants.SIGNUP_URL);
                } else if (usernameFromParameter.trim().isEmpty()) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("Please enter at least one character.");
                } else if (!usernameFromParameter.trim().matches("[a-zA-Z0-9 ]+")) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("Please enter only english letters and digits.");
                } else {
                    usernameFromParameter = usernameFromParameter.trim();
                    if (SDMUsersManager.getInstance().isUserExists(usernameFromParameter)) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("This username is already taken.");
                    } else {
                        SDMUsersManager.getInstance().addNewUser(usernameFromParameter, UserRole.valueOf(userRoleFromParameter.toUpperCase()));
                        SessionUtils.setUsername(req, usernameFromParameter);
                        SessionUtils.setUserRole(req, userRoleFromParameter);
                        out.print(Constants.HOME_URL);
                    }
                }
            } else {
                out.print(Constants.HOME_URL);
            }

            out.flush();
        }
    }
}
