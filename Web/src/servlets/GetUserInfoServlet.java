package servlets;

import com.google.gson.Gson;
import dto.models.UserDTO;
import engine.managers.SDMUsersManager;
import engine.managers.UsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserInfoServlet", urlPatterns = {"/user-info"})
public class GetUserInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            UsersManager usersManager = SDMUsersManager.getInstance();
            Gson gson = new Gson();
            UserDTO user = usersManager.getUserByUsername(SessionUtils.getUsername(req));
            String json = gson.toJson(user);
            out.println(json);
            out.flush();
        }
    }
}
