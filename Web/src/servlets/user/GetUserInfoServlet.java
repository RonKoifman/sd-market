package servlets.user;

import com.google.gson.Gson;
import dto.models.UserDTO;
import engine.managers.SDMUsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetUserInfoServlet", urlPatterns = {"/user-info"})
public class GetUserInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            UserDTO user;

            synchronized (getServletContext()) {
                user = SDMUsersManager.getInstance().getUserByUsername(SessionUtils.getUsername(req));
            }

            Gson gson = new Gson();
            String json = gson.toJson(user);
            out.print(json);
            out.flush();
        }
    }
}
