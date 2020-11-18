package model;

/**@author obianuju akusoba. */


/*This class extends the Part super class .**/
public class InHouse extends Part {

    private int machineId;

    public InHouse(int partId, String name, double price, int numInStock, int min, int max, int machineId ) {

        setPartId(partId);
        setPartName(name);
        setPartPrice(price);
        setPartInStock(numInStock);
        setMin(min);
        setMax(max);
        this.machineId = machineId;


        this.machineId = machineId;

    }

    public int getMachineId() {
        return machineId;


    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
