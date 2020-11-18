package model;

import javafx.collections.ObservableList;
import java.util.ArrayList;
import javafx.collections.FXCollections;

/**
 * @author obianuju akusoba. */


/**This class creates Inventory.*/
public class Inventory {
    private ArrayList <Product> allProduct;
    private ArrayList <Part> allPart;

    public Inventory() {
        allPart = new ArrayList<>();
        allProduct = new ArrayList<>();
    }

    /**This method adds all parts.*/
    public void addPart(Part partToAdd){
        if (partToAdd != null) {
            allPart.add(partToAdd);
        }

    }

    /**This method adds all products.*/
    public void addProduct(Product productToAdd) {
        if (productToAdd != null) {
            this.allProduct.add(productToAdd);
        }
    }

    /**This method searches for all parts and return data.*/
    public Part lookUpPart (int partToLookUp) {
        if (!allPart.isEmpty()) {
            for (int p = 0; p < allPart.size(); p++) {
                if (allPart.get(p).getPartId() == partToLookUp) {
                    return allPart.get(p);
                }
            }
        }
        return null;
    }

    /**This method searches for all products and return data.*/
    public Product lookUpProduct (int productToSearch) {
        if (!allProduct.isEmpty()) {
            for (int p = 0; p < allProduct.size(); p++) {
                if (allProduct.get(p).getProductId() == productToSearch) {
                    return allProduct.get(p);
                }
            }
        }
        return  null;
    }


    /**This method tracks changes in the Part list.*/
    public ObservableList<Part> lookUpPart(String partNameToLookUp) {
        if(!allPart.isEmpty()) {
            ObservableList searchPartsList = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getPartName().contains(partNameToLookUp)) {
                    searchPartsList.add(p);
                }
            }
            return searchPartsList;
        }
        return null;

    }

    /**This method tracks changes in the Product list.*/
    public ObservableList<Product> lookUpProduct(String productNameToLookUp) {
        return null;
    }

    /**This method updates all Parts with items.*/
    public void updatePart (Part partToUpdate) {
        for (int p = 0; p < allPart.size(); p++ ) {
            if (allPart.get(p).getPartId() == partToUpdate.partId) {
                allPart.set(p, partToUpdate);
                break;

            }
        }
        return;

    }
    /**This method updates all Products with items.*/
    public void updateProduct (Product productToUpdate) {
        for (int p = 0; p < allProduct.size(); p++) {
            if (allProduct.get(p).getProductId() == productToUpdate.getProductId()) {
                allProduct.set(p, productToUpdate);
                break;
            }
        }
        return;
    }
    /**This method removes part data from all Parts.*/
    public boolean deletePart(Part partToDelete) {
        for (int p = 0; p < allPart.size(); p++) {
            if (allPart.get(p).getPartId() == partToDelete.getPartId()) {
                allPart.remove(p);
                return true;

            }
        }
        return false;

    }

    /**This method removes products data from all Products.*/
    public boolean removeProduct(int productToRemove) {
        for (int p = 0; p < allProduct.size(); p++) {
            if (allProduct.get(p).getProductId() == productToRemove) {
                allProduct.remove(p);
                return true;
            }

        }
        return false;
    }

    /**This method creates a Part ArrayList.*/
    public ArrayList<Part> getAllParts () {
        return allPart;

    }
    /**This method creates a Product ArrayList.*/
    public ArrayList<Product> getAllProducts () {
        return allProduct;
    }

    /**This method returns all Product size.*/
    public int productListSize() {
        return allProduct.size();
    }

    /**This method returns all Part size.*/
    public int partListSize() {
        return allPart.size();
    }


}
