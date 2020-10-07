package servlets;

import constants.Constants;
import engine.managers.SDMUserManager;
import utils.SessionUtils;
import utils.ServletUtils;
import engine.api.UserManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static constants.Constants.USERNAME;

public class LoginServlet extends HttpServlet {

    private final String CHAT_ROOM_URL = "../chatroom/chatroom.html";
    private final String SIGN_UP_URL = "../signup/signup.html";
    private final String LOGIN_ERROR_URL = "/pages/loginerror/login_attempt_after_error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = SDMUserManager.getInstance();

        if (usernameFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.sendRedirect(SIGN_UP_URL);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
//                    if (userManager.isUserExists(usernameFromParameter)) {
//                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
//                        request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
//                        getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
//                    } else {
//                        userManager.addUser(usernameFromParameter);
//                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
//
//                        System.out.println("On login, request URI is: " + request.getRequestURI());
//                        response.sendRedirect(CHAT_ROOM_URL);
//                    }
                }
            }
        } else {
            response.sendRedirect(CHAT_ROOM_URL);
        }
    }
}
