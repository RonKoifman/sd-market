package servlets.notifications;

import com.google.gson.Gson;
import dto.models.NotificationDTO;
import engine.managers.SDMNotificationsManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetUserNotificationsServlet", urlPatterns = {"/user-notifications"})
public class GetUserNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            String username = SessionUtils.getUsername(req);
            Collection<NotificationDTO> userUnseenNotifications;
            int serverNotificationsVersion;
            int clientNotificationsVersion = SessionUtils.getNotificationsVersion(req);

            synchronized (getServletContext()) {
                serverNotificationsVersion = SDMNotificationsManager.getInstance().getNotificationsVersionByUsername(username);
                userUnseenNotifications = SDMNotificationsManager.getInstance().getUserNotificationsByVersion(username, clientNotificationsVersion);
            }

            if (serverNotificationsVersion != clientNotificationsVersion) {
                SessionUtils.setNotificationsVersion(req, serverNotificationsVersion);
                Gson gson = new Gson();
                String json = gson.toJson(userUnseenNotifications);
                out.print(json);
            }

            out.flush();
        }
    }
}
