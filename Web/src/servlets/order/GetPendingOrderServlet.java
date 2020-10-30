package servlets.order;

import com.google.gson.Gson;
import dto.models.GeneralOrderDTO;
import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            GeneralOrderDTO pendingOrder;

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                pendingOrder = singleRegionManager.getPendingOrderByUsername(SessionUtils.getUsername(req));
            }

            String json = gson.toJson(pendingOrder);
            out.print(json);
            out.flush();
        }
    }
}
