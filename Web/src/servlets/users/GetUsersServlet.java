package servlets.users;

import com.google.gson.Gson;
import dto.models.UserDTO;
import engine.managers.SDMUsersManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetUsersServlet", urlPatterns = {"/users"})
public class GetUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Collection<UserDTO> users;

            synchronized (getServletContext()) {
                users = SDMUsersManager.getInstance().getUsers();
            }

            Gson gson = new Gson();
            String json = gson.toJson(users);
            out.print(json);
            out.flush();
        }
    }
}
