package servlets.order;

import engine.enums.Rating;
import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddFeedbackToStoreServlet", urlPatterns = {"/add-feedback"})
public class AddFeedbackToStoreServlet extends HttpServlet {

    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) {
        SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
        String username = SessionUtils.getUsername(req);
        int rating = Integer.parseInt(req.getParameter(Constants.RATING));
        int storeId = Integer.parseInt(req.getParameter(Constants.STORE_ID));
        String feedbackText = req.getParameter(Constants.FEEDBACK_TEXT).trim();
        singleRegionManager.addFeedbackToStoreAfterOrder(username, storeId, feedbackText, Rating.values()[rating - 1]);
    }
}
