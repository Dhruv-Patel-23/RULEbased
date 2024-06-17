package org.example;
import java.util.ArrayList;
import java.util.List;
public class OutputObj {
    public String recall_number;
    public String recalling_firm;
    public ArrayList<String> item_number = new ArrayList<>();
    public ArrayList<String> model_number = new ArrayList<>();
    public ArrayList<String> catalog_number = new ArrayList<>();
    public ArrayList<String> GTIN = new ArrayList<>();
    public ArrayList<String> lot_number = new ArrayList<>();
    public ArrayList<String> UDI = new ArrayList<>();
    public ArrayList<String> serial_number = new ArrayList<>();
    public ArrayList<String> batch_number = new ArrayList<>();
    public ArrayList<String> product_number = new ArrayList<>();
    public ArrayList<String> part_number = new ArrayList<>();
    public ArrayList<String> UPN = new ArrayList<>();
    public ArrayList<String> UPC = new ArrayList<>();
    public ArrayList<String> kit_number = new ArrayList<>();
    public ArrayList<String> ref_number = new ArrayList<>();
    public ArrayList<String> reorder_number = new ArrayList<>();
    @Override
    public String toString() {
        return "{\n" +
                "\"recall_number\" : \"" + recall_number + '\"' + ",\n" +
                "\"recalling_firm\" : \"" + recalling_firm + '\"' + ",\n" +
                "\"item_number\" : \"" + item_number + '\"' + ",\n" +
                "\"model_number\" : \"" + model_number + '\"' + ",\n" +
                "\"catalog_number\" : \"" + catalog_number + '\"' + ",\n" +
                "\"GTIN\" : \"" + GTIN + '\"' + ",\n" +
                "\"lot_number\" : \"" + lot_number + '\"' + ",\n" +
                "\"UDI\" : \"" + UDI + '\"' + ",\n" +
                "\"serial_number\" : \"" + serial_number + '\"' + ",\n" +
                "\"batch_number\" : \"" + batch_number + '\"' + ",\n" +
                "\"product_number\" : \"" + product_number + '\"' + ",\n" +
                "\"part_number\" : \"" + part_number + '\"' + ",\n" +
                "\"UPN\" : \"" + UPN + '\"' + ",\n" +
                "\"UPC\" : \"" + UPC + '\"' + ",\n" +
                "\"kit_number\" : \"" + kit_number + '\"' + ",\n" +
                "\"ref_number\" : \"" + ref_number + '\"' + ",\n" +
                "\"reorder_number\" : \"" + reorder_number + '\"' + ",\n" +
                '}';
    }
}