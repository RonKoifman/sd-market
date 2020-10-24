package servlets.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.models.GeneralOrderDTO;
import dto.models.StoreDTO;
import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetPendingOrderServlet", urlPatterns = {"/get-pending-order"})
public class GetPendingOrderServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
            GeneralOrderDTO pendingOrder = singleRegionManager.getPendingOrderByUsername(SessionUtils.getUsername(req));
            String json = gson.toJson(pendingOrder);
            out.print(json);
            out.flush();
        }
    }
}
