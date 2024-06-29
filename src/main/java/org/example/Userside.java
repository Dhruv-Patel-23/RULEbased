package org.example;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static org.example.Main.outputObs;

public class Userside {

    public  static  void  main(String[] args){

        Scanner scanner = new Scanner(System.in);
        OutputObj temp =new OutputObj();




        System.out.println("Enter the Model number");
        // Reading a String
        String model = scanner.nextLine();

        if(!Objects.equals(model, "-")) {
            temp.model_number.add(model);
        }

        System.out.println("Enter the catalog number");
        // Reading a String
        String catalog = scanner.nextLine();

        if(!Objects.equals(catalog, "-")){
            temp.catalog_number.add(catalog);
        }
        System.out.println("Enter the product number");
        // Reading a String
        String product = scanner.nextLine();

        if(!Objects.equals(product, "-")){
            temp.product_number.add(product);
        }
        System.out.println("Enter the item number");
        // Reading a String
        String item = scanner.nextLine();

        if(!Objects.equals(item, "-")){
            temp.item_number.add(item);
        }
        System.out.println("Enter the UDI/DI number");
        // Reading a String
        String UDI = scanner.nextLine();

        if(!Objects.equals(UDI, "-")){
            temp.UDI.add(UDI);
        }
        System.out.println("Enter the GTIN number");
        // Reading a String
        String GTIN = scanner.nextLine();

        if(!Objects.equals(GTIN, "-")){
            temp.GTIN.add(GTIN);
        }

        ArrayList<OutputObj> devices = outputObs();


        ArrayList<String> recall_outputs =new ArrayList<>();
        assert devices != null;
        for(OutputObj device: devices){

            if((!temp.model_number.isEmpty()&&device.model_number.contains(temp.model_number.getFirst())) || (!temp.item_number.isEmpty() && device.item_number.contains(temp.item_number.getFirst())) || (!temp.product_number.isEmpty() && device.product_number.contains(temp.product_number.getFirst())) || (!temp.UDI.isEmpty() && device.UDI.contains(temp.UDI.getFirst()))){

               //-
                // System.out.println(device);

            }

        }








    }
}
