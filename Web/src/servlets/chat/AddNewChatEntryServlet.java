package servlets.chat;

import engine.managers.SDMChatManager;
import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddNewChatEntryServlet", urlPatterns = {"/send"})
public class AddNewChatEntryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) {
        String username = SessionUtils.getUsername(req);
        String userNewMessage = req.getParameter(Constants.USER_MESSAGE);

        if (userNewMessage != null && !userNewMessage.isEmpty()) {
            synchronized (getServletContext()) {
                SDMChatManager.getInstance().addNewChatEntry(username, userNewMessage);
            }
        }
    }
}
