package controller;





import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import static controller.AlertPrompt.cancel;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** @author obianuju akusoba */


/** ModifyPart FXML Controller Class.*/
public class ModifyPartController implements Initializable {

    Inventory inv;
    Part part;

    public ModifyPartController(Inventory inv, Part part) {
        this.inv = inv;
        this.part = part;

    }

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private ToggleGroup source;

    @FXML
    private RadioButton OutSourcedRadio;

    @FXML
    private TextField company;

    @FXML
    private TextField Max;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField count;

    @FXML
    private  TextField price;

    @FXML
    private TextField min;

    @FXML
    private TextField max;


    @FXML
    private Label companyLabel;

    @FXML
    private Button modifyPartSaveButton;

    @FXML
    private Button cancelButton;

    /**This method exits the modify part form and redirects users to the main form.*/
    @FXML
    private void cancelModifyPart(MouseEvent event) {
        boolean cancel = cancel();
        if(cancel) {
            mainScreen(event);
        }
        else {
            return;
        }

    }
    /**This method clears data input in a text field.*/
    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }
    /**This method switches the "company" label to "machine Id".*/
    @FXML
    private void clickInHouse(MouseEvent event) {
        companyLabel.setText("Machine Id");

    }

    /**This method switches the "company" label to "company name".*/
    @FXML
    private void clickOutsourced(MouseEvent event) {
        companyLabel.setText("Company Name");

    }
    /**This method disables the Part Id text field.*/
    @FXML
    void idDisabled(MouseEvent event) {

    }
    /**This method saves modifications to the part and redirects users to the main form.*/
    @FXML
    private void saveModifyPart(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count,price,min,max };
        if(inHouseRadio.isSelected() || OutSourcedRadio.isSelected()) {
            for (int p = 0; p < fieldCount.length; p++) {
                boolean valueError = checkValue(fieldCount[p]);
                if(valueError) {
                    end = true;
                    break;
                }
                boolean typeError = checkType(fieldCount[p]);
                if(typeError) {
                    end = true;
                    break;

                }
            }
            if(name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")){
                AlertPrompt.errorPart(4,name);
                return;

            }
            if(Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertPrompt.errorPart(8, min);
                return;
            }
            if(Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                AlertPrompt.errorPart(6, count);
                return;
            }
            if(Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertPrompt.errorPart(7, count);
                return;
            }
            if(end) {
                return;
            }

            else if(OutSourcedRadio.isSelected() && company.getText().trim().isEmpty()) {
                AlertPrompt.errorPart(1, company);
                return;
            }
            else if(inHouseRadio.isSelected() && !company.getText().trim().matches("[1-9]*")) {
                AlertPrompt.errorPart(9, company);
                return;
            }
            else if(inHouseRadio.isSelected() & part instanceof InHouse) {
                updateInHouseitem();
            }
            else if(inHouseRadio.isSelected() & part instanceof Outsourced) {
                updateInHouseitem();
            }
            else if(OutSourcedRadio.isSelected() & part instanceof Outsourced) {
                updateOutSourcedItem();
            }
            else if (OutSourcedRadio.isSelected() & part instanceof InHouse) {
                updateOutSourcedItem();
            }
        }
        else {
            AlertPrompt.errorPart(2, null);
            return;
        }
        mainScreen(event);

    }
    /**This method updates inventory with inHouse parts.*/
    private void updateInHouseitem() {
        inv.updatePart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()),Integer.parseInt(company.getText().trim())));
    }

    /**This method updates inventory with Outsourced parts.*/
    private void updateOutSourcedItem() {
        inv.updatePart(new Outsourced(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));
    }

    /**This method styles Text fields with the white color.*/
    private void resetFieldsStyle () {
        name.setStyle("-fx-border-color: white");
        max.setStyle("-fx-border-color: white");
        count.setStyle("-fx-border-color: white");
        price.setStyle("-fx-border-color: white");
        min.setStyle("-fx-border-color: white");
        company.setStyle("-fx-border-color: white");
    }


    /**This method loads the main form.*/
    private void mainScreen (MouseEvent event) {
        try {
            System.out.println("ModifyPart");

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
    private boolean checkValue (TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                AlertPrompt.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) <= 0.0) {
                AlertPrompt.errorPart(1, field);
                error = true;
            }
        }
         catch (Exception e) {
             error = true;
             AlertPrompt.errorPart(3, field);
             e.printStackTrace();

         }
         return error;
    }

    /**This method implements logical error check.*/
    private boolean checkType (TextField field) {
        if(field == price & !field.getText().trim().matches("\\d+(\\.\\d+?)")) {
            AlertPrompt.errorPart(3, field);
            return true;
        }
        if(field != price & ! field.getText().trim().matches("[0-9]")) {
            AlertPrompt.errorProduct(3, field);
            return true;
        }
        return false;
    }

    /**This method exits the modify part form and redirect users to the main form.*/
    private boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Confirm that you want to cancel");
        alert.setContentText("Select ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }

    }



    /**This method initializes the controller class.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        declareData();
    }

    /**This method populates data for inHouse and Outsourced parts.*/
    private void declareData () {
        if (part instanceof InHouse) {
            InHouse partA = (InHouse) part;
            inHouseRadio.setSelected(true);
            companyLabel.setText("Machine Id");
            this.name.setText(partA.getPartName());
            this.id.setText((Integer.toString(partA.getPartId())));
            this.count.setText((Integer.toString(partA.getPartInStock())));
            this.price.setText((Double.toString(partA.getPartPrice())));
            this.min.setText((Integer.toString(partA.getMin())));
            this.max.setText((Integer.toString(partA.getMax())));
            this.company.setText((Integer.toString(partA.getMachineId())));
        }

        if(part instanceof Outsourced) {
            Outsourced partB = (Outsourced) part;
            OutSourcedRadio.setSelected(true);
            companyLabel.setText("Company Name");
            this.name.setText(partB.getPartName());
            this.id.setText((Integer.toString(partB.getPartId())));
            this.count.setText((Integer.toString(partB.getPartInStock())));
            this.price.setText((Double.toString(partB.getPartPrice())));
            this.min.setText((Integer.toString(partB.getMin())));
            this.max.setText((Integer.toString(partB.getMax())));
            this.company.setText(partB.getCompanyName());

        }

    }
}

