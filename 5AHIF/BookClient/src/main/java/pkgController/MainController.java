package pkgController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pkgData.Book;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainController implements Initializable {

    @FXML
    public Button btnConnection;
    @FXML
    public Button btnGetBook;
    @FXML
    public Button btnGetAllBooks;
    @FXML
    public Button btnAddBook;
    @FXML
    public Button btnUpdateBook;
    @FXML
    public Label lblMessage;
    @FXML
    public TextField txtUri;
    @FXML
    public TextArea txtBookDetails;
    @FXML
    public TextField txtId;
    @FXML
    public TextField txtAuthor;
    @FXML
    public TextField txtTitle;
    @FXML
    public TextField txtImage;
    public TableView<Book> tableBooks;
    public TableColumn<Book, Integer> colId;
    public TableColumn<Book, String> colTitle;
    public TableColumn<Book, String> colAuthor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
    }

    private RESTController restController;

    @FXML
    public void onBtnClick(ActionEvent actionEvent) {
        try {
            if (actionEvent.getSource().equals(btnConnection)) {
                restController = new RESTController(txtUri.getText());
            } else if (actionEvent.getSource().equals(btnGetBook)) {
                Book b = restController.getBook(txtId.getText());
                txtId.setText(Integer.toString(b.getId()));
                txtAuthor.setText(b.getAuthor());
                txtTitle.setText(b.getTitle());

                lblMessage.setText("Got book: " + b);
            } else if (actionEvent.getSource().equals(btnGetAllBooks)) {
                ArrayList<Book> collBooks = restController.getBooks();

                tableBooks.getItems().clear();
                tableBooks.getItems().addAll(collBooks);
                lblMessage.setText("Got books: " + collBooks);
            } else if (actionEvent.getSource().equals(btnAddBook)) {
                Book b = new Book(Integer.parseInt(txtId.getText()), txtTitle.getText(), txtAuthor.getText());
                String status = restController.addBook(b);
                btnGetAllBooks.fire();
                lblMessage.setText(status);
            } else if (actionEvent.getSource().equals(btnUpdateBook)) {
                Book b = new Book(Integer.parseInt(txtId.getText()), txtTitle.getText(), txtAuthor.getText());
                String status = restController.updateBook(b);
                btnGetAllBooks.fire();
                lblMessage.setText(status);
            }
        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
        }
    }

    @FXML
    public void onSelectBook(MouseEvent mouseEvent) {
        Book b = tableBooks.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(b.getId()));
        txtTitle.setText(b.getTitle());
        txtAuthor.setText(b.getAuthor());
    }
}
