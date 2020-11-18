package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * @author obianuju akusoba */

 /**This class is my main program, it extends from the javafx application. If I was to update my application to the next version, one compatible feature that would extend functionality is adding a login screen which requires users to have a username and password for authentication. The second compatible feature would be incorporating a database or Backend as a service for persistent data*/

public class InventoryProgram extends Application {

    /**
     * @param args the command line arguments**/
    public static void main(String[] args) {
        launch(args);
    }

    /*This method executes the main form as soon as the application starts**/
    @Override
    public void start(Stage stage) throws Exception {

        Inventory inv = new Inventory();
        addTestData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_screen.fxml"));
        controller.MainScreenController controller = new controller.MainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /*This method adds sample test data to Inventory.**/
    void addTestData(Inventory inv) {
        //Add In House Parts.
        Part a1 = new InHouse (1, "Part A1",  3.45, 9, 6, 100, 101);
        Part a2 = new InHouse(3, "PartA2", 5.45, 10, 6, 90, 103 );
        Part b = new InHouse(2, "Part B", 4.45, 11, 6, 100, 102);
        inv.addPart(a1);
        inv.addPart(b);
        inv.addPart(a2);
        inv.addPart(new InHouse(4, "Part A3", 5.45, 14, 7, 100, 104));
        inv.addPart(new InHouse(5, "Part A4", 6.45, 8, 8, 100, 105));

        //Add Outsource Parts.
        Part o1 = new Outsourced(6,"Part O1", 1.45, 11, 6, 100, "Ohio Co");
        Part p = new Outsourced(7, "Part P", 3.45, 10, 6, 100, "Ohio Co");
        Part q = new Outsourced(8, "Part Q", 1.45, 11, 6, 100, "Virgina Co");
        inv.addPart(o1);
        inv.addPart(p);
        inv.addPart(q);
        inv.addPart(new Outsourced(9, "Part R", 1.45, 11, 6, 100, "Michigan Co"));
        inv.addPart(new Outsourced(10, "Part 02", 1.45, 11, 6, 100, "Texas Co"));

        //Add allProducts.
        Product prod1 = new Product(80, "Product 1", 8.45, 24, 6, 100);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(o1);
        inv.addProduct(prod1);
        Product prod2 = new Product(160,"Product 2", 8.45, 30, 6, 100);
        prod2.addAssociatedPart(a2);
        prod2.addAssociatedPart(p);
        inv.addProduct(prod2);
        Product prod3 = new Product(240,"Product 3", 8.45, 32, 6, 100);
        prod3.addAssociatedPart(b);
        prod3.addAssociatedPart(q);
        inv.addProduct(prod3);
        Product prod4 = new Product(320,"Product 4", 30.45, 15, 6, 100);
        inv.addProduct(prod4);
        inv.addProduct(new Product(400,"Product 5", 30.45, 16, 6, 100));




    }



}
