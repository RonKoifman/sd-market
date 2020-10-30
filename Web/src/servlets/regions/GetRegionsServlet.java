package servlets.regions;

import com.google.gson.Gson;
import dto.models.RegionDTO;
import engine.managers.SDMRegionsManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetRegionsServlet", urlPatterns = {"/regions"})
public class GetRegionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Gson gson = new Gson();
            Collection<RegionDTO> regions;

            synchronized (getServletContext()) {
                regions = SDMRegionsManager.getInstance().getAllRegions();
            }

            String json = gson.toJson(regions);
            out.print(json);
            out.flush();
        }
    }
}
