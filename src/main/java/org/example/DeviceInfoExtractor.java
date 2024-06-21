
package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class DeviceInfoExtractor {

    public static void Exatech(String input, Set<OutputObj> objInfoList,String recall){

        boolean done=false;

        // Regular expression patterns
        String reg1 = "(\\d{3}-\\d{2}-\\d{2})";
        String reg2 = "UDI/?-?DI\\s*:?\\s*(\\w+)";
        String reg3 = "Serial\\s*Numbers?:?\\s*([\\d,\\s]+)";

        Pattern pat1 = Pattern.compile(reg1);
        Pattern pat2 = Pattern.compile(reg2);
        Pattern pat3 = Pattern.compile(reg3);


        Matcher mat1 = pat1.matcher(input);
        Matcher mat2 = pat2.matcher(input);
        Matcher mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));


            String[] serials = mat3.group(1).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));
            objInfoList.add(deviceInfo);
            done=true;
        }


        if(done){
            return;
        }

        reg1 = "(\\w-)+\\w";
        reg2="GTIN\\s*(\\w+)";
        reg3="Serial\\s*Numbers?:?\\s*([\\w,\\s]+)";

        pat1 = Pattern.compile(reg1);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(mat1.group());
            deviceInfo.GTIN.add(mat2.group(1));
            String[] serials = mat3.group(1).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));
            objInfoList.add(deviceInfo);
            done=true;
        }
    }

    public static  void Fresenius(String input, Set<OutputObj> objInfoList,String recall) {
        boolean done=false;
        String reg1 = "Product\\s*Code:?\\s*(\\w+)";
        String reg2 = "UDI/?-?DI\\s*:?\\s*(\\w+)";
        String reg3 = "Serial\\s*Numbers?:?\\s*([\\w,\\s]+to\\s*\\w+)";

        Pattern pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        Pattern pat2 = Pattern.compile(reg2);
        Pattern pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        Matcher mat1 = pat1.matcher(input);
        Matcher mat2 = pat2.matcher(input);
        Matcher mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.serial_number.add(mat3.group(1));
            objInfoList.add(deviceInfo);
            done=true;
        }



        if(done){
            return;
        }
        reg1="Model\\s*([Numberso.]+)?\\s*:?\\s*([\\w-]+)";
        reg2="UDI/?-?DI:?\\s*(\\w+)";
        reg3="Serial\\s*([Numberso.]+)?:?\\s*([\\w\\s,]+)";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(mat1.group(2));
            deviceInfo.UDI.add(mat2.group(1));

            String[] serials = mat3.group(2).split(",");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));
            objInfoList.add(deviceInfo);
            done=true;
        }

        if(done){
            return;
        }

        reg1="Model\\s*([Numberso.]+)?\\s*:?\\s*([\\w-]+)";
        reg2="UDI-?/?DI\\s*\\(\\w+\\):\\s*(\\w+).\\s*UDI-?/?DI\\s*\\(\\w+\\):\\s*(\\w+)";
        reg3="All lots";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(mat1.group(2));
            deviceInfo.UDI.add(mat2.group(1));deviceInfo.UDI.add(mat2.group(2));

            deviceInfo.lot_number.add(mat3.group());
            objInfoList.add(deviceInfo);
            done=true;
        }

    }

    public static void Medline(String input, Set<OutputObj> objInfoList,String recall){



        boolean done=false;
        String reg1 = "REF\\s*(\\w+)";

        String reg2 = "UDI/?-?DI\\s*:?\\s*(\\w+)\\s*\\(\\w+\\),?\\s*(\\w+)\\s*\\(\\w+\\)";
        String reg3 = "Lot\\s*(Numbers?)?(codes?)?:?\\s*([\\w,\\s]+)";

        Pattern pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        Pattern pat2 = Pattern.compile(reg2);
        Pattern pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        Matcher mat1 = pat1.matcher(input);
        Matcher mat2 = pat2.matcher(input);
        Matcher mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.UDI.add(mat2.group(2));
            deviceInfo.lot_number.add(mat3.group(3));
            objInfoList.add(deviceInfo);
            done=true;
        }

        //--------------------------------------------------------------------------------------------
        if(done){
            return;
        }
         reg1 = "REF\\s*(\\w+)";
         reg2 = "UDI/?-?DI\\s*:?\\s*(\\w+)";
         reg3 = "Lot\\s*(Numbers?)?(codes?)?:?\\s*([\\w,\\s]+)";

         pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
         pat2 = Pattern.compile(reg2);
         pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


         mat1 = pat1.matcher(input);
         mat2 = pat2.matcher(input);
         mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));

            deviceInfo.lot_number.add(mat3.group(3));
            objInfoList.add(deviceInfo);
            done=true;
        }

//
//        // Regular expression to capture REF number, UDI/DI, and Lot Numbers
//        String refRegex = "REF\\s*(\\w+)";
//        String udiregex = "UDI/DI:?\\s*(\\w+)";
//
//        String lotRegex = "Lot\\s*(Numbers)?(code)?:?\\s*([\\w,\\s]+)";
//
//        // Compile the regex
//        Pattern refPattern = Pattern.compile(refRegex);
//        Matcher refMatcher = refPattern.matcher(input);
//
//        Pattern udiPattern = Pattern.compile(udiregex);
//        Matcher udiMatcher = udiPattern.matcher(input);
//
//
//        Pattern lot = Pattern.compile(lotRegex);
//        Matcher lotMatcher = lot.matcher(input);
//
//        while ((refMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
//            deviceInfo.ref_number.add(refMatcher.group(1));
//            deviceInfo.UDI.add(udiMatcher.group(1));
//            deviceInfo.lot_number.add(lotMatcher.group(3));
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
        //----------------------------------------------------------------------------------------

       if(done){
           return;
       }
       reg1 = "REF\\s*(\\w+)";
       reg2 = "GTIN\\s*:?\\s*(\\w+)";
       reg3 = "Lot\\s*(Numbers?)?(codes?)?:?\\s*([\\w,\\s]+)";

         pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
         pat2 = Pattern.compile(reg2);
         pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


         mat1 = pat1.matcher(input);
         mat2 = pat2.matcher(input);
         mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat1.group(1));
            deviceInfo.GTIN.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(3));
            objInfoList.add(deviceInfo);
            done=true;
        }




//
//
//        refRegex = "REF\\s*(\\w+)";
//        udiregex = "GTIN:?\\s*(\\w+)";
//        lotRegex = "Lot\\s*Numbers:?\\s*([\\w,\\s]+)";
//
//        // Compile the regex
//        refPattern = Pattern.compile(refRegex);
//        refMatcher = refPattern.matcher(input);
//
//        udiPattern = Pattern.compile(udiregex);
//        udiMatcher = udiPattern.matcher(input);
//
//
//        lot = Pattern.compile(lotRegex);
//        lotMatcher = lot.matcher(input);
//
//        while ((refMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
//            deviceInfo.ref_number.add(refMatcher.group(1));
//            deviceInfo.GTIN.add(udiMatcher.group(1));
//            deviceInfo.lot_number.add(lotMatcher.group(1));
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
        //----------------------------------------------------------------------------------------


        if(done){
            return;
        }
        reg1 = "Reorder\\s*Numbers?\\s*(\\w+)";
        reg2 = "UDI/?-?DI:?\\s*(\\w+)[\\s()\\w]*.?\\s*(\\w+)[\\s()\\w]*";
        reg3 = "Lot\\s*(Numbers?)?(codes?)?:?\\s*([\\w,\\s]+)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.UDI.add(mat2.group(2));
            deviceInfo.lot_number.add(mat3.group(3));
            objInfoList.add(deviceInfo);
            done=true;
        }


        if(done){
            return;
        }
        reg1 = "Product\\sCode:?\\s(\\w+)";
        reg2 = "UDI/?-?DI:?\\s*(\\w+)\\s*\\(\\w*\\)\\s*,?\\s*(\\w+)\\s*\\(\\w*\\)";
        reg3 = "Lot\\s*(Numbers?)?(codes?)?:?\\s*([\\w,\\s]+)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.UDI.add(mat2.group(2));
            deviceInfo.lot_number.add(mat3.group(3));
            objInfoList.add(deviceInfo);
            done=true;
        }





//        String prodregex = "Product\\sCode\\s[0-9A-Za-z]+";
//        udiregex = "UDI/DI\\s\\d{14}[\\s()\\w]*,?\\s*\\d{14}[\\s()\\w]*";
//        lotRegex = "Lot\\sNumbers:\\s*([\\w+,\\s]+)";
//
//        Pattern prodPattern = Pattern.compile(prodregex);
//        udiPattern=Pattern.compile(udiregex);
//        Pattern lotPattern=Pattern.compile(lotRegex);
//
//        Matcher prodMatcher = prodPattern.matcher(input);
//        udiMatcher = udiPattern.matcher(input);
//        lotMatcher = lotPattern.matcher(input);
//
//        while ((prodMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
//            deviceInfo.product_number.add(prodMatcher.group().substring(12));
//            deviceInfo.UDI.add(udiMatcher.group().substring(6));
//            deviceInfo.lot_number.add(lotMatcher.group().substring(12));
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }


        //--------------------------------------
    if(done){
        return;
    }
         reg1 = "Item\\s*Numbers?:\\s*(\\w+)";
         reg3 = "UDI/GTIN\\s*[\\w()]*:?\\s*(\\w+),\\s*UDI/GTIN\\s*[\\w()]*:?\\s*(\\w+)";
         reg2 = "Lot\\s*Numbers?:?\\s*(\\w+)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while (mat1.find() && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat3.group(1));
            deviceInfo.UDI.add(mat3.group(2));
            deviceInfo.lot_number.add(mat2.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }
        //--------------------------------------
        if(done){
            return;
        }
        reg1 = "Item\\s*Number:\\s*(\\w+)";
        reg3 = "[\\w()]*\\s*UDI/GTIN\\s*:?\\s*(\\w+),\\s*[\\w()]*\\s*UDI/GTIN\\s*:?\\s*(\\w+)";
        reg2 = "Lot\\s*Numbers?:?\\s*(\\w+)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while (mat1.find() && mat2.find() && mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat3.group(1));
            deviceInfo.UDI.add(mat3.group(2));
            deviceInfo.lot_number.add(mat2.group(1));

            objInfoList.add(deviceInfo);

            done=true;
        }

        //--------------------------------------
        if(done){
            return;
        }
        reg1 = "Model\\s*Numbers?:?\\s*(\\w+)";
        reg2 = "UDI/DI\\s*:?\\(\\w*\\)\\s*(\\w+),\\s*UDI/DI\\s*:?\\(\\w*\\)\\s*(\\w+)";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);


        while (mat1.find() && mat2.find()) {
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.UDI.add(mat2.group(2));
            objInfoList.add(deviceInfo);
            done=true;
        }

        //--------------------------------------
        if(done){
            return;
        }
        reg1 = "REF\\s*(\\w+)";
        reg2 = "GTIN\\s*\\(01\\)\\s*(\\w+)";
        reg3="Lot\\s*Numbers?\\s*(\\w+)";


        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat1.group(1));
            deviceInfo.GTIN.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(1));


            objInfoList.add(deviceInfo);

            done=true;
        }
        //--------------------------------------
        if(done){
            return;
        }
        reg1 = "REF\\s*(\\w+)";
        reg2 = "GTIN\\s*(\\w+)";
        reg3="Batch\\s*Numbers?:?\\s*(\\w+)";


        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat1.group(1));
            deviceInfo.GTIN.add(mat2.group(1));
            deviceInfo.batch_number.add(mat3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }
        //------------------------------------------------------------------------------------------------------------------
        if(done){
            return;
        }
        reg1 = "Reorder\\s*#?:?\\s*(\\w+)";
        reg2 = "UDI/DI:?\\s*\\(01\\)\\s*(\\w+)\\s*\\(\\w+\\),\\s*\\(01\\)\\s*(\\w+)\\s*\\(\\w+\\)";
        reg3="Lot\\s*Numbers?\\s*(\\w+)";


        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }

        //--------------------------------------
    if(done){
        return;
    }
        reg1 = "Reorder\\s*:?\\s*(\\w+)";
        reg2 = "UDI-?/?DI:?\\s*(\\w+)";
        reg3="Lot\\s*code:?\\s*(\\w+)";


        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }
        //--------------------------------------
    if(done){
        return;
    }
        reg1 = "Reorder\\s*Number:?\\s*(\\w+)";
        reg2 = "GTIN:?\\s*(\\w+)";
        reg3="Lot\\s*#?\\s*(\\w+)";


        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(mat1.group(1));
            deviceInfo.GTIN.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }
        //------------------------------------------
        if(done){
            return;
        }

        reg1 = "Model\\s*Numbers?\\s*:?\\s*(\\w+)";
        reg2 = "UDI-?/?DI:?\\s*(\\(\\w+\\))?\\s*\\(01\\)\\s*(\\w+)\\s*,\\s*UDI/?-?DI:?\\s*(\\(\\w+\\))?\\s*\\(01\\)\\s*(\\w+)";
        reg3="Lot\\s*Numbers?:?\\s*(\\w+)";


        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(4));
            deviceInfo.UDI.add(mat2.group(2));
            deviceInfo.lot_number.add(mat3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }
        //--------------------------------------
    if(done){
        return;
    }
        reg1 = "PN:?\\s*(\\w+)";
        reg2 = "Items?\\s*(\\w+)";
        String reg4 = "UDI/DI\\s*\\(\\w+\\)\\s*(\\w+)";
        reg3="Lot\\s*numbers?\\s*(\\w+)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);




        Pattern pat4 = Pattern.compile(reg4,Pattern.CASE_INSENSITIVE);



        Matcher mat4 = pat4.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()&& mat4.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(mat1.group(1));
            deviceInfo.item_number.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(1));
            deviceInfo.UDI.add(mat4.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }


        //-------------------------------------------------------------------------------------
        reg1="Model\\s*Number\\s*:\\s*(\\w+),?;?\\s*UPC\\s*Number:\\s*(\\w+)";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);

        mat1 = pat1.matcher(input);

        while (mat1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.model_number.add(mat1.group(1));
            deviceInfo.UPC.add(mat1.group(2));
            objInfoList.add(deviceInfo);
            done=true;
        }

    }

    public static void Boston(String input, Set<OutputObj> objInfoList,String recall){



        boolean done=false;
        String reg1 = "MODEL\\s*-\\s*([\\w\\s]*)\\(\\w*\\)";
        String reg2 = "REF\\s*(\\w+)";
        String reg3 = "UDI/DI\\s*(\\w+)";
        String reg4 = "Batch\\s*Numbers?:([\\s\\w,]+)";

        Pattern pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        Pattern pat2 = Pattern.compile(reg2);
        Pattern pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);
        Pattern pat4 = Pattern.compile(reg4,Pattern.CASE_INSENSITIVE);

        Matcher mat1 = pat1.matcher(input);
        Matcher mat2 = pat2.matcher(input);
        Matcher mat3 = pat3.matcher(input);
        Matcher mat4 = pat4.matcher(input);

        while ((mat1.find()) && mat2.find() && mat3.find() && mat4.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat2.group(1));
            deviceInfo.UDI.add(mat3.group(1));
            deviceInfo.model_number.add(mat1.group(1));
            deviceInfo.batch_number.add(mat4.group(1));
            objInfoList.add(deviceInfo);
            done=true;
        }


        //--------------------------------------------------------------------------------------------------------
        if(done){
            return;
        }



         reg1 = "GTIN numbers in the U.S.:?([\\w\\s,]*)";
         reg2 = "GTIN numbers OUS:?([\\w\\s,]*)";
         reg3 ="All\\s*serial\\s*numbers?";
         pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
         pat2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
         pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

         mat1 = pat1.matcher(input);
         mat2 = pat2.matcher(input);
         mat3 = pat3.matcher(input);

        while (mat1.find() && mat2.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.GTIN.add(mat2.group(1));
            deviceInfo.GTIN.add(mat1.group(1));
            if(mat3.find()) {


                deviceInfo.serial_number.add(mat3.group());
            }
            objInfoList.add(deviceInfo);
            done=true;
        }

        //--------------------------------------------------------------------------------------------------------
        if(done){return;}
        reg1 = "Catalog\\s*numbers?\\s*(\\w*)";
        reg2 = "GTIN:?\\s*(\\w*)";
        reg3 ="Lot[\\w/\\s]*:?\\s*([\\w\\s,]*)";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(mat1.group(1));
            deviceInfo.GTIN.add(mat2.group(1));



                deviceInfo.lot_number.add(mat3.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }

        //--------------------------------------------------------------------------------------------------------
    if(done){return;}
        reg1 = "(Outer\\s*box\\s*UPN?#\\s*(\\w+),\\s*Inner\\s*box\\s*UPN\\s*#\\s*(\\w+))";
        reg2 = "GTIN\\):\\s*(\\w+)";
        reg3 ="Lot\\s*/\\s*Batch\\s*#?\\s([\\w,\\s]+)";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.UPN.add(mat1.group(1));
            deviceInfo.UPN.add(mat1.group(2));
            deviceInfo.GTIN.add(mat2.group(1));



            deviceInfo.lot_number.add(mat3.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }


        //--------------------------------------------------------------------------------------------------------

        if(done){return;}
        reg1 = "GTIN:?\\s*(\\w+)";
        reg2 = "UPN:?\\s*(\\w+)";
        reg3 ="Lot\\s*Numbers?:([\\s,\\d]+)";
        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);

        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.UPN.add(mat2.group(1));
            deviceInfo.GTIN.add(mat1.group(1));
            deviceInfo.lot_number.add(mat3.group(1));
            objInfoList.add(deviceInfo);
            done=true;
        }

//--------------------------------------------------------------------------------------------------------
        if(done){return;}
        reg1 = "UPN\\s(\\w+),\\sGTIN\\s\\(UDI-DI\\)\\s(\\w+),\\sLots?\\s([\\w,\\s]+)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);


        while (mat1.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.UPN.add(mat1.group(1));
            deviceInfo.GTIN.add(mat1.group(2));
            deviceInfo.lot_number.add(mat1.group(3));
            objInfoList.add(deviceInfo);
            done=true;
        }

//---------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------
        if(done){return;}
        reg1 = "(\\w)\\)\\s*(\\w+),\\s*UDI/DI\\s*(\\w+),\\s*ALL\\s*LOT\\s*CODES";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);


        while (mat1.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.UDI.add(mat1.group(3));
            deviceInfo.UPN.add(mat1.group(2));
            deviceInfo.lot_number.add("ALL LOT CODES");
            objInfoList.add(deviceInfo);
            done=true;
        }


        //--------------------------------------------------------------------------------------------------
        if(done){return;}
        reg1 = "REF:?\\s*(\\w*)";
        reg2 = "UDI/?-?DI:?\\s*(\\w+)";
        reg3 = "Lot\\s*Numbers?:?([\\s,\\d]*)";

        pat1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        pat2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        pat3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        mat1 = pat1.matcher(input);
        mat2 = pat2.matcher(input);
        mat3 = pat3.matcher(input);


        while (mat1.find() && mat2.find()&& mat3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(mat1.group(1));
            deviceInfo.UDI.add(mat2.group(1));
            deviceInfo.lot_number.add(mat3.group(1));
            objInfoList.add(deviceInfo);
            done=true;
        }


    }

    public static void Cardinal (String input, Set<OutputObj> objInfoList,String recall){
        boolean done = false;
        String reg1 ="Model\\s*(\\w+)";
        String reg2="Lot\\s*#?(\\w+)";
        String reg3 = "UDI-?/?DI:?\\s*([\\w\\s(),]+)";

        Pattern regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        Pattern regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        Pattern regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        Matcher regMatcher1 = regPattern1.matcher(input);
        Matcher regMatcher2 = regPattern2.matcher(input);
        Matcher regMatcher3 = regPattern3.matcher(input);

        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group(1));
            deviceInfo.lot_number.add(regMatcher2.group(1));
            deviceInfo.UDI.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);

    done = true;


        }


        //-----------------------------------------------------------------------
        if(done){return;}
        reg1= "REF\\s*:?\\s*([\\w-]+)";
        reg2="UDI/DI\\s*(\\w+)\\s*\\(\\w+\\),\\s*(\\w+)\\s*\\(\\w+\\),\\s*(\\w+)\\s*\\(\\w+\\)";
        reg3 = "Lot\\s*Numbers?:?([\\w\\s,]+)";

        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.UDI.add(regMatcher2.group(2));
            deviceInfo.UDI.add(regMatcher2.group(3));
            deviceInfo.lot_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);



    done=true;
        }

        //--------------------------------------------------
        if ((done)) {
            return;}

            reg1= "Cat\\.\\s*(\\w+)";
            reg2="Lot\\s*#?(\\w+)";
            reg3="UDI-?/?DI\\s*(\\d+)";

        // Compile the regex pattern
       regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
       regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
       regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        // Matcher for the regex pattern
        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(regMatcher1.group(1));
            deviceInfo.lot_number.add(regMatcher2.group(1));
            deviceInfo.UDI.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }
        //--------------------------------------------------
        if ((done)) {
            return;}

            reg1= "Cat\\.?\\s*(\\w+)";
        reg2="all lots";


        // Compile the regex pattern
       regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
       regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);

        // Matcher for the regex pattern
        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(regMatcher1.group(1));
            deviceInfo.lot_number.add(regMatcher2.group());


            objInfoList.add(deviceInfo);
            done=true;
        }


//--------------------------------------------------

        if(done){return;}
        reg1= "Product\\s*Code:\\s*(\\w+)";
        reg2="UDI/?-?DI:?\\s*(\\w+)\\s*-\\s*each,\\s*(\\w+)\\s*-\\s*box,\\s*(\\w+)\\s*-\\s*case";
        reg3="Lot\\s*Numbers?:?\\s*([\\w,\\s]+)";

        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.UDI.add(regMatcher2.group(2));
            deviceInfo.UDI.add(regMatcher2.group(3));
            deviceInfo.lot_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
            done=true;


        }

//--------------------------------------------------
        if(done){return;}
        reg1="UDI/?-?DI:?\\s*(\\w+)\\s*\\(\\w+\\),\\s*(\\w+)\\s*\\(\\w+\\)";

        reg2="Lot\\s*Numbers?:?(\\s*[\\w,\\s]+)";
        reg3="REF\\s*([-\\w]+)";

        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while( regMatcher2.find() && regMatcher1.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher1.group(2));
            deviceInfo.lot_number.add(regMatcher2.group(1));

                deviceInfo.ref_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }




    }

    public static void Baxter(String input, Set<OutputObj> objInfoList,String recall) {

        boolean done = false;

        String reg1 = "Product\\s*Code\\s*(\\d+)";
        String reg3 = "Serial\\s*Numbers?\\s*:?\\s*([\\d\\s,]+)";
        String reg2 = "UDI-?/?DI\\s*:?\\s*(\\d+)";

        Pattern regPattern1 = Pattern.compile(reg1);
        Pattern regPattern2 = Pattern.compile(reg2);
        Pattern regPattern3 = Pattern.compile(reg3);

        Matcher regMatcher1 = regPattern1.matcher(input);
        Matcher regMatcher2 = regPattern2.matcher(input);
        Matcher regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.serial_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }



        //-------------------------------------------------------------------------------------
        if(done){
            return;
        }

        reg1 = "REF\\s*:?\\s*([\\w-]+)";
        reg3 ="Serial\\s*Numbers?\\s*:?\\s*([\\w\\s,]+)";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.serial_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
            done = true;
        }

        //-------------------------------------------------------------------------------------

        if(done){
            return;
        }
        reg1="([-\\w]+),\\s*UDI/DI\\s*(\\d+)";

        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher1.group(2));


            objInfoList.add(deviceInfo);
            done =true;
        }

        //-------------------------------------------------------------------------------------
        if(done){
            return;
        }
        reg1="P?p?roduct\\s*C?c?ode:?\\s*(\\w+),\\s*UDI/DI:?\\s*(\\d+)";
        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group(2));
            deviceInfo.product_number.add(regMatcher1.group(1));


            objInfoList.add(deviceInfo);
            done =true;
        }
        //-------------------------------------------------------------------------------------
        if(done){
            return;
        }
        reg1="([\\w-]+):\\s*UDI/DI\\s*([\\w-]+)";
        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group(2));
            deviceInfo.product_number.add(regMatcher1.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }

        //-------------------------------------------------------------------------------------
        if(done){
            return;
        }
        reg1="UDI/DI\\s*(\\w+),\\s*Lot\\s*Numbers?:?\\s*(\\w+)";
        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);



        reg2="REF\\s*(\\w+)";
        regPattern2 = Pattern.compile(reg2);
        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);


        while (regMatcher1.find() && regMatcher2.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group(1));
            deviceInfo.ref_number.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher1.group(2));


            objInfoList.add(deviceInfo);
            done =true;
        }






    }

    public static void NonLLCPhilips(String input, Set<OutputObj> objInfoList,String recall){
    boolean done=false;
        String reg1 ="Model\\s*No[\\s.]+(\\d+)\\s*UDI-DI\\s*([\\w/]+)";


        Pattern regPattern1 = Pattern.compile(reg1);


        Matcher regMatcher1 = regPattern1.matcher(input);




        while(regMatcher1.find()){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher1.group(2));

            objInfoList.add(deviceInfo);
    done=true;

        }


        //----------------------------------------------------------------------------
    if(done){return;}
        reg1="Product\\s*Number:?\\s*(\\d+)";
        String reg2="UDI-?/?DI\\s*:?\\s*([-\\w]+)";
        String reg3="Serial\\s*Numbers?:?\\s*([\\w\\s,]+)";



        regPattern1 = Pattern.compile(reg1);
        Pattern regPattern2 = Pattern.compile(reg2);
        Pattern regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        Matcher regMatcher2 = regPattern2.matcher(input);
        Matcher regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.serial_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }        //----------------------------------------------------------------------------
        if(done){return;}
        reg1="Product\\s*Code\\s*(\\w+)";
        reg2="UDI\\s*([-\\w]+)?";
        reg3="Serial\\s*numbers\\s*([\\w\\s,]+)";



        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            if(regMatcher2.group(1)!=null){
                deviceInfo.UDI.add(regMatcher2.group(1));
            }

            deviceInfo.serial_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
            done=true;
        }


        //----------------------------------
        if(done){return;}
        reg1="Product\\s*Number\\s*(\\w+)";
        reg2="UDI-DI\\s*(\\w+)";
        reg3="Serial\\s*n?N?umber\\s*(\\w+)\\s*Accessory\\s*Serial\\s*N?n?umber\\s*(\\w+)";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            if(regMatcher2.group(1)!=null){
                deviceInfo.UDI.add(regMatcher2.group(1));
            }

            deviceInfo.serial_number.add(regMatcher3.group(1));
            deviceInfo.serial_number.add(regMatcher3.group(2));

            objInfoList.add(deviceInfo);
            done=true;
        }
        //----------------------------------
        if(done){return;}
        reg1="Model\\s*Number\\s*(\\w+)";
        reg2="UDI-DI:?\\s*(\\w+)";
        reg3="Lot\\s*Code:?\\s*(\\w+)";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            if(regMatcher2.group(1)!=null){
                deviceInfo.UDI.add(regMatcher2.group(1));
            }

            deviceInfo.lot_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }
        //----------------------------------
        if(done){return;}
        reg1="Model\\s*(Numbers?)?\\s*:?\\s*(\\w+)";
        reg2="UDI-?/?DI\\s*:?\\s*(\\(\\w+\\))?\\s*(\\w+)";
        reg3="serial\\s*numbers?\\s*:?\\s*([\\s\\d]+)";

        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group(2));
            if(regMatcher2.group(1)!=null){
                deviceInfo.UDI.add(regMatcher2.group(2));
            }

            deviceInfo.serial_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
            done=true;
        }

        //----------------------------------

        if(done){return;}
        reg1="Model\\s*No\\.\\s*(\\w+)";
        reg2="UDI-DI:?\\s*(\\(\\d\\))?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            if(regMatcher2.group(2)!=null){
                deviceInfo.UDI.add(regMatcher2.group(2));
            }


            objInfoList.add(deviceInfo);
            done=true;
        }

    }

    public static void Howmedica(String input, Set<OutputObj> objInfoList,String recall_number) {

        int created = 0;

        // Define regex patterns
        String partNoPattern = "Part\\s*No\\.\\s*([\\w-]+)";
        String gtinPattern = "GTIN:\\s*(\\d+)";
        String lotNoPattern = "Lot\\s*No\\.\\s*([\\w,\\s*]+)";

        // Compile regex patterns
        Pattern partNoRegex = Pattern.compile(partNoPattern,Pattern.CASE_INSENSITIVE);
        Pattern gtinRegex = Pattern.compile(gtinPattern,Pattern.CASE_INSENSITIVE);
        Pattern lotNoRegex = Pattern.compile(lotNoPattern,Pattern.CASE_INSENSITIVE);

        // Match patterns
        Matcher partNoMatcher = partNoRegex.matcher(input);
        Matcher gtinMatcher = gtinRegex.matcher(input);
        Matcher lotNoMatcher = lotNoRegex.matcher(input);

        // Create a new OutputObj
        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        // Extract Part No.
        if (partNoMatcher.find()) {
            outputObj.part_number.add(partNoMatcher.group(1));
            created++;
        }

        // Extract GTIN
        if (gtinMatcher.find()) {
            outputObj.GTIN.add(gtinMatcher.group(1));
            created++;
        }

        // Extract Lot No.
        if (lotNoMatcher.find()) {
            String lotNos = lotNoMatcher.group(1);
            String[] lotNoArray = lotNos.split(",\\s*"); // Split by comma and optional spaces

            outputObj.lot_number.addAll(Arrays.asList(lotNoArray));
            created++;
        }

        if (created == 3){
            // Add the outputObj to the set
            objInfoList.add(outputObj);
            return;
        }

        created = 0;




        String partNumberPattern2 = "Part Number:?\\s*([\\w-]+)";
        String gtinPattern2 = "GTIN:?\\s*(\\w+)";
        String lotNumbersPattern2 = "Lot\\s*Numbers:?\\s*([\\w,\\s]+)";

        // Compile regex patterns
        Pattern partNumberRegex2 = Pattern.compile(partNumberPattern2,Pattern.CASE_INSENSITIVE);
        Pattern gtinRegex2 = Pattern.compile(gtinPattern2,Pattern.CASE_INSENSITIVE);
        Pattern lotNumbersRegex2 = Pattern.compile(lotNumbersPattern2,Pattern.CASE_INSENSITIVE);

        // Match patterns
        Matcher partNumberMatcher2 = partNumberRegex2.matcher(input);
        Matcher gtinMatcher2 = gtinRegex2.matcher(input);
        Matcher lotNumbersMatcher2 = lotNumbersRegex2.matcher(input);

        // Create a new OutputObj
        OutputObj outputObj2 = new OutputObj();
        outputObj2.recall_number = recall_number;

        // Extract Part Number
        if (partNumberMatcher2.find()) {
            outputObj2.part_number.add(partNumberMatcher2.group(1));
            created++;
        }

        // Extract GTIN
        if (gtinMatcher2.find()) {
            outputObj2.GTIN.add(gtinMatcher2.group(1));
            created++;
        }

        // Extract Lot Numbers
        if (lotNumbersMatcher2.find()) {
            String lotNumbers = lotNumbersMatcher2.group(1);
            String[] lotNumberArray = lotNumbers.split(",\\s*"); // Split by comma and optional spaces

            outputObj2.lot_number.addAll(Arrays.asList(lotNumberArray));
            created++;
        }

        if (created == 3){
            // Add the outputObj to the set
            objInfoList.add(outputObj2);
            return;
        }

        created = 0;

        String catalogNumberPattern3 = "Catalog\\s*Number\\s*:?\\s*([\\w-]+)";
        String udiDiPattern3 = "UDI-DI\\s*:?\\s*([\\w()]+)";
        String lotNumberPattern3 = "Lot\\s*Number\\s*:\\s*(\\d+(?: \\d+)*)";

        // Compile regex patterns
        Pattern catalogNumberRegex3 = Pattern.compile(catalogNumberPattern3,Pattern.CASE_INSENSITIVE);
        Pattern udiDiRegex3 = Pattern.compile(udiDiPattern3,Pattern.CASE_INSENSITIVE);
        Pattern lotNumberRegex3 = Pattern.compile(lotNumberPattern3,Pattern.CASE_INSENSITIVE);

        // Match patterns
        Matcher catalogNumberMatcher3 = catalogNumberRegex3.matcher(input);
        Matcher udiDiMatcher3 = udiDiRegex3.matcher(input);
        Matcher lotNumberMatcher3 = lotNumberRegex3.matcher(input);

        // Create a new OutputObj instance
        OutputObj outputObj3 = new OutputObj();
        outputObj3.recall_number = recall_number;

        // Extract Catalog Number
        if (catalogNumberMatcher3.find()) {
            outputObj3.catalog_number.add(catalogNumberMatcher3.group(1));
            created++;
        }

        // Extract UDI-DI
        if (udiDiMatcher3.find()) {
            outputObj3.UDI.add(udiDiMatcher3.group(1));
            created++;
        }

        // Extract Lot Number
        if (lotNumberMatcher3.find()) {
            String lotNumbers = lotNumberMatcher3.group(1);
            String[] lotNumberArray = lotNumbers.split(" "); // Split by space

            outputObj3.lot_number.addAll(Arrays.asList(lotNumberArray));
            created++;
        }

        if(created == 3){
            // Add the outputObj to the set
            objInfoList.add(outputObj3);
            return;
        }

        created = 0;


        String productNumberPattern4 = "Product\\s*Number:?\\s*([\\w-]+)";
        String gtinPattern4 = "GTIN:?\\s*(\\d+)";
        String lotNumbersPattern4 = "Lot\\s*Numbers:?\\s*(\\d+(?:,\\s*\\d+)*)";

        // Compile regex patterns
        Pattern productNumberRegex4 = Pattern.compile(productNumberPattern4,Pattern.CASE_INSENSITIVE);
        Pattern gtinRegex4 = Pattern.compile(gtinPattern4,Pattern.CASE_INSENSITIVE);
        Pattern lotNumbersRegex4 = Pattern.compile(lotNumbersPattern4,Pattern.CASE_INSENSITIVE);

        // Match patterns
        Matcher productNumberMatcher4 = productNumberRegex4.matcher(input);
        Matcher gtinMatcher4 = gtinRegex4.matcher(input);
        Matcher lotNumbersMatcher4 = lotNumbersRegex4.matcher(input);

        // Create a new OutputObj instance
        OutputObj outputObj4 = new OutputObj();
        outputObj4.recall_number = recall_number;

        // Extract Product Number
        if (productNumberMatcher4.find()) {
            outputObj4.product_number.add(productNumberMatcher4.group(1));
            created++;
        }

        // Extract GTIN
        if (gtinMatcher4.find()) {
            outputObj4.GTIN.add(gtinMatcher4.group(1));
            created++;
        }

        // Extract Lot Numbers
        if (lotNumbersMatcher4.find()) {
            String lotNumbers = lotNumbersMatcher4.group(1);
            String[] lotNumberArray = lotNumbers.split(",\\s*"); // Split by comma and optional spaces

            outputObj4.lot_number.addAll(Arrays.asList(lotNumberArray));

            created++;
        }

        if(created==3){
            // Add the outputObj to the set
            objInfoList.add(outputObj4);
        }


    }

    public static  void Becton(String input, Set<OutputObj> objInfoList,String recall_number){
        // Define regex patterns
        String entryPattern = "Catalog No\\.\\s*(\\d+).*?UDI-DI\\s*([\\w;]+).*?Lots?\\s*([\\d\\s]+)";

        // Compile regex pattern
        Pattern entryRegex = Pattern.compile(entryPattern,Pattern.CASE_INSENSITIVE);

        // Match pattern
        Matcher entryMatcher = entryRegex.matcher(input);

        // Iterate over matches
        while (entryMatcher.find()) {
            // Create a new OutputObj
            OutputObj outputObj = new OutputObj();
            outputObj.recall_number = recall_number;

            // Extract Catalog Number
            outputObj.catalog_number.add(entryMatcher.group(1));

            // Extract UDI-DI
            String udiDi = entryMatcher.group(2).replaceAll("\\s+", "");
            outputObj.UDI.add(udiDi);

            // Extract Lot Numbers
            String lotNumbers = entryMatcher.group(3);
            String[] lotNumberArray = lotNumbers.split("\\s+");
            outputObj.lot_number.addAll(Arrays.asList(lotNumberArray));

            // Add the outputObj to the set
            objInfoList.add(outputObj);
        }
    }


    public static  void Angiodynamics(String input, Set<OutputObj> objInfoList,String recall_number){

        int created = 0;

        // Define regex patterns
        String productNumberPattern = "Product\\s*Number:?\\s*(\\w+)";
        String udiDiPattern = "UDI-?/?DI:?\\s*(\\d+)";
        String lotNumbersPattern = "Lot\\s*Numbers:?\\s*([\\d,\\s]+)";

        // Compile regex patterns
        Pattern productNumberRegex = Pattern.compile(productNumberPattern,Pattern.CASE_INSENSITIVE);
        Pattern udiDiRegex = Pattern.compile(udiDiPattern,Pattern.CASE_INSENSITIVE);
        Pattern lotNumbersRegex = Pattern.compile(lotNumbersPattern,Pattern.CASE_INSENSITIVE);

        // Match patterns
        Matcher productNumberMatcher = productNumberRegex.matcher(input);
        Matcher udiDiMatcher = udiDiRegex.matcher(input);
        Matcher lotNumbersMatcher = lotNumbersRegex.matcher(input);

        // Create a new OutputObj
        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        // Extract Product Number
        if (productNumberMatcher.find()) {
            outputObj.product_number.add(productNumberMatcher.group(1));
            created++;
        }

        // Extract UDI-DI
        if (udiDiMatcher.find()) {
            outputObj.UDI.add(udiDiMatcher.group(1));
            created++;
        }

        // Extract Lot Numbers
        if (lotNumbersMatcher.find()) {
            String lotNumbers = lotNumbersMatcher.group(1);
            String[] lotNumberArray = lotNumbers.split(",\\s*");
            outputObj.lot_number.addAll(Arrays.asList(lotNumberArray));
            created++;
        }

        if(created==3){
            // Add the outputObj to the set
            objInfoList.add(outputObj);
            return;
        }

        created = 0;


        // Define regex patterns
        String catalogNumberPattern2 = "Catalog\\s*Number:?\\s*(\\S+)";
        String udiPattern2 = "UDI:?\\s*(\\d+)";
        String upnPattern2 = "UPN:?\\s*(\\S+)";
        String lotNumberPattern2 = "Lot\\s*Numbers?:?\\s*([\\d\\s]+)";

        // Compile regex patterns
        Pattern catalogNumberRegex2 = Pattern.compile(catalogNumberPattern2,Pattern.CASE_INSENSITIVE);
        Pattern udiRegex2 = Pattern.compile(udiPattern2,Pattern.CASE_INSENSITIVE);
        Pattern upnRegex2 = Pattern.compile(upnPattern2,Pattern.CASE_INSENSITIVE);
        Pattern lotNumberRegex2 = Pattern.compile(lotNumberPattern2,Pattern.CASE_INSENSITIVE);

        // Match patterns
        Matcher catalogNumberMatcher2 = catalogNumberRegex2.matcher(input);
        Matcher udiMatcher2 = udiRegex2.matcher(input);
        Matcher upnMatcher2 = upnRegex2.matcher(input);
        Matcher lotNumberMatcher2 = lotNumberRegex2.matcher(input);

        // Create a new OutputObj
        OutputObj outputObj2 = new OutputObj();
        outputObj2.recall_number = recall_number;

        // Extract Catalog Number
        if (catalogNumberMatcher2.find()) {
            outputObj2.catalog_number.add(catalogNumberMatcher2.group(1));
        }

        // Extract UDI
        if (udiMatcher2.find()) {
            outputObj2.UDI.add(udiMatcher2.group(1));
        }

        // Extract UPN
        if (upnMatcher2.find()) {
            outputObj2.UPN.add(upnMatcher2.group(1));
        }

        // Extract Lot Numbers
        if (lotNumberMatcher2.find()) {
            String lotNumbers = lotNumberMatcher2.group(1);
            String[] lotNumberArray = lotNumbers.split("\\s+");
            outputObj2.lot_number.addAll(Arrays.asList(lotNumberArray));
        }

        // Add the outputObj to the set
        objInfoList.add(outputObj2);
    }

    public static  void Universal(String input, Set<OutputObj> objInfoList,String recall_number){


        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        // Regular expressions to match the fields
        Pattern refPattern = Pattern.compile("REF\\s*(\\S{4,})");
        Pattern modelPattern = Pattern.compile("Model\\s*Number:?\\s*(\\S+)",Pattern.CASE_INSENSITIVE);
        Pattern udiPattern = Pattern.compile("UDI-?/?DI\\s*Code:?\\s*(\\S+)",Pattern.CASE_INSENSITIVE);
        Pattern lotPattern = Pattern.compile("Lot\\s*Numbers:?\\s*((?:\\s+\\S+)+)",Pattern.CASE_INSENSITIVE);

        // Match and extract REF
        Matcher refMatcher = refPattern.matcher(input);
        if (refMatcher.find()) {
            outputObj.ref_number.add(refMatcher.group(1));
        }

        // Match and extract Model Number
        Matcher modelMatcher = modelPattern.matcher(input);
        if (modelMatcher.find()) {
            outputObj.model_number.add(modelMatcher.group(1));
        }

        // Match and extract UDI-DI
        Matcher udiMatcher = udiPattern.matcher(input);
        if (udiMatcher.find()) {
            outputObj.UDI.add(udiMatcher.group(1));
        }

        // Match and extract Lot Numbers
        Matcher lotMatcher = lotPattern.matcher(input);
        if (lotMatcher.find()) {
            String[] lotNumbers = lotMatcher.group(1).trim().split("\\s+");
            outputObj.lot_number.addAll(Arrays.asList(lotNumbers));
        }

        objInfoList.add(outputObj);

    }

    public static  void Aizu(String input, Set<OutputObj> objInfoList,String recall_number){
        // Regular expression to match Models and their corresponding UDI-DI
        Pattern modelUdiPattern = Pattern.compile("Models?:\\s+([\\S\\s]+?)UDI?i?-DI:\\s+([\\S\\s]+)",Pattern.CASE_INSENSITIVE);

        // Match and extract Models and UDI-DI block
        Matcher modelUdiMatcher = modelUdiPattern.matcher(input);
        if (modelUdiMatcher.find()) {
            String modelsBlock = modelUdiMatcher.group(1).trim();
            String udiBlock = modelUdiMatcher.group(2).trim();

            // Split the models and UDI-DI information
            String[] models = modelsBlock.split("\\s+");
            String[] udiPairs = udiBlock.split("\\s+");

            // Iterate through models and corresponding UDI-DI pairs
            int pairCount = Math.min(models.length, udiPairs.length / 2);
            for (int i = 0; i < pairCount; i++) {
                String model = models[i];
                String udi = udiPairs[i * 2 + 1]; // UDI-DI is every second element starting from index 1

                // Create and populate the OutputObj
                OutputObj outputObj = new OutputObj();
                outputObj.recall_number = recall_number;
                outputObj.model_number.add(model);
                outputObj.UDI.add(udi);
                objInfoList.add(outputObj);
            }
        }

        // Regular expression to match Model Number and its corresponding UDI-DI
        Pattern pattern = Pattern.compile("Models?:\\s*(\\S+)\\s*UDI-DI:\\s*(\\S+)",Pattern.CASE_INSENSITIVE);

        // Match the pattern
        Matcher matcher = pattern.matcher(input);

        // If pattern matches
        if (matcher.find()) {

            OutputObj outputObj = new OutputObj();
            outputObj.recall_number = recall_number;

            // Extract Model Number and UDI-DI
            String modelNumber = matcher.group(1);
            String udiDi = matcher.group(2);

            // Populate the OutputObj
            outputObj.model_number.add(modelNumber);
            outputObj.UDI.add(udiDi);
            objInfoList.add(outputObj);

        }

        OutputObj output = new OutputObj();
        output.recall_number = recall_number;

        // Regular expression to match Model Number, UDI-DI, and Serial Numbers
        Pattern pattern2 = Pattern.compile("(\\w+-\\w+),.*?UDI-DI\\s+(\\d+).*?Serial Numbers\\s+((?:\\d+,\\s*)+\\d+)",Pattern.CASE_INSENSITIVE);

        // Match the pattern
        Matcher matcher2 = pattern2.matcher(input);

        // If pattern matches
        if (matcher2.find()) {
            output.model_number.add(matcher2.group(1));
            output.UDI.add(matcher2.group(2));

            // Split serial numbers by comma and space
            String[] serialNumbers = matcher2.group(3).split(",\\s*");
            Collections.addAll(output.serial_number, serialNumbers);
            objInfoList.add(output);
        }
    }

    public static  void Zimmer(String input, Set<OutputObj> objInfoList,String recall_number){

        int created = 0;

        // Define patterns for Item Number, UDI, and Lot Numbers
        Pattern itemPattern = Pattern.compile("Item\\s*Number:\\s*([\\w\\-]+)",Pattern.CASE_INSENSITIVE);
        Pattern udiPattern = Pattern.compile("\\(01\\)(\\d{14})\\(17\\)(\\d{6})\\(10\\)(\\d+)");
        Pattern lotPattern = Pattern.compile("Lot\\s*Numbers:?\\s*((?:\\s*\\d+)+)",Pattern.CASE_INSENSITIVE);

        // Match patterns in the input string
        Matcher itemMatcher = itemPattern.matcher(input);
        Matcher udiMatcher = udiPattern.matcher(input);
        Matcher lotMatcher = lotPattern.matcher(input);

        // Create new OutputObj to store extracted information
        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        // Extract Item Number
        if (itemMatcher.find()) {
            String itemNumber = itemMatcher.group(1).trim();
            outputObj.item_number.add(itemNumber);
            created++;
        }

        // Extract UDI numbers
        while (udiMatcher.find()) {
            outputObj.UDI.add("(01)"+udiMatcher.group(1)+"(17)"+udiMatcher.group(2)+"(10)"+udiMatcher.group(3));
            if(created==1){
                created++;
            }

        }

        // Extract Lot Numbers
        if (lotMatcher.find()) {
            String lotNumbersBlock = lotMatcher.group(1).trim();
            String[] lotNumbers = lotNumbersBlock.split("\\s+");
            for (String lotNumber : lotNumbers) {
                outputObj.lot_number.add(lotNumber.trim());
            }
            created++;
        }

        if(created == 3){
            // Add the populated OutputObj to the Set
            objInfoList.add(outputObj);
            return;
        }

        created = 0;

        // Define patterns for Item Number, GTIN, and Lot Numbers
        Pattern itemPattern2 = Pattern.compile("Item\\s*Number:\\s*([\\w\\-]+)",Pattern.CASE_INSENSITIVE);
        Pattern gtinPattern2 = Pattern.compile("GTIN:\\s*(\\d+)");
        Pattern lotPattern2 = Pattern.compile("Lot\\s*Numbers:\\s*(Lot Number ([\\d\\s]+))",Pattern.CASE_INSENSITIVE);

        // Match patterns in the input string
        Matcher itemMatcher2 = itemPattern2.matcher(input);
        Matcher gtinMatcher2 = gtinPattern2.matcher(input);
        Matcher lotMatcher2 = lotPattern2.matcher(input);

        // Create new OutputObj to store extracted information
        OutputObj outputObj2 = new OutputObj();
        outputObj2.recall_number = recall_number;

        // Extract Item Number
        if (itemMatcher2.find()) {
            String itemNumber = itemMatcher2.group(1).trim();
            outputObj2.item_number.add(itemNumber);
            created++;
        }

        // Extract GTIN
        if (gtinMatcher2.find()) {
            String gtin = gtinMatcher2.group(1).trim();
            outputObj2.GTIN.add(gtin);
            created++;
        }

        // Extract Lot Numbers
        if (lotMatcher2.find()) {
            String lotNumbersBlock = lotMatcher2.group(2).trim();
            String[] lotNumbers = lotNumbersBlock.split("\\s+");
            for (String lotNumber : lotNumbers) {
                outputObj2.lot_number.add(lotNumber.trim());
            }
            created++;
        }

        if(created == 3){
            // Add the populated OutputObj to the Set
            objInfoList.add(outputObj2);
            return;
        }

        created = 0;

        // Define patterns for Item Number, GTIN, and Lot Numbers
        Pattern itemPattern3 = Pattern.compile("Item\\s*Number:\\s*([\\w\\-]+)",Pattern.CASE_INSENSITIVE);
        Pattern gtinPattern3 = Pattern.compile("GTIN:\\s*(\\d+)");
        Pattern lotPattern3 = Pattern.compile("Lot\\s*Numbers:\\s*([\\d\\s]+)",Pattern.CASE_INSENSITIVE);

        // Match patterns in the input string
        Matcher itemMatcher3 = itemPattern3.matcher(input);
        Matcher gtinMatcher3 = gtinPattern3.matcher(input);
        Matcher lotMatcher3 = lotPattern3.matcher(input);

        // Create new OutputObj to store extracted information
        OutputObj outputObj3 = new OutputObj();
        outputObj3.recall_number = recall_number;

        // Extract Item Number
        if (itemMatcher3.find()) {
            String itemNumber = itemMatcher3.group(1).trim();
            outputObj3.item_number.add(itemNumber);
        }

        // Extract GTIN
        if (gtinMatcher3.find()) {
            String gtin = gtinMatcher3.group(1).trim();
            outputObj3.GTIN.add(gtin);
        }

        // Extract Lot Numbers
        if (lotMatcher3.find()) {
            String lotNumbersBlock = lotMatcher3.group(1).trim();
            String[] lotNumbers = lotNumbersBlock.split("\\s+");
            for (String lotNumber : lotNumbers) {
                outputObj3.lot_number.add(lotNumber.trim());
            }
        }

        // Add the populated OutputObj to the Set
        objInfoList.add(outputObj3);


    }


    public static  void Philips(String input, Set<OutputObj> objInfoList,String recall_number){
        String Formatted_input = input.replace("and", ",").replace("&", ",");

        Pattern modelPattern = Pattern.compile("Model No.\\s*([\\d\\,\\s]+)");
        Pattern productPattern = Pattern.compile("Product No.\\s*([\\d\\,\\s]+)");
        Pattern udiPattern = Pattern.compile("UDI(-DI)?:?\\s*([^;]+);");
        Pattern serialPattern = Pattern.compile("Serial No.\\s*([\\d\\w\\s\\-\\,]+)");

        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;


        int created = 0;


        // Matcher for Model Number
        Matcher modelMatcher = modelPattern.matcher(Formatted_input);
        // Matcher for Model Number
        Matcher productMatcher = productPattern.matcher(Formatted_input);

        if (modelMatcher.find()) {
            String modelNumbersBlock = modelMatcher.group(1).trim();
            String[] modelNumbers = modelNumbersBlock.split(",");
            for (String modelNumber : modelNumbers) {
                outputObj.model_number.add(modelNumber.trim());
            }
            created++;
        } else if (productMatcher.find()) {
            String modelNumbersBlock = productMatcher.group(1).trim();
            String[] modelNumbers = modelNumbersBlock.split(",");
            for (String modelNumber : modelNumbers) {
                outputObj.product_number.add(modelNumber.trim());
            }
            created++;
        }

        // Matcher for UDI-DI
        Matcher udiMatcher = udiPattern.matcher(Formatted_input);
        if (udiMatcher.find()) {
            outputObj.UDI.add(udiMatcher.group(2));
            created++;
        }

        // Matcher for Serial Number
        Matcher serialMatcher = serialPattern.matcher(Formatted_input);
        if (serialMatcher.find()) {
            String serialNumbersBlock = serialMatcher.group(1).trim();
            String[] serialNumbers = serialNumbersBlock.split(",");
            for (String serialNumber : serialNumbers) {
                outputObj.serial_number.add(serialNumber.trim());
            }
            created++;
        }

        if(created==3){
            objInfoList.add(outputObj);
            return;
        }

    }

    public static  void Integra(String input, Set<OutputObj> objInfoList,String recall_number){
        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        // Define the regex patterns to extract the required information
        Pattern refPattern = Pattern.compile("Ref No:\\s*([^\\s]+)");
        Pattern catPattern = Pattern.compile("Catalog Number:\\s*([^\\s]+)");
        Pattern udiPattern = Pattern.compile("UDI(-DI)?:?\\s*([^\\s]+)");
        Pattern lotPattern = Pattern.compile("\\b(\\d{7})\\b");

        // Match and extract REF number
        Matcher refMatcher = refPattern.matcher(input);
        Matcher catMatcher = catPattern.matcher(input);

        int created = 0;


        if (refMatcher.find()) {
            outputObj.ref_number.add(refMatcher.group(1));
            created++;
        } else if (catMatcher.find()) {
            outputObj.catalog_number.add(catMatcher.group(1));
            created++;
        }

        // Match and extract UDI
        Matcher udiMatcher = udiPattern.matcher(input);
        if (udiMatcher.find()) {
            outputObj.UDI.add(udiMatcher.group(2));
            created++;
        }

        // Match and extract Lot numbers
        Matcher lotMatcher = lotPattern.matcher(input);
        while (lotMatcher.find()) {
            outputObj.lot_number.add(lotMatcher.group(1));
            if(created==2){
                created++;
            }
        }

        if(created==3){
            // Add the populated OutputObj to the set
            objInfoList.add(outputObj);
        }

    }


    public static  void Medtronic(String input, Set<OutputObj> objInfoList,String recall_number){
        // Extract model number
        Pattern modelPattern = Pattern.compile("Model\\s*Number\\s*([^;]+);");
        Matcher modelMatcher = modelPattern.matcher(input);

        String modelNumber = "";
        if (modelMatcher.find()) {
            modelNumber = modelMatcher.group(1).trim();
        }

        // Extract GTINs and corresponding lot numbers
        Pattern gtinPattern = Pattern.compile("GTIN\\s*(\\d+),?.?\\s*Lot\\s*Serial\\s*Numbers?:\\s*([^;]+);?",Pattern.CASE_INSENSITIVE);
        Matcher gtinMatcher = gtinPattern.matcher(input);



        while (gtinMatcher.find()) {
            String gtin = gtinMatcher.group(1).trim();
            String lotNumbers = gtinMatcher.group(2).trim();

            // Create new OutputObj for each GTIN and its corresponding lot numbers
            OutputObj outputObj = new OutputObj();
            outputObj.recall_number = recall_number;

            outputObj.model_number.add(modelNumber);
            outputObj.GTIN.add(gtin);

            // Split lot numbers by comma and add to the lot_number list
            String[] lots = lotNumbers.split(",\\s*");
            for (String lot : lots) {
                outputObj.lot_number.add(lot.trim());
            }

            // Add the populated OutputObj to the set
            objInfoList.add(outputObj);

        }

    }

    public static  void Hobbs(String input, Set<OutputObj> objInfoList,String recall_number){
        // Pattern to extract Catalog Number
        Pattern catalogPattern = Pattern.compile("Catalog\\s*Number:?\\s*([\\w-]+)");
        Pattern refPattern = Pattern.compile("Ref\\s*:?\\s*([\\w-]+)");
        Matcher catalogMatcher = catalogPattern.matcher(input);
        Matcher refMatcher = refPattern.matcher(input);

        // Pattern to extract UDI-DI
        Pattern udiPattern = Pattern.compile("UDI-?/?DI\\s*:?\\s*(\\w+)");
        Matcher udiMatcher = udiPattern.matcher(input);

        // Pattern to extract Lot Numbers
        Pattern lotPattern = Pattern.compile("Lot\\s*Numbers:\\s*([\\w-\\s]+)");
        Matcher lotMatcher = lotPattern.matcher(input);

        String catalogNumber = "";
        String udi = "";
        String lotNumbers = "";

        // Create a new OutputObj and populate it with the extracted data
        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        if (catalogMatcher.find()) {
            catalogNumber = catalogMatcher.group(1).trim();
            outputObj.catalog_number.add(catalogNumber);
        } else if (refMatcher.find()) {
            outputObj.ref_number.add(refMatcher.group(1).trim());
        }

        if (udiMatcher.find()) {
            udi = udiMatcher.group(1).trim();
            outputObj.UDI.add(udi);
        }

        if (lotMatcher.find()) {
            lotNumbers = lotMatcher.group(1).trim();
        }



        // Split lot numbers by space and add to the lot_number list
        String[] lots = lotNumbers.split("\\s+");
        for (String lot : lots) {
            outputObj.lot_number.add(lot.trim());
        }

        // Add the populated OutputObj to the set
        objInfoList.add(outputObj);
    }

    public static  void ROi(String input, Set<OutputObj> objInfoList,String recall_number){
        // Regex to capture item number, UDI/DI and lot numbers with expiry dates
        String itemPattern = "(?i)item number\\s*(\\d+);\\s*(.*?)(lot numbers:(.*))?$";
        Pattern pattern = Pattern.compile(itemPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String itemNumber = matcher.group(1);
            String restOfInfo = matcher.group(2);
            String lotNumbersSection = matcher.group(4);

            OutputObj obj = new OutputObj();
            obj.recall_number = recall_number;

            // Extract UDI/DI
            String udiPattern = "UDI/DI:?\\s*(\\d+)";
            Pattern udiPatternCompiled = Pattern.compile(udiPattern, Pattern.CASE_INSENSITIVE);
            Matcher udiMatcher = udiPatternCompiled.matcher(restOfInfo);

            if (udiMatcher.find()) {
                String udi = udiMatcher.group(1);
                obj.UDI.add(udi);
            }

            // Extract Lot Numbers
            if (lotNumbersSection != null && !lotNumbersSection.isEmpty()) {
                String lotPattern = "\\s*(\\d+), exp\\s*(\\d{1,2}/\\d{1,2}/\\d{4})";
                Pattern lotPatternCompiled = Pattern.compile(lotPattern, Pattern.CASE_INSENSITIVE);
                Matcher lotMatcher = lotPatternCompiled.matcher(lotNumbersSection);

                while (lotMatcher.find()) {
                    String lotNumber = lotMatcher.group(1);
                    obj.lot_number.add(lotNumber);
                }
            }

            // Set the item number
            obj.item_number.add(itemNumber);

            objInfoList.add(obj);
        }
    }


    public static  void CRBARD(String input, Set<OutputObj> objInfoList, String recall_number) {
        String regex = "REF\\s*(\\w+)\\s*UDI/DI\\s*(\\d+),\\s*Lot/Serial\\s*Numbers:\\s*([\\w,\\s]+)";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        String regex1 = "Catalog\\s*Number\\s*(\\w+)\\s*UDI/DI\\s*(\\d+),\\s*Serial\\s*numbers:?\\s*([\\w\\s,]+)";
        Pattern pattern1 = Pattern.compile(regex1,Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(input);

        OutputObj obj = new OutputObj();
        obj.recall_number=recall_number;
        if (matcher.find()) {

            obj.ref_number.add(matcher.group(1));
            obj.UDI.add(matcher.group(2));
            String[] lotSerialNumbers = matcher.group(3).split(",\\s*");
            obj.lot_number.addAll(Arrays.asList(lotSerialNumbers));
            objInfoList.add(obj);
        }
        else if (matcher1.find()) {
            obj.catalog_number.add(matcher1.group(1));
            obj.UDI.add(matcher1.group(2));
            String[] serialNumbers = matcher1.group(3).split(",\\s*");
            obj.serial_number.addAll(Arrays.asList(serialNumbers));
            objInfoList.add(obj);
        }
    }
    public static void StrykerCorporation(String input, Set<OutputObj> objInfoList, String recall_number){
        String regex1 = "Catalog\\s*Numbers?:?\\s*(\\w+-\\d+-\\d+)\\s*GTIN:?\\s*(\\d+);\\s*Lot\\s*Numbers:?\\s*([\\w\\d,\\s]+)";
        String regex2 = "REF:?\\s*(\\w+),.*?Lot\\s*#\\s*([\\w\\d/]+)/(UDI:?\\s*(\\d+))";


        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);

        Matcher matcher1 = pattern1.matcher(input);
        Matcher matcher2 = pattern2.matcher(input);

        OutputObj obj = new OutputObj();
        obj.recall_number = recall_number;

        if (matcher1.find()) {
            obj.catalog_number.add(matcher1.group(1));
            obj.GTIN.add(matcher1.group(2));
            String[] lotNumbers = matcher1.group(3).split(",\\s*");
            obj.lot_number.addAll(Arrays.asList(lotNumbers));
            objInfoList.add(obj);
        } else if (matcher2.find()) {
            obj.ref_number.add(matcher2.group(1));
            String lotNumber = matcher2.group(2);
            String UDI = matcher2.group(3);
            obj.lot_number.add(lotNumber);
            obj.UDI.add(UDI);
            objInfoList.add(obj);
        }
    }
    public static void AmericanContractSystems(String input, Set<OutputObj> objInfoList, String recall_number) {
        Pattern pattern1 = Pattern.compile("(\\b[A-Z0-9-]+\\b),\\s*UDI/DI\\s*([\\d\\w]+|none),\\s*L?l?ot n?N?umbers:\\s*((\\d+,\\s+exp\\.\\s*\\d{1,2}/\\d{1,2}/\\d{4};\\s*)+)");
        Matcher matcher1 = pattern1.matcher(input);

        Pattern pattern2 = Pattern.compile("REF\\s*([A-Z0-9]+)\\s*UDI/DI\\s*([\\d\\w]+),\\s*Lot\\s*Numbers?:?\\s*([\\d,\\s*]+)");
        Matcher matcher2 = pattern2.matcher(input);

        while (matcher1.find()) {
            OutputObj obj = new OutputObj();
            obj.recall_number = recall_number;
            obj.kit_number.add(matcher1.group(1));
            obj.UDI.add(matcher1.group(2));
            String lotNumbers = matcher1.group(3).replaceAll("\\s+", ""); // Remove all spaces
            Pattern pattern = Pattern.compile("\\d+(?=\\s*(?:,|$))");
            Matcher matcher = pattern.matcher(lotNumbers);
            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                result.append(matcher.group()).append(",");
            }


            obj.lot_number.add(result.toString());
            objInfoList.add(obj);
        }

        while (matcher2.find()) {
            OutputObj obj = new OutputObj();
            obj.recall_number = recall_number;
            obj.ref_number.add(matcher2.group(1));
            obj.UDI.add(matcher2.group(2));
            obj.lot_number.add(matcher2.group(3).trim()); // Trim any extra spaces
            objInfoList.add(obj);

        }
    }
    public static void BiometInc(String input, Set<OutputObj> objInfoList, String recall_number){
        Pattern pattern = Pattern.compile("Item Number:\\s*(\\w+)\\s*Lot Numbers/UDI\\s*(.*?)\\s*$", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String itemNumber = matcher.group(1).trim();
            String lotUdiString = matcher.group(2).trim();

            // Extract lot number and UDI number combinations
            Pattern lotUdiPattern = Pattern.compile("(\\w+)\\s*\\(\\d+\\)\\d+\\(17\\)\\d+\\(10\\)\\d+");
            Matcher lotUdiMatcher = lotUdiPattern.matcher(lotUdiString);

            while (lotUdiMatcher.find()) {
                OutputObj obj = new OutputObj();
                obj.recall_number = recall_number;
                obj.item_number.add(itemNumber);
                obj.lot_number.add(lotUdiMatcher.group(1));
                obj.UDI.add(lotUdiMatcher.group());
                objInfoList.add(obj);
            }
        }
    }

    public static void BiomerieuxInc(String input, Set<OutputObj> objInfoList, String recall_number){
        // Regex pattern to match catalog number, UDI, and batch numbers
        Pattern pattern = Pattern.compile("CATALOG\\s*([-\\w]+)\\s*UDI/DI\\s*(\\d+),\\s*Batch Numbers?:?\\s*([\\d,\\s*]+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            OutputObj obj = new OutputObj();
            obj.recall_number=recall_number;
            obj.catalog_number.add(matcher.group(1).trim());
            obj.UDI.add(matcher.group(2).trim());

            String batchNumbersString = matcher.group(3).trim();
            String[] batchNumbersArray = batchNumbersString.split("\\s*,\\s*");

            for (String batchNumber : batchNumbersArray) {
                obj.batch_number.add(batchNumber.trim());
            }

            objInfoList.add(obj);
        }
    }

    public static void LinkbioCorp(String input, Set<OutputObj> objInfoList, String recall_number){
        Pattern pattern = Pattern.compile("Item Number:\\s*([\\w-/]+)\\s*UDI-DI\\s*:\\s*(\\d+)(?:\\s*All\\s*(.*))?");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            OutputObj obj = new OutputObj();
            obj.recall_number=recall_number;
            obj.item_number.add(matcher.group(1).trim());
            obj.UDI.add(matcher.group(2).trim());

            // Capture and store whatever is written after "All"
            if (matcher.group(3) != null) {
                String lotNumbersDescription = matcher.group(3).trim();
                obj.lot_number.add("All " + lotNumbersDescription);
            }

            objInfoList.add(obj);
        }
    }

    public static void StradisMedicalLLCdbaStradisHealthcare(String input, Set<OutputObj> objInfoList, String recall_number){
        Pattern pattern = Pattern.compile("Item No\\.?\\s*([\\w-/]+).*?UDI/DI\\s*\\(case\\)\\s*([\\w-]+),\\s*UDI/DI\\s*\\(kit\\)\\s*([\\w-]+),\\s*Serial/Lot Numbers:\\s*([\\w-]+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            OutputObj obj = new OutputObj();
            obj.recall_number=recall_number;
            obj.item_number.add(matcher.group(1).trim());
            obj.UDI.add(matcher.group(2).trim());
            obj.UDI.add(matcher.group(3).trim());
            obj.lot_number.add(matcher.group(4).trim());

            objInfoList.add(obj);
        }
    }

    public static Set<OutputObj> extractDeviceInfo(String input, String firmname, String recall) {
        Set<OutputObj> objInfoList = new HashSet<>();
        if (Objects.equals(firmname, "Exactech, Inc.")) {
            DeviceInfoExtractor.Exatech(input, objInfoList, recall);
        } else if (Objects.equals(firmname, "Fresenius Medical Care Holdings, Inc.")) {
            DeviceInfoExtractor.Fresenius(input, objInfoList, recall);
        } else if (Objects.equals(firmname, "MEDLINE INDUSTRIES, LP - Northfield")) {
            DeviceInfoExtractor.Medline(input, objInfoList, recall);
        } else if (Objects.equals(firmname, "Boston Scientific Corporation")) {
            DeviceInfoExtractor.Boston(input, objInfoList, recall);
        } else if (Objects.equals(firmname, "Cardinal Health 200, LLC")) {
            DeviceInfoExtractor.Cardinal(input, objInfoList, recall);
        } else if (Objects.equals(firmname, "Baxter Healthcare Corporation")) {
            DeviceInfoExtractor.Baxter(input, objInfoList, recall);
        } else if (Objects.equals(firmname, "Philips North America")) {
            DeviceInfoExtractor.NonLLCPhilips(input, objInfoList, recall);
        }
        else if(Objects.equals(firmname, "Howmedica Osteonics Corp.")){
            DeviceInfoExtractor.Howmedica(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Becton Dickinson & Co.")){
            DeviceInfoExtractor.Becton(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Angiodynamics, Inc.")){
            DeviceInfoExtractor.Angiodynamics(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Universal Meditech Inc.")){
            DeviceInfoExtractor.Universal(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Aizu Olympus Co., Ltd.")){
            DeviceInfoExtractor.Aizu(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Zimmer, Inc.")){
            DeviceInfoExtractor.Zimmer(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Philips North America Llc")){
            DeviceInfoExtractor.Philips(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Integra LifeSciences Corp.")){
            DeviceInfoExtractor.Integra(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Medtronic Inc., Cardiac Rhythm and Heart Failure (CRHF)")){
            DeviceInfoExtractor.Medtronic(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Hobbs Medical, Inc.")){
            DeviceInfoExtractor.Hobbs(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "ROi CPS LLC")){
            DeviceInfoExtractor.ROi(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "C.R. Bard Inc")){
            DeviceInfoExtractor.CRBARD(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Stryker Corporation")){
            DeviceInfoExtractor.StrykerCorporation(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "American Contract Systems, Inc.")){
            DeviceInfoExtractor.AmericanContractSystems(input,objInfoList,recall);
        }
        else if((Objects.equals(firmname, "Biomerieux Inc"))){
            DeviceInfoExtractor.BiomerieuxInc(input,objInfoList,recall);
        }
        else if((Objects.equals(firmname, "Linkbio Corp."))){
            DeviceInfoExtractor.LinkbioCorp(input,objInfoList,recall);
        }
        else if((Objects.equals(firmname, "Stradis Medical, LLC dba Stradis Healthcare"))){
            DeviceInfoExtractor.StradisMedicalLLCdbaStradisHealthcare(input,objInfoList,recall);
        }
        return objInfoList;

    }

}

