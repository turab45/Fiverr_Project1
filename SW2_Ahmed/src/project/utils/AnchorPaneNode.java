package project.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;

    public AnchorPaneNode(Node... children) {
        super(children);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
