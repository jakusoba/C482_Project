package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Inventory;
import model.InHouse;
import model.Outsourced;
import model.Part;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.io.IOException;

/** @author obianuju akusoba */

/** This is the AddPart FXML Controller class. */

public class AddPartController implements Initializable {

    Inventory inv;

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private ToggleGroup addPartToggleGroup;

    @FXML
    private RadioButton outSourcedRadio;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField Inv;

    @FXML
    private TextField price;

    @FXML
    private TextField max;

    @FXML
    private TextField company;

    @FXML
    private Label companyLabel;

    @FXML
    private TextField min;

    @FXML
    private Button cancelAddPartButton;

    @FXML
    private Button saveButton;

    public AddPartController (Inventory inv) {
        this.inv = inv;
    }

    /** This methods exits the add part form and redirect users to the main form.*/
    @FXML
    private void cancelAddPart(MouseEvent event) {
        boolean cancel = AlertPrompt.cancel();
        if (cancel) {
            main_screen(event);
        }

    }
    /**This method clears data input in a text field.*/
    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");

    }
    /**This method switches the "company" label to "machine Id" when the user selects the InHouse radio button.*/
    @FXML
    private void clickInHouse(MouseEvent event) {
        companyLabel.setText("Machine Id");
        company.setText("Machine Id");

    }
    /**This method switches the "company" label to "Company Name" when the user selects the Outsourced radio button.*/
    @FXML
    private void clickedOutSourced(MouseEvent event) {
        companyLabel.setText("Company Name");
        company.setText("Company Name");

    }
    /**This method disables the part Id text field.*/
    @FXML
    private void idDisabled(MouseEvent event) {

    }
    /**This method redirects users to the main form after after saving data.*/
    @FXML
    private void saveAddPart(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {Inv, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
            for (TextField fieldCount1 : fieldCount) {
                boolean valueError = checkValue(fieldCount1);
                if(valueError) {
                    end = true;
                    break;
                }
                boolean typeError = checkType(fieldCount1);
                if(typeError) {
                    end = true;
                    break;
                }
            }
            if(name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
                AlertPrompt.errorPart(4, name);
                return;
            }
            if(Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
               AlertPrompt.errorPart(8, min);
               return;
            }
            if(Integer.parseInt(Inv.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                AlertPrompt.errorPart(6, Inv);
                return;
            }
            if(Integer.parseInt(Inv.getText().trim()) < Integer.parseInt(max.getText().trim())) {
                AlertPrompt.errorPart(7, Inv);
                return;
            }
            if(end) {
                return;
            }
            else if (company.getText().trim().isEmpty() || company.getText().trim().toLowerCase().equals("company name")) {
                AlertPrompt.errorPart(3, company);
                return;
            }
            else if (inHouseRadio.isSelected() && !company.getText().trim().matches("[0-9]*")) {
                AlertPrompt.errorPart(9, company);
                return;
            }
            else if (inHouseRadio.isSelected()) {
                addInHouse();
            }
            else if(outSourcedRadio.isSelected()) {
                addOutsourced();
            }

        }
        else {
            AlertPrompt.errorPart(2, null);
            return;
        }

        main_screen(event);

    }

    /**This method saves the added inHouse parts data to inventory.*/
    private void addInHouse() {
        inv.addPart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(Inv.getText().trim()),
                Integer.parseInt(min.getText().trim()),Integer.parseInt(max.getText().trim()), (Integer.parseInt(company.getText().trim()))));

    }
    /**This method saves the added Outsourced parts data to inventory.*/
    private void addOutsourced() {
        inv.addPart(new Outsourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(Inv.getText().trim()),
                Integer.parseInt(min.getText().trim()),Integer.parseInt(max.getText().trim()), company.getText().trim()));

    }

    /**This method styles Text fields with the white color.*/
    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        Inv.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");
        company.setStyle("-fx-border-color: lightgray");
    }

    /**This method loads the main form.*/
    private void main_screen(Event event) {
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

    /****This method implements input validation.*/
    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                AlertPrompt.errorPart(1, field);
                return true;

            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertPrompt.errorPart(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            AlertPrompt.errorPart(3, field);
            System.out.print(e);

        }
        return error;

    }


    /**This method implements logical error check.*/
    private boolean checkType(TextField field) {
        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertPrompt.errorPart(3, field);
            return true;
        }
        if(field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertPrompt.errorPart(3,field);
            return true;
        }
        return false;

    }


    /**
     * Initializes the controller class.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePartId();
        resetFields();

    }
    /**This method sets Text fields with data from parts class.*/
    private void resetFields() {
        name.setText("Part Name");
        Inv.setText("Inv Count");
        price.setText("Part Price");
        min.setText("Min");
        max.setText("Max");
        company.setText("Machine Id");
        companyLabel.setText("Machine Id");
        inHouseRadio.setSelected(true);

    }

    /**This method auto generates a unique parts Id.*/
    private void generatePartId() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(800);

        if (inv.partListSize() == 0) {
            id.setText(num.toString());
        }
        if (inv.partListSize() == 800) {
            AlertPrompt.errorPart(3, null);
        } else {
            match = verifyIfTaken(num);
            if (match == false) {
                id.setText(num.toString());
            } else {
                generatePartId();

            }
        }
    }

    /**This method checks if the auto generated part Id is a good match.*/
    private boolean verifyIfTaken(Integer num) {
        Part match = inv.lookUpPart(num);
        return match != null;
    }

}


