package controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static controller.AlertPrompt.confirmationWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import javafx.event.Event;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * @author obianuju akusoba */

/**
 * ModifyProduct FXML Controller class. */
public class ModifyProductController implements Initializable {

    Inventory inv;
    Product product;

    public ModifyProductController(Inventory inv, Product product){
        this.inv = inv;
        this.product = product;
    }



    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField count;

    @FXML
    private TextField max;

    @FXML
    private TextField price;

    @FXML
    private TextField min;

    @FXML
    private Button modifyProductSearchButton;

    @FXML
    private TextField search;
    private ObservableList<Part>partInventory = FXCollections.observableArrayList();
    private ObservableList<Part>partInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part>assocPartsList = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> partSearchTable;

    @FXML
    private TableColumn<?, ?> PartIdColumn;

    @FXML
    private TableColumn<?, ?> partNameColumn;

    @FXML
    private TableColumn<?, ?> partCountColumn;

    @FXML
    private TableColumn<?, ?> priceUnit;

    @FXML
    private Button modifyProductAddButton;

    @FXML
    private TableView<Part> assocPartsTable;

    @FXML
    private TableColumn<?, ?> assocPartIdColumn;

    @FXML
    private TableColumn<?, ?> assocPartNameColumn;

    @FXML
    private TableColumn<?, ?> assocPartCountColumn;

    @FXML
    private TableColumn<?, ?> assocPartPriceUnitColumn;

    @FXML
    private Button removeAssociatedPartButton;

    @FXML
    private Button modifyProductSaveButton;

    @FXML
    private Button modifyProductCancelButton;

    /**This method adds selected parts from the Parts Table to the associated Parts list and associated Parts Table.*/
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean duplicateItem = false;
        if(addPart == null) {
            return;
        }
        else{
            int id = addPart.getPartId();
            for(int p = 0; p < assocPartsList.size(); p++) {
                if (assocPartsList.get(p).getPartId() == id) {
                    AlertPrompt.errorProduct(2, null);
                    duplicateItem = true;
                }
            }
            if(!duplicateItem) {
                assocPartsList.add(addPart);
            }
            assocPartsTable.setItems(assocPartsList);


        }

    }



    /**This method exits the modify product form and redirect users to the main form.*/
    @FXML
    private void cancelModify(MouseEvent event) {
        boolean cancel = AlertPrompt.cancel();
        if(cancel) {
            mainScreen(event);
        }
        else {
            return;
        }

    }
    /**This method clears data input in a text field.*/
    @FXML
    void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (field == search) {
            partSearchTable.setItems(partInventory);
        }

    }
    /**This method searches for parts by ID or name(partial or full name).*/
    @FXML
    private void modifyProductSearch(MouseEvent event) {
       if(search.getText() != null && search.getText().trim().length() != 0){
           partInventorySearch.clear();
           for (Part p: inv.getAllParts()) {
               if(p.getPartName().contains(search.getText().trim())) {
                   partInventorySearch.add(p);
               }
           }
           partSearchTable.setItems(partInventorySearch);
       }


    }
    /**This method removes selected Parts from the associated Parts Table.*/
    @FXML
    private void removeAssociatedPart(MouseEvent event) {
        Part deleteSelectedPart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean removePart = false;
        if(deleteSelectedPart != null){
            boolean remove= confirmationWindow(deleteSelectedPart.getPartName());
            if(remove) {
                removePart = product.removeAssociatedPart(deleteSelectedPart.getPartId());
                assocPartsList.remove(deleteSelectedPart);
                assocPartsTable.refresh();
            }
        }
        else {
            return;
        }
        if(removePart) {
            AlertPrompt.infoWindow(1, deleteSelectedPart.getPartName());
        }
        else{
            AlertPrompt.infoWindow(2, "");
        }

    }
    /**This method implements Product input validation, saves product and redirect users to the main form.*/
    @FXML
    private void saveProduct(MouseEvent event) {
        resetFieldStyle();
        boolean complete = false;
        TextField [] fieldNum = {count,price,min,max};
        double miniCost = 0;
        for(int p = 0; p < assocPartsList.size(); p++) {
            miniCost+= assocPartsList.get(p).getPartPrice();
        }
        if(name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            AlertPrompt.errorProduct(4, name);
            return;
        }
        for(int p = 0; p <fieldNum.length; p++) {
            boolean valueErrortype = checkValue(fieldNum[p]);
            if (valueErrortype) {
                complete = true;
                break;
            }


            boolean typeError = checkType(fieldNum[p]);
            if(typeError) {
                complete = true;
                break;
            }
        }

        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            AlertPrompt.errorProduct(8, count);
            return;
        }
        if(Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertPrompt.errorProduct(10,min);
            return;
        }
        if(Integer.parseInt(max.getText().trim()) < Integer.parseInt(count.getText().trim())) {
            AlertPrompt.errorProduct(9,count);
            return;
        }
        if(miniCost > Double.parseDouble(price.getText().trim())) {
            AlertPrompt.errorProduct(6, price);
        }
        if(assocPartsList.size() == 0) {
            AlertPrompt.errorProduct(7, null);
            return;

        }

        saveProduct();
        mainScreen(event);


    }
    /**This method saves and updates products to inventory.*/
    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(min.getText().trim()),
                Integer.parseInt(max.getText().trim()),Integer.parseInt(count.getText().trim()));

        for(int p = 0; p < assocPartsList.size(); p++) {
            product.addAssociatedPart(assocPartsList.get(p));
        }
        inv.updateProduct(product);
    }



    /**This method initializes the controller class.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillSearchTable();
        declareData();

    }
    /**This method populates the Parts search table with items from inventory.*/
    private void fillSearchTable() {
        partInventory.setAll(inv.getAllParts());
        partSearchTable.setItems(partInventory);
        partSearchTable.refresh();

    }
    /**This method provides associated parts table with items from the associated parts list.*/
    private void declareData() {
        for (int p = 0; p < 1000; p++) {
            Part part = product.lookUpAssociatedPart(p);
            if(part != null) {
                assocPartsList.add(part);
            }

        }
        assocPartsTable.setItems(assocPartsList);
        this.name.setText(product.getName());
        this.id.setText(Integer.toString(product.getProductId()));
        this.count.setText(Integer.toString(product.getInStock()));
        this.price.setText(Double.toString(product.getPrice()));
        this.max.setText(Integer.toString(product.getMax()));
        this.min.setText((Integer.toString(product.getMin())));
    }

    /**This method styles Text fields with the white color.*/
    private void resetFieldStyle () {
        name.setStyle("-fx-border-color: white");
        count.setStyle("-fx-border-color: white");
        price.setStyle("-fx-border-color: white");
        min.setStyle("-fx-border-color: white");
        max.setStyle("-fx-border-color: white");
    }

    /**This method confirms the Delete action. */
    private boolean confirmationWindow (String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(" Part Delete");
        alert.setHeaderText("Delete " + name + "?");
        alert.setContentText("Select ok to confirm");

        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }

    /**This method redirects the user to the main form.*/
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
    private boolean checkType(TextField field) {
        if(!field.getText().trim().matches("\\d+(\\.\\d+)?") & field == price) {
            AlertPrompt.errorProduct(3, field);
            return true;

        }
        if(!field.getText().trim().matches("[0-9]*") & field != price) {
            AlertPrompt.errorProduct(3, field);
            return true;
        }
        return false;
    }

    /**This method implements input validation.*/
    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if(field.getText().trim() == null || field.getText().trim().isEmpty()){
                AlertPrompt.errorProduct(1, field);
                return true;
            }
            if (Double.parseDouble(field.getText().trim()) < 0 && field == price) {
                AlertPrompt.errorProduct(5, field);
                error = true;
            }
        }
        catch (Exception e) {
            error = true;
            AlertPrompt.errorProduct(3, field);
            e.printStackTrace();
        }
        return error;
    }



}
