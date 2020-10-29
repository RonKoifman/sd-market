package servlets.add;

import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AddNewStoreServlet", urlPatterns = {"/add-new-store"})
public class AddNewStoreServlet extends HttpServlet {

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
            Map<Integer, Float> itemIdToItemPriceInStore = new HashMap<>();
            Map<String, String[]> parameterMap = req.getParameterMap();
            parameterMap.forEach((itemId, itemPrice) -> {
                if (!itemPrice[0].isEmpty()) {
                    itemIdToItemPriceInStore.put(Integer.parseInt(itemId), Float.parseFloat(itemPrice[0]));
                }
            });

            if (itemIdToItemPriceInStore.isEmpty()) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("Please choose at least one item for sale before adding a new store.");
            } else {
                String ownerUsername = SessionUtils.getUsername(req);
                int storeId = Integer.parseInt(req.getSession().getAttribute(Constants.STORE_ID).toString());
                String storeName = req.getSession().getAttribute(Constants.STORE_NAME).toString().trim();
                int xCoordinate = Integer.parseInt(req.getSession().getAttribute(Constants.X_COORDINATE).toString());
                int yCoordinate = Integer.parseInt(req.getSession().getAttribute(Constants.Y_COORDINATE).toString());
                float deliveryPPK = Float.parseFloat(req.getSession().getAttribute(Constants.PPK).toString());
                singleRegionManager.addNewStoreToRegion(storeId, ownerUsername, storeName, new Point(xCoordinate, yCoordinate), deliveryPPK, itemIdToItemPriceInStore);
            }

            out.flush();
        }
    }
}
