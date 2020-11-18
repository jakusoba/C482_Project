package model;

/**
 * @author obianuju akusoba */

/**This class extends the Part Super Class.*/
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int partId, String name, double price, int numInStock, int min, int max,String company) {
        setMax(max);
        setMin(min);
        setPartId(partId);
        setPartInStock(numInStock);
        setPartName(name);
        setPartPrice(price);
        setCompanyName(company);


    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }
}
