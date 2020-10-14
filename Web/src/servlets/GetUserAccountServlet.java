package servlets;

import com.google.gson.Gson;
import dto.models.AccountDTO;
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

@WebServlet(name = "GetUserAccountServlet", urlPatterns = {"/user-account"})
public class GetUserAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        UsersManager usersManager = SDMUsersManager.getInstance();

        try (PrintWriter out = res.getWriter()) {
            Gson gson = new Gson();
            UserDTO user = usersManager.getUserByUsername(SessionUtils.getUsername(req));
            AccountDTO userAccount = user.getAccount();
            String json = gson.toJson(userAccount);
            out.println(json);
            out.flush();
        }
    }
}
