package servlets.order;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.models.DiscountInformationDTO;
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
import java.util.Collection;
import java.util.Map;

@WebServlet(name = "GetAvailableDiscountsServlet", urlPatterns = {"/available-discounts"})
public class GetAvailableDiscountsServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
            Gson gson = new Gson();
            Collection<DiscountInformationDTO> availableDiscounts = singleRegionManager.getAvailableDiscountsFromPendingOrderByUsername(SessionUtils.getUsername(req));
            String json = gson.toJson(availableDiscounts);
            out.print(json);
            out.flush();
        }
    }
}
