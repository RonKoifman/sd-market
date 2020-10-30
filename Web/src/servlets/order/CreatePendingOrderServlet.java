package servlets.order;

import dto.models.RegionItemDTO;
import dto.models.StoreDTO;
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
import java.util.*;

@WebServlet(name = "CreatePendingOrderServlet", urlPatterns = {"/create-pending-order"})
public class CreatePendingOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String customerUsername = SessionUtils.getUsername(req);
            Point orderDestination = new Point(Integer.parseInt(req.getSession().getAttribute(Constants.X_COORDINATE).toString()), Integer.parseInt(req.getSession().getAttribute(Constants.Y_COORDINATE).toString()));
            LocalDate orderDate = LocalDate.parse(req.getSession().getAttribute(Constants.ORDER_DATE).toString());
            boolean isDynamicOrder = req.getSession().getAttribute(Constants.ORDER_TYPE).toString().equals(Constants.DYNAMIC_ORDER);

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                Map<RegionItemDTO, Float> itemToItemPurchaseAmount = new HashMap<>();
                Map<String, String[]> parameterMap = req.getParameterMap();

                parameterMap.forEach((itemId, purchaseAmount) -> {
                    if (!purchaseAmount[0].isEmpty()) {
                        RegionItemDTO item = singleRegionManager.getItemById(Integer.parseInt(itemId));
                        itemToItemPurchaseAmount.put(item, Float.parseFloat(purchaseAmount[0]));
                    }
                });

                if (itemToItemPurchaseAmount.isEmpty()) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("Please purchase at least one item before continuing to checkout.");
                } else {
                    StoreDTO chosenStore = isDynamicOrder ? null : singleRegionManager.getStoreById(Integer.parseInt(req.getSession().getAttribute(Constants.CHOSEN_STORE_ID).toString()));
                    singleRegionManager.createNewPendingOrder(isDynamicOrder, orderDate, orderDestination, customerUsername, chosenStore, itemToItemPurchaseAmount);
                    out.print(Constants.CHECKOUT_URL);
                }
            }

            out.flush();
        }
    }
}
