package model;

import java.util.ArrayList;

/** @author obianuju akusoba. */

/**This class creates the Product entity.*/
public class Product {

    private ArrayList<Part> associatedParts = new ArrayList<Part>();
    private int productId;
    private int inStock = 0;
    private String name;
    private double price = 0.0;
    private double cost;
    private int min;
    private int max;

    public  Product (int productId, String name, double price, int inStock, int min, int max) {
        setProductId(productId);
        setName(name);
        setPrice(price);
        setInstock(inStock);
        setMin(min);
        setMax(max);
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int id) {
        this.productId = id;
    }

    public int getInStock() {
        return this.inStock;
    }

    public void setInstock(int quantity) {
        this.inStock = quantity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
         this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**This method adds associated Parts.*/
    public void addAssociatedPart (Part partToAdd) {
        associatedParts.add(partToAdd);

    }
    /**This method removes associated Parts.*/
    public boolean removeAssociatedPart(int partToRemove) {
        int p;
        for (p = 0; p < associatedParts.size(); p++) {
            if(associatedParts.get(p).getPartId() == partToRemove) {
                associatedParts.remove(p);
                return true;
            }
        }
        return false;
    }

    /**This method searches associated Parts.*/
    public Part lookUpAssociatedPart(int partToSearch) {
        for (int p = 0; p < associatedParts.size(); p++) {
            if (associatedParts.get(p).getPartId() == partToSearch) {
                return associatedParts.get(p);

            }
        }
        return null;
    }

    /**This method returns associated Parts size.*/
    public int getPartListSize() {
        return associatedParts.size();
    }

}
