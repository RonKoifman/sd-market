package servlets;

import constants.Constants;
import engine.managers.SDMAccountsManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "NewDepositTransactionServlet", urlPatterns = {"/new-deposit-transaction"})
public class NewDepositTransactionServlet extends HttpServlet {

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String transactionAmountFromParameter = req.getParameter(Constants.TRANSACTION_AMOUNT);
            String transactionDateFromParameter = req.getParameter(Constants.TRANSACTION_DATE);
            String username = SessionUtils.getUsername(req);

            try {
                float transactionAmount = Float.parseFloat(transactionAmountFromParameter);
                LocalDate transactionDate = LocalDate.parse(transactionDateFromParameter);
                SDMAccountsManager.getInstance().deposit(username, transactionAmount, transactionDate);
                out.println("A deposit of $" + String.format("%.2f", transactionAmount) + " was made successfully.");
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            out.flush();
        }
    }
}
