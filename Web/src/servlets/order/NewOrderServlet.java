package servlets.order;

import engine.exceptions.IdenticalLocationsException;
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

@WebServlet(name = "NewOrderServlet", urlPatterns = {"/new-order"})
public class NewOrderServlet extends HttpServlet {

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");

            try {
                String regionName = SessionUtils.getRegionName(req);
                int xCoordinate = Integer.parseInt(req.getParameter(Constants.X_COORDINATE));
                int yCoordinate = Integer.parseInt(req.getParameter(Constants.Y_COORDINATE));
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(regionName);
                singleRegionManager.checkForValidOrderDestination(new Point(xCoordinate, yCoordinate));
                req.getSession(true).setAttribute(Constants.X_COORDINATE, req.getParameter(Constants.X_COORDINATE));
                req.getSession(true).setAttribute(Constants.Y_COORDINATE, req.getParameter(Constants.Y_COORDINATE));
                req.getSession(true).setAttribute(Constants.ORDER_DATE, req.getParameter(Constants.ORDER_DATE));
                req.getSession(true).setAttribute(Constants.ORDER_TYPE, req.getParameter(Constants.ORDER_TYPE));
                req.getSession(true).setAttribute(Constants.CHOSEN_STORE_ID, req.getParameter(Constants.CHOSEN_STORE_ID));
                out.print(Constants.CHOOSE_ITEMS_URL);
            } catch (IdenticalLocationsException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("The entered location is occupied by a store. Please enter a valid location.");
            }

            out.flush();
        }
    }
}
