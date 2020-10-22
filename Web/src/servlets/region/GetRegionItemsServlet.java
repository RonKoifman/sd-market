package servlets.region;

import com.google.gson.Gson;
import dto.models.RegionItemDTO;
import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetRegionItemsServlet", urlPatterns = {"/region-items"})
public class GetRegionItemsServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
            Collection<RegionItemDTO> items = singleRegionManager.getAllItemsInRegion();
            String json = gson.toJson(items);
            out.print(json);
            out.flush();
        }
    }
}
