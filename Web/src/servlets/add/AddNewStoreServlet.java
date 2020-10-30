package servlets.add;

import engine.exceptions.OccupiedLocationException;
import engine.exceptions.TakenIdException;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String ownerUsername = SessionUtils.getUsername(req);

            int storeId = Integer.parseInt(req.getParameter(Constants.STORE_ID));
            String storeName = req.getParameter(Constants.STORE_NAME).trim();
            int xCoordinate = Integer.parseInt(req.getParameter(Constants.X_COORDINATE));
            int yCoordinate = Integer.parseInt(req.getParameter(Constants.Y_COORDINATE));
            float deliveryPPK = Float.parseFloat(req.getParameter(Constants.PPK));

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                Map<Integer, Float> itemIdToItemPriceInStore = getItemIdToItemPriceFromParameters(req.getParameterMap());

                try {
                    singleRegionManager.checkForFreeLocation(new Point(xCoordinate, yCoordinate));
                    singleRegionManager.checkForAvailableId(storeId, "Store");
                    if (itemIdToItemPriceInStore.isEmpty()) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("Please choose at least one item for sale before adding a new store.");
                    } else if (storeName.isEmpty()) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("Please enter at least one character for the store's name.");
                    } else if (!storeName.matches("[a-zA-Z0-9 ]+")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("Please enter only English letters and digits for the store's name.");
                    } else {
                        singleRegionManager.addNewStoreToRegion(storeId, ownerUsername, storeName, new Point(xCoordinate, yCoordinate), deliveryPPK, itemIdToItemPriceInStore);
                    }
                } catch (OccupiedLocationException e) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("The entered location is already occupied by a store.");
                } catch (TakenIdException e) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("The entered store ID is already taken.");
                }
            }

            out.flush();
        }
    }

    private Map<Integer, Float> getItemIdToItemPriceFromParameters(Map<String, String[]> parameterMap) {
        Map<Integer, Float> itemIdToItemPriceInStore = new HashMap<>();

        parameterMap.forEach((key, value) -> {
            if (key.contains(Constants.ITEM_ID) && !value[0].isEmpty()) {
                int itemId = Integer.parseInt(key.replace(Constants.ITEM_ID, "").trim());
                float itemPrice = Float.parseFloat(value[0]);
                itemIdToItemPriceInStore.put(itemId, itemPrice);
            }
        });

        return itemIdToItemPriceInStore;
    }
}
