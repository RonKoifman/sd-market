package engine.models.notification;

import java.util.Objects;

public class Notification {

    private final String title;
    private final String text;

    public Notification(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
