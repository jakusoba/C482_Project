package controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**@author obianuju akusoba*/

/** AddProduct FXML Controller class*/
public class AddProductController implements Initializable {
    Inventory inv;

    public AddProductController (Inventory inv){
        this.inv = inv;

    }

    @FXML
    private TableView<Part> partSearchTable;

    @FXML
    private TableColumn<?, ?> partIdColumn;

    @FXML
    private TableColumn<?, ?> partNameColumn;

    @FXML
    private TableColumn<?, ?> partInvLevel;

    @FXML
    private TableColumn<?, ?> priceUnit;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private Label count;

    @FXML
    private TextField price;

    @FXML
    private TextField max;

    @FXML
    private TextField min;

    @FXML
    private TextField Inv;

    @FXML
    private TextField search;

    @FXML
    private Button addProductSearchButton;

    @FXML
    private Button addProductAddButton;

    @FXML
    private TableView<Part> assocPartTable;
    private ObservableList<Part>partInventory = FXCollections.observableArrayList();
    private ObservableList<Part>partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part>assocPartList = FXCollections.observableArrayList();


    @FXML
    private TableColumn<?, ?> assocPartIdColumn;

    @FXML
    private TableColumn<?, ?> assocPartNameColumn;

    @FXML
    private TableColumn<?, ?> assocPartCountColumn;

    @FXML
    private TableColumn<?, ?> assocPartPriceUnitColumn;

    @FXML
    private Button addProductCancelButton;

    @FXML
    private Button addProductSaveButton;

    @FXML
    private Button removeAssociatedPartButton;

    /**This method takes a selected part from the top table(parts table) and adds it to the bottom table(associated parts table).*/
   @FXML
   private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean duplicatedItem = false;

        if (addPart != null) {
            int id =  addPart.getPartId();
            for(int p = 0; p < assocPartList.size(); p++) {
                if (assocPartList.get(p).getPartId() == id) {
                    AlertPrompt.errorProduct(2, null);
                    duplicatedItem = true;

                }
            }

            if (!duplicatedItem) {
                assocPartList.add(addPart);
            }

            assocPartTable.setItems(assocPartList);
        }

    }
    /**This method exits the add product form and redirects the users to the main form.*/
    @FXML
    private void cancelAddProduct(MouseEvent event) {
        boolean cancelProduct = AlertPrompt.cancel();
        if (cancelProduct) {
            mainScreen(event);
        }

    }

    /**This method clears data input in a field.*/
    @FXML
    void clearField(MouseEvent event) {
        search.setText("");
        if (!partInventory.isEmpty()) {
            partSearchTable.setItems(partInventory);
        }

    }

    /**This method loads the main form.*/
    private void mainScreen (Event event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_screen.fxml"));
            MainScreenController controller = new MainScreenController(inv);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**This method implements logical error check.*/
    private boolean checkType (TextField field) {
        if(field == price && !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertPrompt.errorProduct(3,field);
            return true;
        }

        if(field != price && !field.getText().trim().matches("[0-9]*")) {
            AlertPrompt.errorProduct(3,field);
            return true;
        }

        return false;

    }

    /****This method implements input validation.*/
    private boolean checkValue (TextField field) {
        boolean error = false;
        try{
            if(field.getText().trim().isEmpty() || field.getText().trim() == null) {
                AlertPrompt.errorProduct(1, field);
                return true;
            }
            if(field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertPrompt.errorProduct(5,field);
                error = true;

            }

        }
        catch (NumberFormatException e) {
            error = true;
            AlertPrompt.errorProduct(3, field);
            System.out.println(e);
        }
        return error;

    }


    /**This method clears data input in a field.*/
    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");

    }
    /**This method dissociates or removes a part from a product.*/
    @FXML
    private void removeAssociatedPart(MouseEvent event) {
        Part removeAssocPart = assocPartTable.getSelectionModel().getSelectedItem();
        boolean removed = false;
        if(removeAssocPart != null) {
            boolean delete = AlertPrompt.confirmationWindow(removeAssocPart.getPartName());
            if (delete) {
                assocPartList.remove(removeAssocPart);
                assocPartTable.refresh();
            }

        } else {
            return;

        }
        if (removed) {
            AlertPrompt.infoWindow(1, removeAssocPart.getPartName());
        }
        else {
            AlertPrompt.infoWindow(2, "");
        }


    }
    /**This method redirects users to the main form after after saving data.*/
    @FXML
    private void saveAddProduct(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {Inv,price, min, max};
        double minCost = 0;
        for (int p = 0; p < assocPartList.size(); p++) {
            minCost += assocPartList.get(p).getPartPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name ")) {
            AlertPrompt.errorProduct(4, name);
            return;
        }
        for (TextField fieldCount1 : fieldCount) {
            boolean valueError = checkValue(fieldCount1);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount1);
            if (typeError) {
                end = true;
                break;
            }


        }
        if(Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertPrompt.errorProduct(10,min);
        }
        if(Integer.parseInt(Inv.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            AlertPrompt.errorProduct(8, Inv);
        }

        if(Integer.parseInt(Inv.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertPrompt.errorProduct(9, Inv);
        }

        if (Double.parseDouble(price.getText().trim()) < minCost) {
            AlertPrompt.errorProduct(6, price);
        }

        if(assocPartList.isEmpty()) {
            AlertPrompt.errorProduct(7, null);
            return;
        }
        saveAddedProduct();
        mainScreen(event);

    }
    /**This method implements input validation.*/
    private void fieldError(TextField field) {
        if(field != null) {
            field.setStyle("-fx-border-color: red");
        }
    }

    /**This method saves the added product data to inventory.*/
    private void saveAddedProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(),Double.parseDouble(price.getText().trim()),
                Integer.parseInt(Inv.getText().trim()), Integer.parseInt(min.getText().trim()),Integer.parseInt(max.getText().trim()));
        for (int p = 0; p < assocPartList.size(); p++) {
            product.addAssociatedPart(assocPartList.get(p));
        }
        inv.addProduct(product);
    }

    /**This method styles Text fields with the white color.*/
    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: white");
        Inv.setStyle("-fx-border-color: white");
        price.setStyle("-fx-border-color: white");
        min.setStyle("-fx-border-color: white");
        max.setStyle("-fx-border-color: white");
    }
    /**This method searches for part by Id or name(partial or full name).*/
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!search.getText().trim().isEmpty()) {
            partInventory.clear();
            partSearchTable.setItems(inv.lookUpPart(search.getText().trim()));
            partSearchTable.refresh();
        }

    }
    /**Initializes the controller class*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        provideProductId();
        fillSearchTable();

    }
    /**This method auto generates a unique product Id.*/
    private void provideProductId() {
        boolean match;
        Random ranNumber = new Random();
        Integer num = ranNumber.nextInt(2000);

        if (inv.productListSize()==0) {
            id.setText(num.toString());
        }
        else {
            match = provideNum(num);

            if (match == false) {
                id.setText(num.toString());

            }
            else {
                provideProductId();

            }
        }
        id.setText(num.toString());
    }
    /**This method checks if the generated product Id is a good match.*/
    private boolean provideNum (Integer num) {
        Part match = inv.lookUpPart(num);
        return match != null;

    }

    /**This method populates parts search table with items from inventory.*/
    private void fillSearchTable() {
        partInventory.setAll(inv.getAllParts());

        partSearchTable.setItems(partInventory);
        partSearchTable.refresh();

    }


















}
