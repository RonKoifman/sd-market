package servlets;

import constants.Constants;
import engine.enums.TransactionType;
import engine.managers.SDMUsersManager;
import engine.managers.UsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "NewTransactionServlet", urlPatterns = {"/new-transaction"})
public class NewTransactionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            UsersManager usersManager = SDMUsersManager.getInstance();
            String transactionAmountFromParameter = req.getParameter(Constants.TRANSACTION_AMOUNT);
            String transactionDateFromParameter = req.getParameter(Constants.TRANSACTION_DATE);
            String transactionTypeFromParameter = req.getParameter(Constants.TRANSACTION_TYPE);
            String username = SessionUtils.getUsername(req);

            try {
                float transactionAmount = Float.parseFloat(transactionAmountFromParameter);
                LocalDate transactionDate = LocalDate.parse(transactionDateFromParameter);
                usersManager.addNewTransactionToUser(username, transactionAmount, transactionDate, TransactionType.valueOf(transactionTypeFromParameter.toUpperCase()));
                out.println("A deposit of $" + transactionAmountFromParameter + " was made successfully.");
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
