package servlets.order;

import com.google.gson.Gson;
import dto.models.DiscountInformationDTO;
import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetAvailableDiscountsServlet", urlPatterns = {"/available-discounts"})
public class GetAvailableDiscountsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Collection<DiscountInformationDTO> availableDiscounts;

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                availableDiscounts = singleRegionManager.getAvailableDiscountsFromPendingOrderByUsername(SessionUtils.getUsername(req));
            }

            Gson gson = new Gson();
            String json = gson.toJson(availableDiscounts);
            out.print(json);
            out.flush();
        }
    }
}
