package servlets.orders;

import com.google.gson.Gson;
import dto.models.SubOrderDTO;
import engine.managers.SDMUsersManager;
import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetStoreOwnerOwnedStoreOrdersServlet", urlPatterns = {"/owner-store-orders"})
public class GetStoreOwnerOwnedStoreOrdersServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            String username = SessionUtils.getUsername(req);
            String regionName = SessionUtils.getRegionName(req);
            int storeId = Integer.parseInt(req.getParameter(Constants.STORE_ID));
            Collection<SubOrderDTO> storeOrders = SDMUsersManager.getInstance().getStoreOwnerOwnedStoreOrdersByRegionName(username, regionName, storeId);
            String json = gson.toJson(storeOrders);
            out.print(json);
            out.flush();
        }
    }
}
