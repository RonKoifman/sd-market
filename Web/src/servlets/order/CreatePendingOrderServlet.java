package servlets.order;

import dto.models.RegionItemDTO;
import dto.models.StoreDTO;
import dto.models.StoreItemDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreatePendingOrderServlet", urlPatterns = {"/create-pending-order"})
public class CreatePendingOrderServlet extends HttpServlet {

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String regionName = SessionUtils.getRegionName(req);
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(regionName);

            String customerUsername = SessionUtils.getUsername(req);
            Point orderDestination = new Point(Integer.parseInt(req.getSession().getAttribute(Constants.X_COORDINATE).toString()), Integer.parseInt(req.getSession().getAttribute(Constants.Y_COORDINATE).toString()));
            LocalDate orderDate = LocalDate.parse(req.getSession().getAttribute(Constants.ORDER_DATE).toString());
            boolean isDynamicOrder = req.getSession().getAttribute(Constants.ORDER_TYPE).toString().equals("dynamicOrder");
            StoreDTO chosenStore = isDynamicOrder ? null : singleRegionManager.getStoreById(Integer.parseInt(req.getSession().getAttribute(Constants.CHOSEN_STORE_ID).toString()));
            Map<RegionItemDTO, Float> itemToItemPurchaseAmount = new HashMap<>();

            ArrayList<RegionItemDTO> regionItems = new ArrayList<>(singleRegionManager.getAllItemsInRegion());
            String[] parameters = req.getParameterMap().get(Constants.ITEM_PURCHASE_AMOUNT);
            for (int i = 0; i < parameters.length; i++) {
                if (!parameters[i].isEmpty()) {
                    itemToItemPurchaseAmount.put(regionItems.get(i), Float.parseFloat(parameters[i]));
                }
            }

            singleRegionManager.createNewPendingOrder(isDynamicOrder, orderDate, orderDestination, customerUsername, chosenStore, itemToItemPurchaseAmount);
            out.flush();
        }
    }
}
