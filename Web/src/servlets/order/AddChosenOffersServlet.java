package servlets.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.models.DiscountOfferDTO;
import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "AddChosenOffersServlet", urlPatterns = {"/add-chosen-offers"})
public class AddChosenOffersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) {
        Gson gson = new Gson();
        String offersJsonString = req.getParameter(Constants.CHOSEN_OFFERS);
        Type type = new TypeToken<List<DiscountOfferDTO>>(){}.getType();
        List<DiscountOfferDTO> chosenOffers = gson.fromJson(offersJsonString, type);

        synchronized (getServletContext()){
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
            singleRegionManager.addChosenDiscountOffersToPendingOrderByUsername(SessionUtils.getUsername(req), chosenOffers);
        }
    }
}
