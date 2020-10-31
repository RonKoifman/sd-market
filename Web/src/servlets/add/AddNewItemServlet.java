package servlets.add;

import engine.exceptions.TakenIdException;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AddNewItemServlet", urlPatterns = {"/add-new-item"})
public class AddNewItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");

            int itemId = Integer.parseInt(req.getParameter(Constants.ITEM_ID));
            String itemName = req.getParameter(Constants.ITEM_NAME).trim();
            String itemPurchaseForm = req.getParameter(Constants.ITEM_PURCHASE_FORM).trim();

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                Map<Integer, Float> storeIdToItemPriceInStore = getStoreIdToItemPriceFromParameters(req.getParameterMap());

                try {
                    singleRegionManager.checkForAvailableId(itemId, "RegionItem");
                    if (storeIdToItemPriceInStore.isEmpty()) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("Please choose at least one store to sell the item before adding a new item.");
                    } else if (itemName.isEmpty()) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("Please enter at least one character for the item's name.");
                    } else if (!itemName.matches("[a-zA-Z0-9 ]+")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("Please enter only English letters and digits for the item's name.");
                    } else {
                        singleRegionManager.addNewItemToRegion(itemId, itemName, itemPurchaseForm, storeIdToItemPriceInStore);
                    }
                } catch (TakenIdException e) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("The entered item ID is already taken.");
                }
            }

            out.flush();
        }
    }

    private Map<Integer, Float> getStoreIdToItemPriceFromParameters(Map<String, String[]> parameterMap) {
        Map<Integer, Float> storeIdToItemPriceInStore = new HashMap<>();

        parameterMap.forEach((key, value) -> {
            if (key.contains(Constants.STORE_ID) && !value[0].isEmpty()) {
                int storeId = Integer.parseInt(key.replace(Constants.STORE_ID, "").trim());
                float itemPrice = Float.parseFloat(value[0]);
                storeIdToItemPriceInStore.put(storeId, itemPrice);
            }
        });

        return storeIdToItemPriceInStore;
    }
}
