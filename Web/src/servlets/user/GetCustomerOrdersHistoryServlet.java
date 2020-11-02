package servlets.user;

import com.google.gson.Gson;
import dto.models.GeneralOrderDTO;
import engine.managers.SDMUsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetCustomerOrdersHistoryServlet", urlPatterns = {"/customer-orders-history"})
public class GetCustomerOrdersHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            String username = SessionUtils.getUsername(req);
            String regionName = SessionUtils.getRegionName(req);
            Collection<GeneralOrderDTO> orders;

            synchronized (getServletContext()) {
                orders = SDMUsersManager.getInstance().getCustomerOrdersByRegionName(username, regionName);
            }

            Gson gson = new Gson();
            String json = gson.toJson(orders);
            out.print(json);
            out.flush();
        }
    }
}
