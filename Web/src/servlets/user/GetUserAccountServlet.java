package servlets.user;

import com.google.gson.Gson;
import dto.models.AccountDTO;
import engine.managers.SDMAccountsManager;
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
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            AccountDTO account;

            synchronized (getServletContext()) {
                account = SDMAccountsManager.getInstance().getAccountByUsername(SessionUtils.getUsername(req));
            }

            String json = gson.toJson(account);
            out.print(json);
            out.flush();
        }
    }
}
