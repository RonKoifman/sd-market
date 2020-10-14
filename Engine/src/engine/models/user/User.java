package engine.models.user;

import dto.models.UserDTO;
import engine.enums.UserRole;
import engine.interfaces.Identifiable;
import engine.models.account.Account;
import engine.models.account.Transaction;

import java.util.Objects;

public abstract class User implements Identifiable {

    protected final int id;
    protected final String username;
    protected final UserRole userRole;
    protected final Account account = new Account();
    private static int idGenerator;

    public User(String username, UserRole userRole) {
        this.id = ++idGenerator;
        this.username = username;
        this.userRole = userRole;
    }

    public abstract UserDTO toUserDTO();

    public void addNewTransaction(Transaction newTransaction) {
        account.addNewTransaction(newTransaction);
    }

    public float getAccountBalance() {
        return account.getBalance();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userRole=" + userRole +
                ", account=" + account +
                '}';
    }
}
