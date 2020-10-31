package servlets.add;

import engine.managers.SDMRegionsManager;
import engine.managers.SingleRegionManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckIsUserRegionOwnerServlet", urlPatterns = {"/is-region-owner"})
public class CheckIsUserRegionOwnerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            boolean isUserRegionOwner;
            String username = SessionUtils.getUsername(req);

            synchronized (getServletContext()) {
                SingleRegionManager singleRegionManager = SDMRegionsManager.getInstance().getSingleRegionManagerByRegionName(SessionUtils.getRegionName(req));
                isUserRegionOwner = singleRegionManager.isUserRegionOwner(username);
            }

            out.print(isUserRegionOwner);
            out.flush();
        }
    }
}
