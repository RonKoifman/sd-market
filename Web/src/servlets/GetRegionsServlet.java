package servlets;

import com.google.gson.Gson;
import dto.models.RegionDTO;
import engine.managers.SDMRegionsManager;
import engine.managers.RegionsManager;

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
            RegionsManager regionsManager = SDMRegionsManager.getInstance();
            Gson gson = new Gson();
            Collection<RegionDTO> regions = regionsManager.getAllRegions();
            String json = gson.toJson(regions);
            out.println(json);
            out.flush();
        }
    }
}
