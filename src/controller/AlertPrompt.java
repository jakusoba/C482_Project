package controller;

import javafx.scene.control.TextField;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;

/**@author obianuju akusoba */

/**This class performs input validation and logical error checks.*/
public class AlertPrompt {

    /**This method performs logical error checks on parts.*/
    public static void errorPart(int code, TextField field) {
        fieldError(field);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error while adding part");
        alert.setHeaderText("Unable to add Part");
        switch (code) {
            case 1: {
                alert.setContentText("empty field!");
                break;
            }
            case 2: {
                alert.setContentText("Please set In house or OutSourced category!");
                break;
            }
            case 3: {
                alert.setContentText("Wrong format!");
                break;
            }
            case 4: {
                alert.setContentText("Invalid Name format!");
                break;
            }
            case 5: {
                alert.setContentText("Number can not be zero or negative, please enter a positive number.");
                break;
            }
            case 6: {
                alert.setContentText("Inventory can not be lower than min.");
                break;
            }
            case 7: {
                alert.setContentText("Inventory can not be higher than Max.");
                break;
            }
            case 8: {
                alert.setContentText("Max must be greater than min!");
                break;
            }
            case 9: {
                alert.setContentText("Machine Id value must be numeric!");
                break;
            }
            default: {
                alert.setContentText("UnknownError.");
                break;
            }
        }
        alert.showAndWait();
    }

    /**This method performs logical error checks on products.*/
    public static void errorProduct(int code, TextField field) {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error while adding product.");
        alert.setHeaderText("Unable to add product.");
        switch (code) {
            case 1: {
                alert.setContentText("empty field!");
                break;
            }
            case 2: {
                alert.setContentText("This Part is associated with this product!");
                break;
            }
            case 3: {
                alert.setContentText("Invalid format!");
                break;
            }
            case 4: {
                alert.setContentText("Invalid Name!");
                break;
            }
            case 5: {
                alert.setContentText("Number can not be zero or negative, please enter a positive number.");
                break;
            }
            case 6: {
                alert.setContentText("Cost of product can not be lower than it's part.");
                break;
            }
            case 7: {
                alert.setContentText("Product must have a minimum of one part!");
                break;
            }
            case 8: {
                alert.setContentText("Inventory can not be lower than min.");
                break;
            }
            case 9: {
                alert.setContentText("Inventory can not be higher than Max.");
                break;
            }
            case 10: {
                alert.setContentText("Max must be greater than min!");
                break;
            }
            default: {
                alert.setContentText("Unknown error.");
                break;
            }

        }
    }

    /**This method performs input validation on text fields.*/
    private static void fieldError(TextField field) {
        if (field != null) {
            field.setStyle("-fx-border-color: red");

        }

    }

    /**This method confirms delete Parts action.*/
    public static boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure you want delete: " + name);
        alert.setContentText("Click ok to confirm.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**This method confirms cancel action.*/
    public static boolean cancel () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setHeaderText("Click ok to confirm.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

     /**This method confirms successful delete actions.*/
    public static void infoWindow(int code, String name) {
        if (code != 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(name + "has been deleted.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred.");
        }
    }

}
