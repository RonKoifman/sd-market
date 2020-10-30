package servlets.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebServlet(name = "GetOrderTypeAndChosenStoreServlet", urlPatterns = {"/order-type-and-store"})
public class GetOrderTypeAndChosenStoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            String orderType = req.getSession().getAttribute(Constants.ORDER_TYPE).toString();
            int chosenStoreId = Integer.parseInt(req.getSession().getAttribute(Constants.CHOSEN_STORE_ID).toString());
            StoreDTO chosenStore;

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                chosenStore = singleRegionManager.getStoreById(chosenStoreId);
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("orderType", orderType);
            jsonObject.add("chosenStore", gson.toJsonTree(chosenStore));
            out.print(gson.toJson(jsonObject));
            out.flush();
        }
    }
}
