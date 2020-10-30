package servlets.file;

import engine.managers.SDMRegionsManager;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet(name = "UploadFileServlet", urlPatterns = {"/upload-file"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String GENERAL_ERROR_MESSAGE = "General failure occurred while loading your file. Make sure you've entered a valid XML file.";
            String username = SessionUtils.getUsername(req);

            Part filePart = req.getPart("file-key");
            InputStream fileInputStream = filePart.getInputStream();
            String fileType = filePart.getContentType();

            if (!fileType.contains("xml")) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("Invalid file. Please choose only XML file type.");
            } else {
                try {
                    synchronized (getServletContext()) {
                        SDMRegionsManager.getInstance().loadNewRegionDataFromFile(username, fileInputStream);
                        out.print("File uploaded successfully!");
                    }
                } catch (JAXBException e) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("Invalid file. " + GENERAL_ERROR_MESSAGE);
                } catch (Exception e) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String errorMessage = e.getMessage() == null ? GENERAL_ERROR_MESSAGE : e.getMessage();
                    out.print("Invalid file. " + errorMessage);
                }
            }

            out.flush();
        }
    }
}
