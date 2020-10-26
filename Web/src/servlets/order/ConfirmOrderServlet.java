package servlets.order;

import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ConfirmOrderServlet", urlPatterns = {"/confirm-order"})
public class ConfirmOrderServlet extends HttpServlet {

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) {
        SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
        singleRegionManager.confirmPendingOrderByUsername(SessionUtils.getUsername(req));
    }
}
