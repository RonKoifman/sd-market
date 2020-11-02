package servlets.user;

import com.google.gson.Gson;
import dto.models.StoreDTO;
import engine.managers.SDMUsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetStoreOwnerOwnedStoresServlet", urlPatterns = {"/owner-owned-stores"})
public class GetStoreOwnerOwnedStoresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            String username = SessionUtils.getUsername(req);
            String regionName = SessionUtils.getRegionName(req);
            Collection<StoreDTO> ownedStores;

            synchronized (getServletContext()) {
                ownedStores = SDMUsersManager.getInstance().getStoreOwnerOwnedStoresByRegionName(username, regionName);
            }

            Gson gson = new Gson();
            String json = gson.toJson(ownedStores);
            out.print(json);
            out.flush();
        }
    }
}
