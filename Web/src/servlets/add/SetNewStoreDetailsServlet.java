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

@WebServlet(name = "SetNewStoreDetailsServlet", urlPatterns = {"/set-store-details"})
public class SetNewStoreDetailsServlet extends HttpServlet {

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String regionName = SessionUtils.getRegionName(req);
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(regionName);

            try {
                int storeId = Integer.parseInt(req.getParameter(Constants.STORE_ID));
                String storeName = req.getParameter(Constants.STORE_NAME).trim();
                int xCoordinate = Integer.parseInt(req.getParameter(Constants.X_COORDINATE));
                int yCoordinate = Integer.parseInt(req.getParameter(Constants.Y_COORDINATE));

                if (storeName.isEmpty()) {
                    throw new IllegalArgumentException("Please enter at least one character for the store's name.");
                } else if (!storeName.matches("[a-zA-Z0-9 ]+")) {
                    throw new IllegalArgumentException("Please enter only English letters and digits for the store's name.");
                }

                singleRegionManager.checkForFreeLocation(new Point(xCoordinate, yCoordinate));
                singleRegionManager.checkForAvailableId(storeId, "Store");
                req.getSession(true).setAttribute(Constants.STORE_ID, req.getParameter(Constants.STORE_ID));
                req.getSession(true).setAttribute(Constants.STORE_NAME, req.getParameter(Constants.STORE_NAME));
                req.getSession(true).setAttribute(Constants.X_COORDINATE, req.getParameter(Constants.X_COORDINATE));
                req.getSession(true).setAttribute(Constants.Y_COORDINATE, req.getParameter(Constants.Y_COORDINATE));
                req.getSession(true).setAttribute(Constants.PPK, req.getParameter(Constants.PPK));
                out.print(Constants.CHOOSE_STORE_ITEMS_URL);
            } catch (OccupiedLocationException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("The entered location is already occupied by a store.");
            } catch (TakenIdException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("The entered store ID is already taken.");
            } catch (IllegalArgumentException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(e.getMessage());
            }

            out.flush();
        }
    }
}
