package controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;


/**
 * @author obianuju akusoba */

/** MainScreen Controller FXML class*/
public class MainScreenController implements Initializable {

    Inventory inv;

    @FXML
    private TableView<Part>partTable;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInvColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TableView<Product>productTable;

    private ObservableList<Product> productInventory= FXCollections.observableArrayList();
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> partInventorySearch = FXCollections.observableArrayList();

    public MainScreenController(Inventory inv) {
        this.inv = inv;

    }

    /** This initializes the class controller.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateProductsTable();
        generatePartsTable();

    }
    /**This method populates the products table with items from inventory.*/
    private void generateProductsTable() {
        productInventory.setAll(inv.getAllProducts());
        productTable.getColumns();
        productTable.setItems(productInventory);
        productTable.refresh();

    }

    /**This method populates the parts table with items from inventory. Initially when I opened the application, I had a logical error where the tables did not load with the test data. This was because I named the cell property value in the Parts Table columns differently from how I named them in my Part class. I ensured my cell property values matched what I had in my Part class, and that resolved the issue.*/

    private void generatePartsTable() {
        partInventory.setAll(inv.getAllParts());
        partTable.getColumns().addAll();
        partTable.setItems(partInventory);
        partTable.refresh();
    }


    @FXML
    private AnchorPane MainScreen;

    @FXML
    private Label mainTitleLabel;

    @FXML
    private Button exitBtn;

    @FXML
    private TextField partSearchBox;

    @FXML
    private Button searchForPartButton;

    @FXML
    private Button addPartButton;

    @FXML
    private Button modifyPartButton;

    @FXML
    private Button deletePartsButton;

    @FXML
    private AnchorPane outerProductsBoxMain;

    @FXML
    private Label productsLabelMain;

    @FXML
    private TextField productSearchBox;

    @FXML
    private Button searchProductsButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button modifyProduct;

    @FXML
    private Button deleteProductButton;

    /**This method displays the Add Part form. I corrected a runtime error in my code by setting Add Part controller at runtime and removing it in scene builder during sign time because the runtime error showed that I had specified a controller twice.*/
    @FXML
    private void addPart(MouseEvent event) {
        try {
            System.out.println("addPart");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPart.fxml"));

            AddPartController controller = new AddPartController(inv);
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

    /**This method displays the Add Product form.*/
    @FXML
     private void addProduct(MouseEvent event) {
        try {
            System.out.println("addProduct.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProduct.fxml"));
            AddProductController controller = new AddProductController(inv);
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
    /**This method clears data input in a text field.*/
    @FXML
     void clearText(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (partSearchBox == field) {
            if(partInventory.size() != 0) {
                partTable.setItems(partInventory);
            }
        }
        if (productSearchBox == field) {
            if (productInventory.size() != 0) {
                productTable.setItems(productInventory);
            }
        }


    }
    /**This method deletes selected Parts from the Parts TableView.*/
    @FXML
   private void deletePart(MouseEvent event) {
        Part removePart = partTable.getSelectionModel().getSelectedItem();
        if(partInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if(!partInventory.isEmpty() && removePart == null) {
            errorWindow(2);
            return;
        }
        else{
            boolean confirm = confirmationWindow(removePart.getPartName());
            if(!confirm) {
                return;
            }
            inv.deletePart(removePart);
            partInventory.remove(removePart);
            partTable.refresh();

        }


    }
    /**This method deletes selected Products from the Products TableView.*/
    @FXML
    private void deleteProduct(MouseEvent event) {
        boolean deleted = false;
        Product removeProduct = productTable.getSelectionModel().getSelectedItem();
        if (productInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if(!productInventory.isEmpty() && removeProduct == null) {
            errorWindow(2);
            return;
        }
        if (removeProduct.getPartListSize() > 0) {
            boolean confirm = confirmDelete(removeProduct.getName());
            if(!confirm) {
                return;
            }
        }
        else {
            if( removeProduct != null) {
                infoWindow(1, removeProduct.getName());
                deleted = true;
                if(deleted) {
                    return;
                }
                else {
                    infoWindow(2, "");
                }
            }
        }
        inv.removeProduct(removeProduct.getProductId());
        productInventory.remove(removeProduct);
        productTable.setItems(productInventory);
        productTable.refresh();

    }
    /**This method closes the application.*/
    @FXML
    private void exitProgramButton(MouseEvent event) {
        Platform.exit();

    }
    /**This method displays the modify part form.*/
    @FXML
   private void modifyPart(MouseEvent event) {
        try {
            Part selected = partTable.getSelectionModel().getSelectedItem();
            if (partInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!partInventory.isEmpty() && selected == null) {
                errorWindow(2);
                return;
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inv, selected);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                // ModifyPartController c = (ModifyPartController)loader.getController();
                // c.setSelectPart(selected);
            }
        }

        catch (IOException e) {

            }


    }
    /**This method displays the modify product form.*/
    @FXML
    private void modifyProduct(MouseEvent event) {
        try {
            Product productSelected = productTable.getSelectionModel().getSelectedItem();
            if(productInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if(!productInventory.isEmpty() && productSelected == null) {
                errorWindow(2);
                return;
            }
            else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(inv, productSelected);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();

        }

    }

    /**This method displays matching results when users search for part by Id or name(full or partial name).*/
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!partSearchBox.getText().trim().isEmpty()) {
            partTable.setItems(inv.lookUpPart(partSearchBox.getText().trim()));
            partTable.refresh();
        }


    }
    /**This method displays matching results when users search for products by Id or name(full or partial name).*/
    @FXML
    private void searchForProduct(MouseEvent event) {
        if(!productSearchBox.getText().trim().isEmpty()) {
            productInventorySearch.clear();
            for (Product p: inv.getAllProducts()) {
                if (p.getName().contains(productSearchBox.getText().trim())) {
                    productInventorySearch.add(p);
                }
            }
            productTable.setItems(productInventorySearch);
            productTable.refresh();
        }

    }

    /**This method implements logical error checks.*/
    private void errorWindow(int code) {
        if(code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Inventory is empty!");
            alert.setContentText("No item to select!");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid option");
            alert.setContentText("Please select an item!");
            alert.showAndWait();
        }
    }

    /**This method confirms part delete actions.*/
    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Select ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }

    }

    /**This method confirms product delete actions.*/
    private boolean confirmDelete(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete product");
        alert.setHeaderText("Are you sure you want to delete:  "+ name + "?" + "This product still has parts assigned to it!");
        alert.setContentText("Select ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.OK) {
            return true;
        }

        else {
            return false;
        }
    }


    /**This method confirms successful delete actions.*/
    private void infoWindow (int code, String name) {
        if (code != 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(name + "has been deleted");
            alert.showAndWait();
        }
        else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error has occurred");

        }


    }





}

