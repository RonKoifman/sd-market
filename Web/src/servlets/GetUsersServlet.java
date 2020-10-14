package servlets;

import com.google.gson.Gson;
import dto.models.RegionDTO;
import dto.models.UserDTO;
import engine.managers.RegionsManager;
import engine.managers.SDMRegionsManager;
import engine.managers.SDMUsersManager;
import engine.managers.UsersManager;

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
            UsersManager usersManager = SDMUsersManager.getInstance();
            Gson gson = new Gson();
            Collection<UserDTO> users = usersManager.getUsers();
            String json = gson.toJson(users);
            out.println(json);
            out.flush();
        }
    }
}
