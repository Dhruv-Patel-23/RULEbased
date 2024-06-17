
package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class DeviceInfoExtractor {
    public static void Exatech(String input, Set<OutputObj> objInfoList,String recall){

        // Regular expression patterns
        String itemPattern = "(\\d{3}-\\d{2}-\\d{2})";
        String udiPattern = "UDI/DI\\s([\\d]+)";
        String serialPattern = "Serial Numbers:\\s([\\d,\\s]+)";

        Pattern itemNumPattern = Pattern.compile(itemPattern);
        Pattern udiNumPattern = Pattern.compile(udiPattern);
        Pattern serialNumPattern = Pattern.compile(serialPattern);


        Matcher itemNumMatcher = itemNumPattern.matcher(input);
        Matcher udiNumMatcher = udiNumPattern.matcher(input);
        Matcher serialNumMatcher = serialNumPattern.matcher(input);

        String regex = "\\b(\\w{2}-\\w{3}-\\w{2}-\\d{4,5}), GTIN (\\d{14}), Serial Numbers: ([\\w, ]+);?";
        String regex2 = "(\\d{3}-\\d{2}-\\d{2}), GTIN (\\d{14}), Serial Numbers: ([\\w, ]+);?";

        Pattern re = Pattern.compile(regex);
        Pattern re2 = Pattern.compile(regex2);

        Matcher rema= re.matcher(input);
        Matcher rema2= re2.matcher(input);

        while (rema.find()){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(rema.group(1));
            deviceInfo.GTIN.add(rema.group(2));


            String[] serials = rema.group(3).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));



            objInfoList.add(deviceInfo);

        }

        while (rema2.find()){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(rema2.group(1));
            deviceInfo.GTIN.add(rema2.group(2));
            String[] serials = rema2.group(3).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));
            objInfoList.add(deviceInfo);

        }

        while ((itemNumMatcher.find()) && udiNumMatcher.find() && serialNumMatcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(itemNumMatcher.group(1));
            deviceInfo.UDI.add(udiNumMatcher.group(1));


            String[] serials = serialNumMatcher.group(1).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));



            objInfoList.add(deviceInfo);
        }

    }

    public static  void Fresenius(String input, Set<OutputObj> objInfoList,String recall) {
        String regex = "(?=.*Product Code:\\s*(\\w+))(?=.*UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?))(?=.*Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?))";
//        String regex = "Product Code:\\s*(\\w+)|UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?)|Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?)";
        // Compile the regex
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);


        // Find and extract values
        int i=0;
        while (matcher.find()) {
            if(i>0) break;
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            if (matcher.group(1) != null) {
                deviceInfo.product_number.add(matcher.group(1));
            }
            if (matcher.group(2) != null) {
                deviceInfo.UDI.add(matcher.group(2));
            }
            if (matcher.group(3) != null) {
                deviceInfo.serial_number.add(matcher.group(3));
            }
            i++;
            objInfoList.add(deviceInfo);

        }

        regex = "(?=.*Model numbers\\s*([\\w\\d]+(?:\\s*and\\s*[\\w\\d]+)*))(?=.*UDI-DI\\s*(\\d+))(?=.*Serial Numbers\\s*([\\w\\d\\s]+)).*";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);

        // Use a Set to ensure uniqueness
        Set<OutputObj> uniqueDevices = new HashSet<>();

        // Find and extract values
        while (matcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            if (matcher.group(1) != null) {
                String[] models = matcher.group(1).split("\\s*and\\s*");
                deviceInfo.model_number.addAll(Arrays.asList(models));
            }
            if (matcher.group(2) != null) {
                deviceInfo.UDI.add(matcher.group(2));
            }
            if (matcher.group(3) != null) {
                String[] serials = matcher.group(3).split("\\s+");
                deviceInfo.serial_number.addAll(Arrays.asList(serials));
            }

            // Add to the set (duplicates are automatically ignored)
            uniqueDevices.add(deviceInfo);
        }
        // Add unique devices to the output list
        objInfoList.addAll(uniqueDevices);

        regex = "Model No:\\s*(\\w+);\\s*UDI-DI:\\s*(\\d+);\\s*Serial No.\\s*([\\w\\d,\\s]+)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);

        uniqueDevices.clear();

        while (matcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            if (matcher.group(1) != null) {
                deviceInfo.model_number.add(matcher.group(1));
            }
            if (matcher.group(2) != null) {
                deviceInfo.UDI.add(matcher.group(2));
            }
            if (matcher.group(3) != null) {
                String[] serials = matcher.group(3).split(",\\s*");
                deviceInfo.serial_number.addAll(Arrays.asList(serials));
            }
            uniqueDevices.add(deviceInfo);
        }

        objInfoList.addAll(uniqueDevices);



        regex = "Model Number:\\s*([\\w\\-]+);\\s*UDI/DI \\(Bag\\):\\s*(\\d+);\\s*UDI/DI \\(Case\\):\\s*(\\d+);\\s*All lots\\.";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);

        uniqueDevices.clear();

        while (matcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            if (matcher.group(1) != null) {
                deviceInfo.model_number.add(matcher.group(1));
            }
            if (matcher.group(2) != null) {
                deviceInfo.UDI.add(matcher.group(2));
            }
            if (matcher.group(3) != null) {
                deviceInfo.UDI.add(matcher.group(3));
            }
            deviceInfo.serial_number.add("All lots");
            uniqueDevices.add(deviceInfo);
        }

        objInfoList.addAll(uniqueDevices);


    }

    public static void Medline(String input, Set<OutputObj> objInfoList,String recall){



        // Regular expression to capture REF number, UDI/DI, and Lot Numbers
        String refRegex = "REF\\s*(\\w+)";
        String udiregex = "UDI/DI:?\\s*(\\w+)";

        String lotRegex = "Lot\\s*(Numbers)?(code)?:?\\s*([\\w,\\s]+)";

        // Compile the regex
        Pattern refPattern = Pattern.compile(refRegex);
        Matcher refMatcher = refPattern.matcher(input);

        Pattern udiPattern = Pattern.compile(udiregex);
        Matcher udiMatcher = udiPattern.matcher(input);


        Pattern lot = Pattern.compile(lotRegex);
        Matcher lotMatcher = lot.matcher(input);

        while ((refMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(refMatcher.group(1));
            deviceInfo.UDI.add(udiMatcher.group(1));
            deviceInfo.lot_number.add(lotMatcher.group(3));




            objInfoList.add(deviceInfo);
        }
        //----------------------------------------------------------------------------------------
        refRegex = "REF\\s*(\\w+)";
        udiregex = "GTIN:?\\s*(\\w+)";
        lotRegex = "Lot\\s*Numbers:?\\s*([\\w,\\s]+)";

        // Compile the regex
        refPattern = Pattern.compile(refRegex);
        refMatcher = refPattern.matcher(input);

        udiPattern = Pattern.compile(udiregex);
        udiMatcher = udiPattern.matcher(input);


        lot = Pattern.compile(lotRegex);
        lotMatcher = lot.matcher(input);

        while ((refMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(refMatcher.group(1));
            deviceInfo.GTIN.add(udiMatcher.group(1));
            deviceInfo.lot_number.add(lotMatcher.group(1));




            objInfoList.add(deviceInfo);
        }
        //----------------------------------------------------------------------------------------


        String reorderRegex = "Reorder\\s*Number \\w+";
        udiregex = "UDI/DI\\s\\d{14}[\\s()\\w]*,?\\s*\\d{14}[\\s()\\w]*";
        lotRegex = "Lot Numbers:\\s*([\\w+,\\s]+)";

        Pattern reorderPattern = Pattern.compile(reorderRegex);
        Matcher reoderMatcher = reorderPattern.matcher(input);

        udiPattern = Pattern.compile(udiregex);
        udiMatcher = udiPattern.matcher(input);


        lot = Pattern.compile(lotRegex);
        lotMatcher = lot.matcher(input);

        while (reoderMatcher.find() && udiMatcher.find() && lotMatcher.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(reoderMatcher.group().substring(14));
            deviceInfo.UDI.add(udiMatcher.group().substring(7));
            deviceInfo.lot_number.add(lotMatcher.group().substring(13));
            objInfoList.add(deviceInfo);
        }




        String prodregex = "Product\\sCode\\s[0-9A-Za-z]+";
        udiregex = "UDI/DI\\s\\d{14}[\\s()\\w]*,?\\s*\\d{14}[\\s()\\w]*";
        lotRegex = "Lot\\sNumbers:\\s*([\\w+,\\s]+)";

        Pattern prodPattern = Pattern.compile(prodregex);
        udiPattern=Pattern.compile(udiregex);
        Pattern lotPattern=Pattern.compile(lotRegex);

        Matcher prodMatcher = prodPattern.matcher(input);
        udiMatcher = udiPattern.matcher(input);
        lotMatcher = lotPattern.matcher(input);

        while ((prodMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(prodMatcher.group().substring(12));
            deviceInfo.UDI.add(udiMatcher.group().substring(6));
            deviceInfo.lot_number.add(lotMatcher.group().substring(12));




            objInfoList.add(deviceInfo);
        }


        //--------------------------------------

        String reg1 = "Item\\s*Number:\\s*(\\w+)";
        String reg3 = "UDI/GTIN\\s*[\\w()]*:?\\s*(\\w+),\\s*UDI/GTIN\\s*[\\w()]*:?\\s*(\\w+)";
        String reg2 = "Lot\\s*Numbers?:?\\s*(\\w+)";

        Pattern regPattern1 = Pattern.compile(reg1);
        Pattern regPattern2 = Pattern.compile(reg2);
        Pattern regPattern3 = Pattern.compile(reg3);

        Matcher regMatcher1 = regPattern1.matcher(input);
        Matcher regMatcher2 = regPattern2.matcher(input);
        Matcher regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher3.group(1));
            deviceInfo.UDI.add(regMatcher3.group(2));
            deviceInfo.lot_number.add(regMatcher2.group(1));

            objInfoList.add(deviceInfo);
        }
        //--------------------------------------

        reg1 = "Item\\s*Number:\\s*(\\w+)";
        reg3 = "[\\w()]*\\s*UDI/GTIN\\s*:?\\s*(\\w+),\\s*[\\w()]*\\s*UDI/GTIN\\s*:?\\s*(\\w+)";
        reg2 = "Lot\\s*Numbers?:?\\s*(\\w+)";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher3.group(1));
            deviceInfo.UDI.add(regMatcher3.group(2));
            deviceInfo.lot_number.add(regMatcher2.group(1));

            objInfoList.add(deviceInfo);
        }//--------------------------------------

        reg1 = "Model\\s*Numbers?:?\\s*(\\w+)";
        reg2 = "UDI/DI\\s*:?\\(\\w*\\)\\s*(\\w+),\\s*UDI/DI\\s*:?\\(\\w*\\)\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.UDI.add(regMatcher2.group(2));


            objInfoList.add(deviceInfo);
        }
        //--------------------------------------

        reg1 = "REF\\s*(\\w+)";
        reg2 = "GTIN\\s*\\(01\\)\\s*(\\w+)";
        reg3="Lot\\s*Number\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(regMatcher1.group(1));
            deviceInfo.GTIN.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
        }
        //--------------------------------------

        reg1 = "REF\\s*(\\w+)";
        reg2 = "GTIN\\s*(\\w+)";
        reg3="Batch\\s*Numbers?:?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(regMatcher1.group(1));
            deviceInfo.GTIN.add(regMatcher2.group(1));
            deviceInfo.batch_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
        }
        //--------------------------------------

        reg1 = "Reorder\\s*#:?\\s*(\\w+)";
        reg2 = "UDI/DI:?\\s*\\(01\\)\\s*(\\w+)\\s*\\(\\w+\\),\\s*\\(01\\)\\s*(\\w+)\\s*\\(\\w+\\)";
        reg3="Lot\\s*Numbers?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
        }

        //--------------------------------------

        reg1 = "Reorder\\s*:?\\s*(\\w+)";
        reg2 = "UDI/DI:?\\s*(\\w+)";
        reg3="Lot\\s*C?c?ode?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
        }        //--------------------------------------

        reg1 = "Reorder\\s*Number:?\\s*(\\w+)";
        reg2 = "GTIN:?\\s*(\\w+)";
        reg3="Lot\\s*#?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
        regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
        regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.reorder_number.add(regMatcher1.group(1));
            deviceInfo.GTIN.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
        }
        //------------------------------------------


        reg1 = "Model\\s*Number\\s*:?\\s*(\\w+)";
        reg2 = "UDI/DI:?\\s*(\\(\\w+\\))?\\s*\\(01\\)\\s*(\\w+)\\s*,\\s*UDI/DI:?\\s*(\\(\\w+\\))?\\s*\\(01\\)\\s*(\\w+)";
        reg3="Lot\\s*Numbers?:?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(4));
            deviceInfo.UDI.add(regMatcher2.group(2));
            deviceInfo.lot_number.add(regMatcher3.group(1));


            objInfoList.add(deviceInfo);
        }
        //--------------------------------------

        reg1 = "PN:?\\s*(\\w+)";
        reg2 = "Item\\s*(\\w+)";
        String reg4 = "UDI/DI\\s*\\(\\w+\\)\\s*(\\w+)";
        reg3="Lot\\s*numbers?\\s*(\\w+)";


        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);
        Pattern regPattern4 = Pattern.compile(reg4);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);
        Matcher regMatcher4 = regPattern4.matcher(input);


        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()&& regMatcher4.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group(1));
            deviceInfo.item_number.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher3.group(1));
            deviceInfo.UDI.add(regMatcher4.group(1));


            objInfoList.add(deviceInfo);
        }


        //-------------------------------------------------------------------------------------
        reg1="Model\\s*Number\\s*:\\s*(\\w+),?;?\\s*UPC\\s*Number:\\s*(\\w+)";
        regPattern1 = Pattern.compile(reg1);
        regMatcher1=regPattern1.matcher(input);





        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.model_number.add(regMatcher1.group(1));
            deviceInfo.UPC.add(regMatcher1.group(2));



            objInfoList.add(deviceInfo);
        }


    }

    public static void Boston(String input, Set<OutputObj> objInfoList,String recall){

        String modelRegex = "MODEL\\s*-\\s*[\\w\\s]*\\(\\w*\\)";
        String refRegex = "REF\\s*\\w*";
        String udiRegex = "UDI/DI\\s*\\w{14}";
        String batchRegex = "Batch\\s*Numbers?:[\\s\\w,]+";

        Pattern modelPattern = Pattern.compile(modelRegex);
        Pattern refPattern = Pattern.compile(refRegex);
        Pattern udiPattern = Pattern.compile(udiRegex);
        Pattern batchPattern = Pattern.compile(batchRegex);


        Matcher modelMatcher = modelPattern.matcher(input);
        Matcher refMatcher = refPattern.matcher(input);
        Matcher udiMatcher = udiPattern.matcher(input);
        Matcher batchMatcher = batchPattern.matcher(input);

        while ((refMatcher.find()) && udiMatcher.find() && modelMatcher.find() && batchMatcher.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(refMatcher.group().substring(4));
            deviceInfo.UDI.add(udiMatcher.group().substring(6));
            deviceInfo.model_number.add(modelMatcher.group().substring(8));
            deviceInfo.batch_number.add(batchMatcher.group().substring(15));





            objInfoList.add(deviceInfo);
        }

        //--------------------------------------------------------------------------------------------------------

        String gtinRegex1 = "GTIN numbers in the U.S.:[\\w\\s,]*";
        String gtinRegex2 = "GTIN numbers OUS:[\\w\\s,]*";

        Pattern gtinPattern1 = Pattern.compile(gtinRegex1);
        Pattern gtinPattern2 = Pattern.compile(gtinRegex2);

        Matcher gtinMatcher1 = gtinPattern1.matcher(input);
        Matcher gtinMatcher2 = gtinPattern2.matcher(input);

        while ((gtinMatcher1.find()) && gtinMatcher2.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.GTIN.add(gtinMatcher1.group().substring(25));
            deviceInfo.GTIN.add(gtinMatcher2.group().substring(17));

            deviceInfo.serial_number.add("All serial numbers");






            objInfoList.add(deviceInfo);
        }
        //--------------------------------------------------------------------------------------------------------




        String catalogRegex = "Catalog\\s*number\\s*\\w*";
        gtinRegex1 = "GTIN\\s*\\w*";
        batchRegex="Lot[\\w/\\s]*:\\s*[\\w\\s,]*";

        Pattern catalogPattern = Pattern.compile(catalogRegex);
        gtinPattern1 = Pattern.compile(gtinRegex1);
        batchPattern = Pattern.compile(batchRegex);

        // Create matchers for the input string
        Matcher catalogMatcher = catalogPattern.matcher(input);
        gtinMatcher1 = gtinPattern1.matcher(input);
        batchMatcher = batchPattern.matcher(input);



        while ((gtinMatcher1.find()) && catalogMatcher.find() && batchMatcher.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.GTIN.add(gtinMatcher1.group().substring(5));
            deviceInfo.catalog_number.add(catalogMatcher.group().substring(15));
            deviceInfo.batch_number.add(batchMatcher.group().substring(19));

            objInfoList.add(deviceInfo);
        }

        //--------------------------------------------------------------------------------------------------------


        String regexGTIN = "GTIN\\):\\s*(\\d+)";
        String regexUPN = "(Outer\\s*box\\s*UPN?#\\s*([A-Za-z0-9]+),\\s*Inner\\s*box\\s*UPN\\s*#\\s*([A-Za-z0-9]+))";
        String regexLotBatch = "Lot\\s*/\\s*Batch\\s*#?\\s([\\d,\\s]+)";

        // Compile the regex patterns
        Pattern patternGTIN = Pattern.compile(regexGTIN);
        Pattern patternUPN = Pattern.compile(regexUPN);
        Pattern patternLotBatch = Pattern.compile(regexLotBatch);

        // Matchers for each regex pattern
        Matcher matcherGTIN = patternGTIN.matcher(input);
        Matcher matcherUPN = patternUPN.matcher(input);
        Matcher matcherLotBatch = patternLotBatch.matcher(input);

        // Store results in lists

        List<String> gtins = new ArrayList<>();
        List<String[]> upns = new ArrayList<>();
        List<String[]> lotBatches = new ArrayList<>();

        // Extract GTINs

        while(matcherGTIN.find() && matcherUPN.find() && matcherLotBatch.find()){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.GTIN.add(matcherGTIN.group(1));
            deviceInfo.UPN.add(matcherUPN.group(2));
            deviceInfo.UPN.add( matcherUPN.group(3));
            deviceInfo.lot_number.add(String.join(", ",matcherLotBatch.group(1).trim().split(",\\s*")));


            objInfoList.add(deviceInfo);
        }
        //--------------------------------------------------------------------------------------------------------

        regexUPN = "UPN:\\s*\\w+";
        gtinRegex1 ="GTIN:\\s*\\w+";
        regexLotBatch ="Lot\\s*Numbers?:[\\s,\\d]+";

        patternUPN = Pattern.compile(regexUPN);
        gtinPattern1 = Pattern.compile(gtinRegex1);
        patternLotBatch = Pattern.compile(regexLotBatch);


        matcherUPN = patternUPN.matcher(input);
        gtinMatcher1 = gtinPattern1.matcher(input);
        matcherLotBatch = patternLotBatch.matcher(input);


        while(gtinMatcher1.find() && matcherUPN.find() && matcherLotBatch.find()){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.GTIN.add(gtinMatcher1.group().substring(6));
            deviceInfo.UPN.add(matcherUPN.group().substring(5));

            deviceInfo.lot_number.add(String.join(", ",matcherLotBatch.group().trim().split(",\\s*")).substring(13));


            objInfoList.add(deviceInfo);
        }


//--------------------------------------------------------------------------------------------------------

        String regex = "UPN\\s([A-Za-z0-9]+),\\sGTIN\\s\\(UDI-DI\\)\\s(\\d+),\\sLots?\\s([\\d,\\s]+)";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Matcher for the regex pattern
        Matcher matcher = pattern.matcher(input);


        // Extract matches
        while (matcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.UPN.add(matcher.group(1));
            deviceInfo.GTIN.add(matcher.group(2));
            deviceInfo.lot_number.add(String.join(",",matcher.group(3).trim().split(",\\s*")));
            objInfoList.add(deviceInfo);
        }
//---------------------------------------------------------------------------------------------------------

        regex = "([a-z])\\)\\s([A-Za-z0-9]+),\\sUDI/DI\\s(\\d+),\\sALL\\sLOT\\sCODES";

        // Compile the regex pattern
        pattern = Pattern.compile(regex);

        // Matcher for the regex pattern
        matcher = pattern.matcher(input);

        while (matcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UPN.add(matcher.group(2));
            deviceInfo.UDI.add(matcher.group(3));
            deviceInfo.lot_number.add("ALL LOT CODES");

            objInfoList.add(deviceInfo);
        }

        //--------------------------------------------------------------------------------------------------


        String regex4 = "REF\\s*\\w*", regex2="UDI/DI\\s*\\w+",regex3="Lot\\s*Numbers?:[\\s,\\d]*";

        // Compile the regex pattern
        Pattern pattern4 = Pattern.compile(regex4);

        Pattern pattern2 = Pattern.compile(regex2);

        Pattern pattern3 = Pattern.compile(regex3);

        // Matcher for the regex pattern
        Matcher matcher4 = pattern4.matcher(input);

        Matcher matcher2 = pattern2.matcher(input);

        Matcher matcher3 = pattern3.matcher(input);


        // Extract matches
        while (matcher4.find() && matcher2.find() && matcher3.find()) {

            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.ref_number.add(matcher4.group().substring(4));
            deviceInfo.UDI.add(matcher2.group().substring(8));
            deviceInfo.lot_number.add(String.join(",",matcher3.group().trim().split(",\\s*")).substring(13));


            objInfoList.add(deviceInfo);
        }


//        regexUPN = "UPN\\s*\\w+";
//        gtinRegex1 ="GTIN\\s*\\(UDI-DI\\)\\s*\\w+";
//        regexLotBatch ="Lots?\\s*[\\s,\\d]+";
//
//        patternUPN = Pattern.compile(regexUPN);
//        gtinPattern1 = Pattern.compile(gtinRegex1);
//        patternLotBatch = Pattern.compile(regexLotBatch);
//
//
//        matcherUPN = patternUPN.matcher(input);
//        gtinMatcher1 = gtinPattern1.matcher(input);
//        matcherLotBatch = patternLotBatch.matcher(input);
//
//
//        while(matcherUPN.find() && gtinMatcher1.find() &&  matcherLotBatch.find()){
//            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
//
//            deviceInfo.GTIN.add(gtinMatcher1.group().substring(4));
//            deviceInfo.UPN.add(matcherUPN.group().substring(13));
//
//            deviceInfo.lot_number.add(String.join(", ",matcherLotBatch.group().trim().split(",\\s*")).substring(4));
//
//
//            objInfoList.add(deviceInfo);
//        }


//
//        while (matcherGTIN.find()) {
//            gtins.add(matcherGTIN.group(1));
//        }
//
//        // Extract UPNs
//        while (matcherUPN.find()) {
//            upns.add(new String[]{matcherUPN.group(2), matcherUPN.group(3)});
//        }
//
//        // Extract Lot/Batch numbers
//        while (matcherLotBatch.find()) {
//            String[] lots = matcherLotBatch.group(1).trim().split(",\\s*");
//            lotBatches.add(lots);
//        }
//
//        // Print results, ensuring we respect the correlation
//        for (int i = 0; i < gtins.size(); i++) {
//            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
//
//            if (i < upns.size() && i < lotBatches.size()) {
//                String[] upnPair = upns.get(i);
//                String[] lots = lotBatches.get(i);
//                String gtin = gtins.get(i);
//
//                deviceInfo.GTIN.add(gtin);
//               deviceInfo.UPN.add(upnPair[0]);
//                deviceInfo.UPN.add(upnPair[1]);
//                deviceInfo.lot_number.add(String.join(", ", lots));
//
//            }


        //}





    }

    public static void Cardinal (String input, Set<OutputObj> objInfoList,String recall){

        String reg1 ="Model\\s*\\d*";
        String reg2="Lot\\s*#\\w+";
        String reg3 = "UDI-?/?DI:\\s*[\\w\\s(),]+";

        Pattern regPattern1 = Pattern.compile(reg1);
        Pattern regPattern2 = Pattern.compile(reg2);
        Pattern regPattern3 = Pattern.compile(reg3);

        Matcher regMatcher1 = regPattern1.matcher(input);
        Matcher regMatcher2 = regPattern2.matcher(input);
        Matcher regMatcher3 = regPattern3.matcher(input);

        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group().substring(6));
            deviceInfo.lot_number.add(regMatcher2.group().substring(5));
            deviceInfo.UDI.add(regMatcher3.group().substring(8));

            objInfoList.add(deviceInfo);




        }


        //-----------------------------------------------------------------------

        reg1= "REF\\s*[\\w-]+";
        reg2="UDI/DI\\s*\\d{14}\\s*\\(Case\\),(\\s*\\d{14}\\s*\\(Box\\),)?\\s*\\s*\\d{14}\\s*\\(Each\\)";
        reg3 = "Lot\\s*Numbers?:[\\w\\s,]+";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(regMatcher1.group().substring(4));
            deviceInfo.UDI.add(regMatcher2.group().substring(7));
            deviceInfo.lot_number.add(regMatcher3.group().substring(13));

            objInfoList.add(deviceInfo);




        }

        //--------------------------------------------------

        String regex = "Cat\\.\\s*(\\w+)\\s*-\\s*Lot\\s*#(\\w+),\\s*E?e?xp\\.\\s[\\d/]+,\\s*UDI-DI\\s*(\\d+)";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Matcher for the regex pattern
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.catalog_number.add(matcher.group(1));
            deviceInfo.lot_number.add(matcher.group(2));
            deviceInfo.UDI.add(matcher.group(3));

            objInfoList.add(deviceInfo);
        }


//--------------------------------------------------
        reg1= "Product\\s*Code:\\s*\\w+";
        reg2="UDI/?-?DI:?\\s*\\d{14}\\s*-\\s*each,\\s*\\d{14}\\s*-\\s*box,\\s*\\d{14}\\s*-\\s*case";
        reg3="Lot\\s*Numbers?:?\\s*[\\w,\\s]+";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.product_number.add(regMatcher1.group().substring(14));
            deviceInfo.UDI.add(regMatcher2.group().substring(7));
            deviceInfo.lot_number.add(regMatcher3.group().substring(13));

            objInfoList.add(deviceInfo);



        }

//--------------------------------------------------

        reg1="UDI/DI\\s*\\d{14}\\s\\(cs\\),\\s*\\d{14}\\s*\\(ea\\)";

        reg2="Lot\\s*Numbers?:?(\\s*[\\w,\\s]+)\\s*(REF\\s*\\d+)?";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);


        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);

        while( regMatcher2.find() && regMatcher1.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group().substring(7));
            deviceInfo.lot_number.add(regMatcher2.group(1));
            if(regMatcher2.group(2)!=null){
                deviceInfo.ref_number.add(regMatcher2.group(2));
            }

            objInfoList.add(deviceInfo);

        }




    }

    public static void Baxter(String input, Set<OutputObj> objInfoList,String recall) {
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
        }



        //-------------------------------------------------------------------------------------
//
        reg1 = "REF\\s*:?\\s*([\\w-]+)";
        reg3 ="Serial\\s*Numbers?\\s*:?\\s*([\\w\\s,]+)";

        regPattern1 = Pattern.compile(reg1);
        regPattern2 = Pattern.compile(reg2);
        regPattern3 = Pattern.compile(reg3);

        regMatcher1 = regPattern1.matcher(input);
        regMatcher2 = regPattern2.matcher(input);
        regMatcher3 = regPattern3.matcher(input);

        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.ref_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher2.group(1));
            deviceInfo.serial_number.add(regMatcher3.group(1));

            objInfoList.add(deviceInfo);
        }

        //-------------------------------------------------------------------------------------
        reg1="([-\\w]+),\\s*UDI/DI\\s*(\\d+)";

        regPattern1 = Pattern.compile(reg1);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.item_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher1.group(2));


            objInfoList.add(deviceInfo);
        }

        //-------------------------------------------------------------------------------------
        reg1="P?p?roduct\\s*C?c?ode:?\\s*(\\w+),\\s*UDI/DI:?\\s*(\\d+)";
        regPattern1 = Pattern.compile(reg1);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group(2));
            deviceInfo.product_number.add(regMatcher1.group(1));


            objInfoList.add(deviceInfo);
        }
        //-------------------------------------------------------------------------------------
        reg1="([\\w-]+):\\s*UDI/DI\\s*([\\w-]+)";
        regPattern1 = Pattern.compile(reg1);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;

            deviceInfo.UDI.add(regMatcher1.group(2));
            deviceInfo.product_number.add(regMatcher1.group(1));


            objInfoList.add(deviceInfo);
        }

        //-------------------------------------------------------------------------------------
        reg1="UDI/DI\\s*(\\w+),\\s*Lot\\s*Numbers?:?\\s*(\\w+)";
        regPattern1 = Pattern.compile(reg1);
        regPattern1 = Pattern.compile(reg1);


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
        }






    }

    public static void NonLLCPhilips(String input, Set<OutputObj> objInfoList,String recall){

        String reg1 ="Model\\s*No[\\s.]+(\\d+)\\s*UDI-DI\\s*([\\w/]+)";


        Pattern regPattern1 = Pattern.compile(reg1);


        Matcher regMatcher1 = regPattern1.matcher(input);




        while(regMatcher1.find()){
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;
            deviceInfo.model_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher1.group(2));

            objInfoList.add(deviceInfo);


        }


        //----------------------------------------------------------------------------

        reg1="Product\\s*Number\\s*(\\w+)";
        String reg2="UDI-DI\\s*([-\\w]+)";
        String reg3="Serial\\s*Numbers?\\s*([\\w\\s,]+)";



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
        }        //----------------------------------------------------------------------------

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
        }


        //----------------------------------

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
        }
        //----------------------------------

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
        }      //----------------------------------

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
        }

    }

    public static void Howmedica(String input, Set<OutputObj> objInfoList,String recall_number) {

        int created = 0;

        // Define regex patterns
        String partNoPattern = "Part No\\. ([\\w-]+)";
        String gtinPattern = "GTIN: (\\d+)";
        String lotNoPattern = "Lot No\\. ([\\w, ]+)";

        // Compile regex patterns
        Pattern partNoRegex = Pattern.compile(partNoPattern);
        Pattern gtinRegex = Pattern.compile(gtinPattern);
        Pattern lotNoRegex = Pattern.compile(lotNoPattern);

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

            for (String lotNo : lotNoArray) {
                outputObj.lot_number.add(lotNo);
            }
            created++;
        }

        if (created == 3){
            // Add the outputObj to the set
            objInfoList.add(outputObj);
            return;
        }

        created = 0;




        String partNumberPattern2 = "Part Number:\\s*([\\w-]+)";
        String gtinPattern2 = "GTIN:\\s*(\\d+)";
        String lotNumbersPattern2 = "Lot NU?u?mbers:\\s*([A-Z\\w, ]+)";

        // Compile regex patterns
        Pattern partNumberRegex2 = Pattern.compile(partNumberPattern2);
        Pattern gtinRegex2 = Pattern.compile(gtinPattern2);
        Pattern lotNumbersRegex2 = Pattern.compile(lotNumbersPattern2);

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

            for (String lotNumber : lotNumberArray) {
                outputObj2.lot_number.add(lotNumber);
            }
            created++;
        }

        if (created == 3){
            // Add the outputObj to the set
            objInfoList.add(outputObj2);
            return;
        }

        created = 0;

        String catalogNumberPattern3 = "Catalog Number: ([\\w-]+)";
        String udiDiPattern3 = "UDI-DI: ([\\d\\(\\)]+)";
        String lotNumberPattern3 = "Lot Number:\\s*(\\d+(?: \\d+)*)";

        // Compile regex patterns
        Pattern catalogNumberRegex3 = Pattern.compile(catalogNumberPattern3);
        Pattern udiDiRegex3 = Pattern.compile(udiDiPattern3);
        Pattern lotNumberRegex3 = Pattern.compile(lotNumberPattern3);

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

            for (String lotNumber : lotNumberArray) {
                outputObj3.lot_number.add(lotNumber);
            }
            created++;
        }

        if(created == 3){
            // Add the outputObj to the set
            objInfoList.add(outputObj3);
            return;
        }

        created = 0;


        String productNumberPattern4 = "Product Number:\\s*([\\w-]+)";
        String gtinPattern4 = "GTIN:\\s*(\\d+)";
        String lotNumbersPattern4 = "Lot Numbers:\\s*(\\d+(?:,\\s*\\d+)*)";

        // Compile regex patterns
        Pattern productNumberRegex4 = Pattern.compile(productNumberPattern4);
        Pattern gtinRegex4 = Pattern.compile(gtinPattern4);
        Pattern lotNumbersRegex4 = Pattern.compile(lotNumbersPattern4);

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

            for (String lotNumber : lotNumberArray) {
                outputObj4.lot_number.add(lotNumber);
            }

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
        Pattern entryRegex = Pattern.compile(entryPattern);

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
            for (String lotNumber : lotNumberArray) {
                outputObj.lot_number.add(lotNumber);
            }

            // Add the outputObj to the set
            objInfoList.add(outputObj);
        }
    }


    public static  void Angiodynamics(String input, Set<OutputObj> objInfoList,String recall_number){

        int created = 0;

        // Define regex patterns
        String productNumberPattern = "Product Number:\\s*([\\w]+)";
        String udiDiPattern = "UDI-DI:\\s*(\\d+)";
        String lotNumbersPattern = "Lot Numbers:\\s*([\\d,\\s]+)";

        // Compile regex patterns
        Pattern productNumberRegex = Pattern.compile(productNumberPattern);
        Pattern udiDiRegex = Pattern.compile(udiDiPattern);
        Pattern lotNumbersRegex = Pattern.compile(lotNumbersPattern);

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
            for (String lotNumber : lotNumberArray) {
                outputObj.lot_number.add(lotNumber);
            }
            created++;
        }

        if(created==3){
            // Add the outputObj to the set
            objInfoList.add(outputObj);
            return;
        }

        created = 0;


        // Define regex patterns
        String catalogNumberPattern2 = "Catalog Number: (\\S+)";
        String udiPattern2 = "UDI: (\\d+)";
        String upnPattern2 = "UPN: (\\S+)";
        String lotNumberPattern2 = "Lot Number: ([\\d\\s]+)";

        // Compile regex patterns
        Pattern catalogNumberRegex2 = Pattern.compile(catalogNumberPattern2);
        Pattern udiRegex2 = Pattern.compile(udiPattern2);
        Pattern upnRegex2 = Pattern.compile(upnPattern2);
        Pattern lotNumberRegex2 = Pattern.compile(lotNumberPattern2);

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
            for (String lotNumber : lotNumberArray) {
                outputObj2.lot_number.add(lotNumber);
            }
        }

        // Add the outputObj to the set
        objInfoList.add(outputObj2);
    }

    public static  void Universal(String input, Set<OutputObj> objInfoList,String recall_number){


        OutputObj outputObj = new OutputObj();
        outputObj.recall_number = recall_number;

        // Regular expressions to match the fields
        Pattern refPattern = Pattern.compile("REF\\s(\\S{4,})");
        Pattern modelPattern = Pattern.compile("Model Number:\\s(\\S+)");
        Pattern udiPattern = Pattern.compile("UDI-DI Code:\\s(\\S+)");
        Pattern lotPattern = Pattern.compile("Lot Numbers:\\s*((?:\\s+\\S+)+)");

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
            for (String lotNumber : lotNumbers) {
                outputObj.lot_number.add(lotNumber);
            }
        }

        objInfoList.add(outputObj);

    }

    public static  void Aizu(String input, Set<OutputObj> objInfoList,String recall_number){
        // Regular expression to match Models and their corresponding UDI-DI
        Pattern modelUdiPattern = Pattern.compile("Models?:\\s+([\\S\\s]+?)UDI?i?-DI:\\s+([\\S\\s]+)");

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
        Pattern pattern = Pattern.compile("Models?:\\s*(\\S+)\\s*UDI-DI:\\s*(\\S+)");

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
        Pattern pattern2 = Pattern.compile("(\\w+-\\w+),.*?UDI-DI\\s+(\\d+).*?Serial Numbers\\s+((?:\\d+,\\s*)+\\d+)");

        // Match the pattern
        Matcher matcher2 = pattern2.matcher(input);

        // If pattern matches
        if (matcher2.find()) {
            output.model_number.add(matcher2.group(1));
            output.UDI.add(matcher2.group(2));

            // Split serial numbers by comma and space
            String[] serialNumbers = matcher2.group(3).split(",\\s*");
            for (String serialNumber : serialNumbers) {
                output.serial_number.add(serialNumber);
            }
            objInfoList.add(output);
        }
    }

    public static  void Zimmer(String input, Set<OutputObj> objInfoList,String recall_number){

        int created = 0;

        // Define patterns for Item Number, UDI, and Lot Numbers
        Pattern itemPattern = Pattern.compile("Item Number:\\s*([\\w\\-]+)");
        Pattern udiPattern = Pattern.compile("\\(01\\)(\\d{14})\\(17\\)(\\d{6})\\(10\\)(\\d+)");
        Pattern lotPattern = Pattern.compile("Lot Numbers:\\s*((?:\\s*\\d+)+)");

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
        Pattern itemPattern2 = Pattern.compile("Item Number:\\s*([\\w\\-]+)");
        Pattern gtinPattern2 = Pattern.compile("GTIN:\\s*(\\d+)");
        Pattern lotPattern2 = Pattern.compile("Lot Numbers:\\s*(Lot Number ([\\d\\s]+))");

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
        Pattern itemPattern3 = Pattern.compile("Item Number:\\s*([\\w\\-]+)");
        Pattern gtinPattern3 = Pattern.compile("GTIN:\\s*(\\d+)");
        Pattern lotPattern3 = Pattern.compile("Lot Numbers:\\s*([\\d\\s]+)");

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
        Pattern modelPattern = Pattern.compile("Model Number\\s*([^;]+);");
        Matcher modelMatcher = modelPattern.matcher(input);

        String modelNumber = "";
        if (modelMatcher.find()) {
            modelNumber = modelMatcher.group(1).trim();
        }

        // Extract GTINs and corresponding lot numbers
        Pattern gtinPattern = Pattern.compile("GTIN\\s*(\\d+),?.? Lot Serial N?n?umbers?:\\s*([^;]+);?");
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
        Pattern catalogPattern = Pattern.compile("Catalog Number:\\s*([\\w-]+)");
        Pattern refPattern = Pattern.compile("Ref:\\s*([\\w-]+)");
        Matcher catalogMatcher = catalogPattern.matcher(input);
        Matcher refMatcher = refPattern.matcher(input);

        // Pattern to extract UDI-DI
        Pattern udiPattern = Pattern.compile("UDI-DI:\\s*(\\w+)");
        Matcher udiMatcher = udiPattern.matcher(input);

        // Pattern to extract Lot Numbers
        Pattern lotPattern = Pattern.compile("Lot Numbers:\\s*([\\w-\\s]+)");
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
            String udiPattern = "UDI/DI\\s*(\\d+)";
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
        String regex = "REF (\\w+) UDI/DI (\\d+), Lot/Serial Numbers: ([\\w\\d,\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        String regex1 = "Catalog Number (\\w+) UDI/DI (\\d+), Serial numbers: ([\\w\\s,]+)";
        Pattern pattern1 = Pattern.compile(regex1);
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
        String regex1 = "Catalog Number\\s*(\\w+-\\d+-\\d+) GTIN:\\s*(\\d+); Lot Numbers:\\s*([\\w\\d,\\s]+)";
        String regex2 = "REF:\\s*(\\w+),.*?Lot\\s*#\\s*([\\w\\d/]+)/(UDI:\\s*(\\d+))";


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
















//package org.example;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.*;
//
//public class DeviceInfoExtractor {
//    public static void Exatech(String input, Set<OutputObj> objInfoList,String recall){
//
//        // Regular expression patterns
//        String itemPattern = "(\\d{3}-\\d{2}-\\d{2})";
//        String udiPattern = "UDI/DI\\s([\\d]+)";
//        String serialPattern = "Serial Numbers:\\s([\\d,\\s]+)";
//
//        Pattern itemNumPattern = Pattern.compile(itemPattern);
//        Pattern udiNumPattern = Pattern.compile(udiPattern);
//        Pattern serialNumPattern = Pattern.compile(serialPattern);
//
//
//        Matcher itemNumMatcher = itemNumPattern.matcher(input);
//        Matcher udiNumMatcher = udiNumPattern.matcher(input);
//        Matcher serialNumMatcher = serialNumPattern.matcher(input);
//
//        String regex = "\\b(\\w{2}-\\w{3}-\\w{2}-\\d{4,5}), GTIN (\\d{14}), Serial Numbers: ([\\w, ]+);?";
//        String regex2 = "(\\d{3}-\\d{2}-\\d{2}), GTIN (\\d{14}), Serial Numbers: ([\\w, ]+);?";
//
//        Pattern re = Pattern.compile(regex);
//        Pattern re2 = Pattern.compile(regex2);
//
//        Matcher rema= re.matcher(input);
//        Matcher rema2= re2.matcher(input);
//
//        while (rema.find()){
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.catalog_number.add(rema.group(1));
//            deviceInfo.GTIN.add(rema.group(2));
//
//
//            String[] serials = rema.group(3).split(",\\s*");
//            deviceInfo.serial_number.addAll(Arrays.asList(serials));
//
//
//
//            objInfoList.add(deviceInfo);
//
//        }
//
//        while (rema2.find()){
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.catalog_number.add(rema2.group(1));
//            deviceInfo.GTIN.add(rema2.group(2));
//            String[] serials = rema2.group(3).split(",\\s*");
//            deviceInfo.serial_number.addAll(Arrays.asList(serials));
//            objInfoList.add(deviceInfo);
//
//        }
//
//        while ((itemNumMatcher.find()) && udiNumMatcher.find() && serialNumMatcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.item_number.add(itemNumMatcher.group(1));
//            deviceInfo.UDI.add(udiNumMatcher.group(1));
//
//
//            String[] serials = serialNumMatcher.group(1).split(",\\s*");
//            deviceInfo.serial_number.addAll(Arrays.asList(serials));
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//    }
//
//
//    public static  void Fresenius(String input, Set<OutputObj> objInfoList,String recall) {
//        String regex = "(?=.*Product Code:\\s*(\\w+))(?=.*UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?))(?=.*Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?))";
////        String regex = "Product Code:\\s*(\\w+)|UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?)|Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?)";
//        // Compile the regex
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(input);
//
//
//        // Find and extract values
//        int i=0;
//        while (matcher.find()) {
//            if(i>0) break;
//            OutputObj deviceInfo = new OutputObj();
//            if (matcher.group(1) != null) {
//                deviceInfo.product_number.add(matcher.group(1));
//            }
//            if (matcher.group(2) != null) {
//                deviceInfo.UDI.add(matcher.group(2));
//            }
//            if (matcher.group(3) != null) {
//                deviceInfo.serial_number.add(matcher.group(3));
//            }
//    i++;
//            objInfoList.add(deviceInfo);
//
//        }
//
//        regex = "(?=.*Model numbers\\s*([\\w\\d]+(?:\\s*and\\s*[\\w\\d]+)*))(?=.*UDI-DI\\s*(\\d+))(?=.*Serial Numbers\\s*([\\w\\d\\s]+)).*";
//        pattern = Pattern.compile(regex);
//        matcher = pattern.matcher(input);
//
//        // Use a Set to ensure uniqueness
//        Set<OutputObj> uniqueDevices = new HashSet<>();
//
//        // Find and extract values
//        while (matcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            if (matcher.group(1) != null) {
//                String[] models = matcher.group(1).split("\\s*and\\s*");
//                deviceInfo.model_number.addAll(Arrays.asList(models));
//            }
//            if (matcher.group(2) != null) {
//                deviceInfo.UDI.add(matcher.group(2));
//            }
//            if (matcher.group(3) != null) {
//                String[] serials = matcher.group(3).split("\\s+");
//                deviceInfo.serial_number.addAll(Arrays.asList(serials));
//            }
//
//            // Add to the set (duplicates are automatically ignored)
//            uniqueDevices.add(deviceInfo);
//        }
//        // Add unique devices to the output list
//        objInfoList.addAll(uniqueDevices);
//
//        regex = "Model No:\\s*(\\w+);\\s*UDI-DI:\\s*(\\d+);\\s*Serial No.\\s*([\\w\\d,\\s]+)";
//         pattern = Pattern.compile(regex);
//         matcher = pattern.matcher(input);
//
//        uniqueDevices.clear();
//
//        while (matcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            if (matcher.group(1) != null) {
//                deviceInfo.model_number.add(matcher.group(1));
//            }
//            if (matcher.group(2) != null) {
//                deviceInfo.UDI.add(matcher.group(2));
//            }
//            if (matcher.group(3) != null) {
//                String[] serials = matcher.group(3).split(",\\s*");
//                deviceInfo.serial_number.addAll(Arrays.asList(serials));
//            }
//            uniqueDevices.add(deviceInfo);
//        }
//
//        objInfoList.addAll(uniqueDevices);
//
//
//
//        regex = "Model Number:\\s*([\\w\\-]+);\\s*UDI/DI \\(Bag\\):\\s*(\\d+);\\s*UDI/DI \\(Case\\):\\s*(\\d+);\\s*All lots\\.";
//         pattern = Pattern.compile(regex);
//         matcher = pattern.matcher(input);
//
//         uniqueDevices.clear();
//
//        while (matcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            if (matcher.group(1) != null) {
//                deviceInfo.model_number.add(matcher.group(1));
//            }
//            if (matcher.group(2) != null) {
//                deviceInfo.UDI.add(matcher.group(2));
//            }
//            if (matcher.group(3) != null) {
//                deviceInfo.UDI.add(matcher.group(3));
//            }
//            deviceInfo.serial_number.add("All lots");
//            uniqueDevices.add(deviceInfo);
//        }
//
//        objInfoList.addAll(uniqueDevices);
//
//
//    }
//
//
//
//    public static void Medline(String input, Set<OutputObj> objInfoList,String recall){
//
//
//
//        // Regular expression to capture REF number, UDI/DI, and Lot Numbers
//        String refRegex = "REF\\s*(\\w+)";
//        String udiregex = "UDI/DI:?\\s*(\\w+)";
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
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(refMatcher.group(1));
//            deviceInfo.UDI.add(udiMatcher.group(1));
//            deviceInfo.lot_number.add(lotMatcher.group(3));
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//    //----------------------------------------------------------------------------------------
//         refRegex = "REF\\s*(\\w+)";
//        udiregex = "GTIN:?\\s*(\\w+)";
//        lotRegex = "Lot\\s*Numbers:?\\s*([\\w,\\s]+)";
//
//        // Compile the regex
//         refPattern = Pattern.compile(refRegex);
//         refMatcher = refPattern.matcher(input);
//
//         udiPattern = Pattern.compile(udiregex);
//         udiMatcher = udiPattern.matcher(input);
//
//
//         lot = Pattern.compile(lotRegex);
//         lotMatcher = lot.matcher(input);
//
//        while ((refMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(refMatcher.group(1));
//            deviceInfo.GTIN.add(udiMatcher.group(1));
//            deviceInfo.lot_number.add(lotMatcher.group(1));
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//    //----------------------------------------------------------------------------------------
//
//
//        String reorderRegex = "Reorder\\s*Number \\w+";
//        udiregex = "UDI/DI\\s\\d{14}[\\s()\\w]*,?\\s*\\d{14}[\\s()\\w]*";
//        lotRegex = "Lot Numbers:\\s*([\\w+,\\s]+)";
//
//        Pattern reorderPattern = Pattern.compile(reorderRegex);
//        Matcher reoderMatcher = reorderPattern.matcher(input);
//
//        udiPattern = Pattern.compile(udiregex);
//        udiMatcher = udiPattern.matcher(input);
//
//
//        lot = Pattern.compile(lotRegex);
//        lotMatcher = lot.matcher(input);
//
//        while (reoderMatcher.find() && udiMatcher.find() && lotMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.reorder_number.add(reoderMatcher.group().substring(14));
//            deviceInfo.UDI.add(udiMatcher.group().substring(7));
//            deviceInfo.lot_number.add(lotMatcher.group().substring(13));
//            objInfoList.add(deviceInfo);
//        }
//
//
//
//
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
//         lotMatcher = lotPattern.matcher(input);
//
//        while ((prodMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(prodMatcher.group().substring(12));
//            deviceInfo.UDI.add(udiMatcher.group().substring(6));
//            deviceInfo.lot_number.add(lotMatcher.group().substring(12));
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//
//    //--------------------------------------
//
//        String reg1 = "Item\\s*Number:\\s*(\\w+)";
//        String reg3 = "UDI/GTIN\\s*[\\w()]*:?\\s*(\\w+),\\s*UDI/GTIN\\s*[\\w()]*:?\\s*(\\w+)";
//        String reg2 = "Lot\\s*Numbers?:?\\s*(\\w+)";
//
//        Pattern regPattern1 = Pattern.compile(reg1);
//        Pattern regPattern2 = Pattern.compile(reg2);
//        Pattern regPattern3 = Pattern.compile(reg3);
//
//        Matcher regMatcher1 = regPattern1.matcher(input);
//        Matcher regMatcher2 = regPattern2.matcher(input);
//        Matcher regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.item_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher3.group(1));
//            deviceInfo.UDI.add(regMatcher3.group(2));
//            deviceInfo.lot_number.add(regMatcher2.group(1));
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------
//
//        reg1 = "Item\\s*Number:\\s*(\\w+)";
//        reg3 = "[\\w()]*\\s*UDI/GTIN\\s*:?\\s*(\\w+),\\s*[\\w()]*\\s*UDI/GTIN\\s*:?\\s*(\\w+)";
//        reg2 = "Lot\\s*Numbers?:?\\s*(\\w+)";
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.item_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher3.group(1));
//            deviceInfo.UDI.add(regMatcher3.group(2));
//            deviceInfo.lot_number.add(regMatcher2.group(1));
//
//            objInfoList.add(deviceInfo);
//        }//--------------------------------------
//
//        reg1 = "Model\\s*Numbers?:?\\s*(\\w+)";
//        reg2 = "UDI/DI\\s*:?\\(\\w*\\)\\s*(\\w+),\\s*UDI/DI\\s*:?\\(\\w*\\)\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.model_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(2));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------
//
//        reg1 = "REF\\s*(\\w+)";
//        reg2 = "GTIN\\s*\\(01\\)\\s*(\\w+)";
//        reg3="Lot\\s*Number\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(regMatcher1.group(1));
//            deviceInfo.GTIN.add(regMatcher2.group(1));
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------
//
//        reg1 = "REF\\s*(\\w+)";
//        reg2 = "GTIN\\s*(\\w+)";
//        reg3="Batch\\s*Numbers?:?\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(regMatcher1.group(1));
//            deviceInfo.GTIN.add(regMatcher2.group(1));
//            deviceInfo.batch_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------
//
//        reg1 = "Reorder\\s*#:?\\s*(\\w+)";
//        reg2 = "UDI/DI:?\\s*\\(01\\)\\s*(\\w+)\\s*\\(\\w+\\),\\s*\\(01\\)\\s*(\\w+)\\s*\\(\\w+\\)";
//        reg3="Lot\\s*Numbers?\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.reorder_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(1));
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//        //--------------------------------------
//
//        reg1 = "Reorder\\s*:?\\s*(\\w+)";
//        reg2 = "UDI/DI:?\\s*(\\w+)";
//        reg3="Lot\\s*C?c?ode?\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.reorder_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(1));
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }        //--------------------------------------
//
//        reg1 = "Reorder\\s*Number:?\\s*(\\w+)";
//        reg2 = "GTIN:?\\s*(\\w+)";
//        reg3="Lot\\s*#?\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1,Pattern.CASE_INSENSITIVE);
//         regPattern2 = Pattern.compile(reg2,Pattern.CASE_INSENSITIVE);
//         regPattern3 = Pattern.compile(reg3,Pattern.CASE_INSENSITIVE);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.reorder_number.add(regMatcher1.group(1));
//            deviceInfo.GTIN.add(regMatcher2.group(1));
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //------------------------------------------
//
//
//        reg1 = "Model\\s*Number\\s*:?\\s*(\\w+)";
//        reg2 = "UDI/DI:?\\s*(\\(\\w+\\))?\\s*\\(01\\)\\s*(\\w+)\\s*,\\s*UDI/DI:?\\s*(\\(\\w+\\))?\\s*\\(01\\)\\s*(\\w+)";
//        reg3="Lot\\s*Numbers?:?\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.model_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(4));
//            deviceInfo.UDI.add(regMatcher2.group(2));
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------
//
//        reg1 = "PN:?\\s*(\\w+)";
//        reg2 = "Item\\s*(\\w+)";
//        String reg4 = "UDI/DI\\s*\\(\\w+\\)\\s*(\\w+)";
//        reg3="Lot\\s*numbers?\\s*(\\w+)";
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//        Pattern regPattern4 = Pattern.compile(reg4);
//
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//        Matcher regMatcher4 = regPattern4.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()&& regMatcher3.find()&& regMatcher4.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            deviceInfo.item_number.add(regMatcher2.group(1));
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//            deviceInfo.UDI.add(regMatcher4.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//
//        //-------------------------------------------------------------------------------------
//        reg1="Model\\s*Number\\s*:\\s*(\\w+),?;?\\s*UPC\\s*Number:\\s*(\\w+)";
//        regPattern1 = Pattern.compile(reg1);
//        regMatcher1=regPattern1.matcher(input);
//
//
//
//
//
//        while (regMatcher1.find() ) {
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.model_number.add(regMatcher1.group(1));
//            deviceInfo.UPC.add(regMatcher1.group(2));
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//
//    }
//
//    public static void Boston(String input, Set<OutputObj> objInfoList,String recall){
//
//        String modelRegex = "MODEL\\s*-\\s*[\\w\\s]*\\(\\w*\\)";
//        String refRegex = "REF\\s*\\w*";
//        String udiRegex = "UDI/DI\\s*\\w{14}";
//        String batchRegex = "Batch\\s*Numbers?:[\\s\\w,]+";
//
//        Pattern modelPattern = Pattern.compile(modelRegex);
//        Pattern refPattern = Pattern.compile(refRegex);
//        Pattern udiPattern = Pattern.compile(udiRegex);
//        Pattern batchPattern = Pattern.compile(batchRegex);
//
//
//        Matcher modelMatcher = modelPattern.matcher(input);
//        Matcher refMatcher = refPattern.matcher(input);
//        Matcher udiMatcher = udiPattern.matcher(input);
//        Matcher batchMatcher = batchPattern.matcher(input);
//
//        while ((refMatcher.find()) && udiMatcher.find() && modelMatcher.find() && batchMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(refMatcher.group().substring(4));
//            deviceInfo.UDI.add(udiMatcher.group().substring(6));
//            deviceInfo.model_number.add(modelMatcher.group().substring(8));
//            deviceInfo.batch_number.add(batchMatcher.group().substring(15));
//
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//    //--------------------------------------------------------------------------------------------------------
//
//        String gtinRegex1 = "GTIN numbers in the U.S.:[\\w\\s,]*";
//        String gtinRegex2 = "GTIN numbers OUS:[\\w\\s,]*";
//
//        Pattern gtinPattern1 = Pattern.compile(gtinRegex1);
//        Pattern gtinPattern2 = Pattern.compile(gtinRegex2);
//
//        Matcher gtinMatcher1 = gtinPattern1.matcher(input);
//        Matcher gtinMatcher2 = gtinPattern2.matcher(input);
//
//        while ((gtinMatcher1.find()) && gtinMatcher2.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.GTIN.add(gtinMatcher1.group().substring(25));
//            deviceInfo.GTIN.add(gtinMatcher2.group().substring(17));
//
//            deviceInfo.serial_number.add("All serial numbers");
//
//
//
//
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------------------------------------------------------------------------
//
//
//
//
//        String catalogRegex = "Catalog\\s*number\\s*\\w*";
//        gtinRegex1 = "GTIN\\s*\\w*";
//        batchRegex="Lot[\\w/\\s]*:\\s*[\\w\\s,]*";
//
//        Pattern catalogPattern = Pattern.compile(catalogRegex);
//         gtinPattern1 = Pattern.compile(gtinRegex1);
//         batchPattern = Pattern.compile(batchRegex);
//
//        // Create matchers for the input string
//        Matcher catalogMatcher = catalogPattern.matcher(input);
//        gtinMatcher1 = gtinPattern1.matcher(input);
//        batchMatcher = batchPattern.matcher(input);
//
//
//
//        while ((gtinMatcher1.find()) && catalogMatcher.find() && batchMatcher.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.GTIN.add(gtinMatcher1.group().substring(5));
//            deviceInfo.catalog_number.add(catalogMatcher.group().substring(15));
//            deviceInfo.batch_number.add(batchMatcher.group().substring(19));
//
//            objInfoList.add(deviceInfo);
//        }
//
//        //--------------------------------------------------------------------------------------------------------
//
//
//        String regexGTIN = "GTIN\\):\\s*(\\d+)";
//        String regexUPN = "(Outer\\s*box\\s*UPN?#\\s*([A-Za-z0-9]+),\\s*Inner\\s*box\\s*UPN\\s*#\\s*([A-Za-z0-9]+))";
//        String regexLotBatch = "Lot\\s*/\\s*Batch\\s*#?\\s([\\d,\\s]+)";
//
//        // Compile the regex patterns
//        Pattern patternGTIN = Pattern.compile(regexGTIN);
//        Pattern patternUPN = Pattern.compile(regexUPN);
//        Pattern patternLotBatch = Pattern.compile(regexLotBatch);
//
//        // Matchers for each regex pattern
//        Matcher matcherGTIN = patternGTIN.matcher(input);
//        Matcher matcherUPN = patternUPN.matcher(input);
//        Matcher matcherLotBatch = patternLotBatch.matcher(input);
//
//        // Store results in lists
//
//        List<String> gtins = new ArrayList<>();
//        List<String[]> upns = new ArrayList<>();
//        List<String[]> lotBatches = new ArrayList<>();
//
//        // Extract GTINs
//
//        while(matcherGTIN.find() && matcherUPN.find() && matcherLotBatch.find()){
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.GTIN.add(matcherGTIN.group(1));
//            deviceInfo.UPN.add(matcherUPN.group(2));
//            deviceInfo.UPN.add( matcherUPN.group(3));
//            deviceInfo.lot_number.add(String.join(", ",matcherLotBatch.group(1).trim().split(",\\s*")));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //--------------------------------------------------------------------------------------------------------
//
//        regexUPN = "UPN:\\s*\\w+";
//        gtinRegex1 ="GTIN:\\s*\\w+";
//        regexLotBatch ="Lot\\s*Numbers?:[\\s,\\d]+";
//
//        patternUPN = Pattern.compile(regexUPN);
//        gtinPattern1 = Pattern.compile(gtinRegex1);
//        patternLotBatch = Pattern.compile(regexLotBatch);
//
//
//        matcherUPN = patternUPN.matcher(input);
//        gtinMatcher1 = gtinPattern1.matcher(input);
//        matcherLotBatch = patternLotBatch.matcher(input);
//
//
//        while(gtinMatcher1.find() && matcherUPN.find() && matcherLotBatch.find()){
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.GTIN.add(gtinMatcher1.group().substring(6));
//            deviceInfo.UPN.add(matcherUPN.group().substring(5));
//
//            deviceInfo.lot_number.add(String.join(", ",matcherLotBatch.group().trim().split(",\\s*")).substring(13));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//
////--------------------------------------------------------------------------------------------------------
//
//        String regex = "UPN\\s([A-Za-z0-9]+),\\sGTIN\\s\\(UDI-DI\\)\\s(\\d+),\\sLots?\\s([\\d,\\s]+)";
//
//        // Compile the regex pattern
//        Pattern pattern = Pattern.compile(regex);
//
//        // Matcher for the regex pattern
//        Matcher matcher = pattern.matcher(input);
//
//
//        // Extract matches
//        while (matcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.UPN.add(matcher.group(1));
//            deviceInfo.GTIN.add(matcher.group(2));
//            deviceInfo.lot_number.add(String.join(",",matcher.group(3).trim().split(",\\s*")));
//            objInfoList.add(deviceInfo);
//        }
////---------------------------------------------------------------------------------------------------------
//
//         regex = "([a-z])\\)\\s([A-Za-z0-9]+),\\sUDI/DI\\s(\\d+),\\sALL\\sLOT\\sCODES";
//
//        // Compile the regex pattern
//         pattern = Pattern.compile(regex);
//
//        // Matcher for the regex pattern
//         matcher = pattern.matcher(input);
//
//        while (matcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.UPN.add(matcher.group(2));
//            deviceInfo.UDI.add(matcher.group(3));
//            deviceInfo.lot_number.add("ALL LOT CODES");
//
//            objInfoList.add(deviceInfo);
//        }
//
//        //--------------------------------------------------------------------------------------------------
//
//
//        String regex4 = "REF\\s*\\w*", regex2="UDI/DI\\s*\\d{14}",regex3="Lot\\s*Numbers?:[\\s,\\d]*";
//
//        // Compile the regex pattern
//        Pattern pattern4 = Pattern.compile(regex4);
//
//        Pattern pattern2 = Pattern.compile(regex2);
//
//        Pattern pattern3 = Pattern.compile(regex3);
//
//        // Matcher for the regex pattern
//        Matcher matcher4 = pattern4.matcher(input);
//
//        Matcher matcher2 = pattern2.matcher(input);
//
//        Matcher matcher3 = pattern3.matcher(input);
//
//
//        // Extract matches
//        while (matcher4.find() && matcher2.find() && matcher3.find()) {
//
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.ref_number.add(matcher4.group().substring(4));
//            deviceInfo.UDI.add(matcher2.group().substring(8));
//            deviceInfo.lot_number.add(String.join(",",matcher3.group().trim().split(",\\s*")).substring(13));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//
////        regexUPN = "UPN\\s*\\w+";
////        gtinRegex1 ="GTIN\\s*\\(UDI-DI\\)\\s*\\w+";
////        regexLotBatch ="Lots?\\s*[\\s,\\d]+";
////
////        patternUPN = Pattern.compile(regexUPN);
////        gtinPattern1 = Pattern.compile(gtinRegex1);
////        patternLotBatch = Pattern.compile(regexLotBatch);
////
////
////        matcherUPN = patternUPN.matcher(input);
////        gtinMatcher1 = gtinPattern1.matcher(input);
////        matcherLotBatch = patternLotBatch.matcher(input);
////
////
////        while(matcherUPN.find() && gtinMatcher1.find() &&  matcherLotBatch.find()){
////            OutputObj deviceInfo = new OutputObj();
////
////            deviceInfo.GTIN.add(gtinMatcher1.group().substring(4));
////            deviceInfo.UPN.add(matcherUPN.group().substring(13));
////
////            deviceInfo.lot_number.add(String.join(", ",matcherLotBatch.group().trim().split(",\\s*")).substring(4));
////
////
////            objInfoList.add(deviceInfo);
////        }
//
//
////
////        while (matcherGTIN.find()) {
////            gtins.add(matcherGTIN.group(1));
////        }
////
////        // Extract UPNs
////        while (matcherUPN.find()) {
////            upns.add(new String[]{matcherUPN.group(2), matcherUPN.group(3)});
////        }
////
////        // Extract Lot/Batch numbers
////        while (matcherLotBatch.find()) {
////            String[] lots = matcherLotBatch.group(1).trim().split(",\\s*");
////            lotBatches.add(lots);
////        }
////
////        // Print results, ensuring we respect the correlation
////        for (int i = 0; i < gtins.size(); i++) {
////            OutputObj deviceInfo = new OutputObj();
////
////            if (i < upns.size() && i < lotBatches.size()) {
////                String[] upnPair = upns.get(i);
////                String[] lots = lotBatches.get(i);
////                String gtin = gtins.get(i);
////
////                deviceInfo.GTIN.add(gtin);
////               deviceInfo.UPN.add(upnPair[0]);
////                deviceInfo.UPN.add(upnPair[1]);
////                deviceInfo.lot_number.add(String.join(", ", lots));
////
////            }
//
//
//        //}
//
//
//
//
//
//    }
//
//
//
//    public static void Cardinal (String input, Set<OutputObj> objInfoList,String recall){
//
//    String reg1 ="Model\\s*\\d*";
//    String reg2="Lot\\s*#\\w+";
//    String reg3 = "UDI-?/?DI:\\s*[\\w\\s(),]+";
//
//    Pattern regPattern1 = Pattern.compile(reg1);
//    Pattern regPattern2 = Pattern.compile(reg2);
//    Pattern regPattern3 = Pattern.compile(reg3);
//
//    Matcher regMatcher1 = regPattern1.matcher(input);
//    Matcher regMatcher2 = regPattern2.matcher(input);
//    Matcher regMatcher3 = regPattern3.matcher(input);
//
//    while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
//        OutputObj deviceInfo = new OutputObj();
//        deviceInfo.model_number.add(regMatcher1.group().substring(6));
//        deviceInfo.lot_number.add(regMatcher2.group().substring(5));
//        deviceInfo.UDI.add(regMatcher3.group().substring(8));
//
//        objInfoList.add(deviceInfo);
//
//
//
//
//    }
//
//
//    //-----------------------------------------------------------------------
//
//       reg1= "REF\\s*[\\w-]+";
//        reg2="UDI/DI\\s*\\d{14}\\s*\\(Case\\),(\\s*\\d{14}\\s*\\(Box\\),)?\\s*\\s*\\d{14}\\s*\\(Each\\)";
//        reg3 = "Lot\\s*Numbers?:[\\w\\s,]+";
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(regMatcher1.group().substring(4));
//            deviceInfo.UDI.add(regMatcher2.group().substring(7));
//            deviceInfo.lot_number.add(regMatcher3.group().substring(13));
//
//            objInfoList.add(deviceInfo);
//
//
//
//
//        }
//
//        //--------------------------------------------------
//
//        String regex = "Cat\\.\\s*(\\w+)\\s*-\\s*Lot\\s*#(\\w+),\\s*E?e?xp\\.\\s[\\d/]+,\\s*UDI-DI\\s*(\\d+)";
//
//        // Compile the regex pattern
//        Pattern pattern = Pattern.compile(regex);
//
//        // Matcher for the regex pattern
//        Matcher matcher = pattern.matcher(input);
//
//        while (matcher.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.catalog_number.add(matcher.group(1));
//            deviceInfo.lot_number.add(matcher.group(2));
//            deviceInfo.UDI.add(matcher.group(3));
//
//            objInfoList.add(deviceInfo);
//        }
//
//
////--------------------------------------------------
//            reg1= "Product\\s*Code:\\s*\\w+";
//        reg2="UDI/?-?DI:?\\s*\\d{14}\\s*-\\s*each,\\s*\\d{14}\\s*-\\s*box,\\s*\\d{14}\\s*-\\s*case";
//        reg3="Lot\\s*Numbers?:?\\s*[\\w,\\s]+";
//
//        regPattern1 = Pattern.compile(reg1);
//        regPattern2 = Pattern.compile(reg2);
//        regPattern3 = Pattern.compile(reg3);
//
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//        regMatcher3 = regPattern3.matcher(input);
//
//        while(  regMatcher1.find()  && regMatcher2.find() && regMatcher3.find() ){
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group().substring(14));
//            deviceInfo.UDI.add(regMatcher2.group().substring(7));
//            deviceInfo.lot_number.add(regMatcher3.group().substring(13));
//
//            objInfoList.add(deviceInfo);
//
//
//
//        }
//
////--------------------------------------------------
//
//        reg1="UDI/DI\\s*\\d{14}\\s\\(cs\\),\\s*\\d{14}\\s*\\(ea\\)";
//
//        reg2="Lot\\s*Numbers?:?(\\s*[\\w,\\s]+)\\s*(REF\\s*\\d+)?";
//
//        regPattern1 = Pattern.compile(reg1);
//        regPattern2 = Pattern.compile(reg2);
//
//
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//
//        while( regMatcher2.find() && regMatcher1.find()) {
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.UDI.add(regMatcher1.group().substring(7));
//            deviceInfo.lot_number.add(regMatcher2.group(1));
//            if(regMatcher2.group(2)!=null){
//                deviceInfo.ref_number.add(regMatcher2.group(2));
//            }
//
//            objInfoList.add(deviceInfo);
//
//        }
//
//
//
//
//    }
//
//
//    public static void Baxter(String input, Set<OutputObj> objInfoList,String recall) {
//        String reg1 = "Product\\s*Code\\s*(\\d+)";
//        String reg3 = "Serial\\s*Numbers?\\s*:?\\s*([\\d\\s,]+)";
//        String reg2 = "UDI-?/?DI\\s*:?\\s*(\\d+)";
//
//        Pattern regPattern1 = Pattern.compile(reg1);
//        Pattern regPattern2 = Pattern.compile(reg2);
//        Pattern regPattern3 = Pattern.compile(reg3);
//
//        Matcher regMatcher1 = regPattern1.matcher(input);
//        Matcher regMatcher2 = regPattern2.matcher(input);
//        Matcher regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(1));
//            deviceInfo.serial_number.add(regMatcher3.group(1));
//
//            objInfoList.add(deviceInfo);
//        }
//
//
//
//        //-------------------------------------------------------------------------------------
////
//        reg1 = "REF\\s*:?\\s*([\\w-]+)";
//        reg3 ="Serial\\s*Numbers?\\s*:?\\s*([\\w\\s,]+)";
//
//        regPattern1 = Pattern.compile(reg1);
//        regPattern2 = Pattern.compile(reg2);
//        regPattern3 = Pattern.compile(reg3);
//
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//        regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.ref_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(1));
//            deviceInfo.serial_number.add(regMatcher3.group(1));
//
//            objInfoList.add(deviceInfo);
//        }
//
//        //-------------------------------------------------------------------------------------
//    reg1="([-\\w]+),\\s*UDI/DI\\s*(\\d+)";
//
//        regPattern1 = Pattern.compile(reg1);
//
//
//        regMatcher1 = regPattern1.matcher(input);
//
//
//        while (regMatcher1.find() ) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.item_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher1.group(2));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//        //-------------------------------------------------------------------------------------
//        reg1="P?p?roduct\\s*C?c?ode:?\\s*(\\w+),\\s*UDI/DI:?\\s*(\\d+)";
//        regPattern1 = Pattern.compile(reg1);
//
//
//        regMatcher1 = regPattern1.matcher(input);
//
//
//        while (regMatcher1.find() ) {
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.UDI.add(regMatcher1.group(2));
//            deviceInfo.product_number.add(regMatcher1.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//        //-------------------------------------------------------------------------------------
//        reg1="([\\w-]+):\\s*UDI/DI\\s*([\\w-]+)";
//        regPattern1 = Pattern.compile(reg1);
//
//
//        regMatcher1 = regPattern1.matcher(input);
//
//
//        while (regMatcher1.find() ) {
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.UDI.add(regMatcher1.group(2));
//            deviceInfo.product_number.add(regMatcher1.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//        //-------------------------------------------------------------------------------------
//        reg1="UDI/DI\\s*(\\w+),\\s*Lot\\s*Numbers?:?\\s*(\\w+)";
//        regPattern1 = Pattern.compile(reg1);
//        regPattern1 = Pattern.compile(reg1);
//
//
//        reg2="REF\\s*(\\w+)";
//        regPattern2 = Pattern.compile(reg2);
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find() ) {
//            OutputObj deviceInfo = new OutputObj();
//
//            deviceInfo.UDI.add(regMatcher1.group(1));
//            deviceInfo.ref_number.add(regMatcher2.group(1));
//            deviceInfo.lot_number.add(regMatcher1.group(2));
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//
//
//
//
//
//    }
//
//
//    public static void NonLLCPhilips(String input, Set<OutputObj> objInfoList,String recall){
//
//        String reg1 ="Model\\s*No[\\s.]+(\\d+)\\s*UDI-DI\\s*([\\w/]+)";
//
//
//        Pattern regPattern1 = Pattern.compile(reg1);
//
//
//        Matcher regMatcher1 = regPattern1.matcher(input);
//
//
//
//
//        while(regMatcher1.find()){
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.model_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher1.group(2));
//
//            objInfoList.add(deviceInfo);
//
//
//        }
//
//
//        //----------------------------------------------------------------------------
//
//        reg1="Product\\s*Number\\s*(\\w+)";
//       String reg2="UDI-DI\\s*([-\\w]+)";
//       String reg3="Serial\\s*Numbers?\\s*([\\w\\s,]+)";
//
//
//
//         regPattern1 = Pattern.compile(reg1);
//        Pattern regPattern2 = Pattern.compile(reg2);
//        Pattern regPattern3 = Pattern.compile(reg3);
//
//         regMatcher1 = regPattern1.matcher(input);
//        Matcher regMatcher2 = regPattern2.matcher(input);
//        Matcher regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            deviceInfo.UDI.add(regMatcher2.group(1));
//            deviceInfo.serial_number.add(regMatcher3.group(1));
//
//            objInfoList.add(deviceInfo);
//        }        //----------------------------------------------------------------------------
//
//        reg1="Product\\s*Code\\s*(\\w+)";
//        reg2="UDI\\s*([-\\w]+)?";
//        reg3="Serial\\s*numbers\\s*([\\w\\s,]+)";
//
//
//
//         regPattern1 = Pattern.compile(reg1);
//         regPattern2 = Pattern.compile(reg2);
//         regPattern3 = Pattern.compile(reg3);
//
//         regMatcher1 = regPattern1.matcher(input);
//         regMatcher2 = regPattern2.matcher(input);
//         regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            if(regMatcher2.group(1)!=null){
//                deviceInfo.UDI.add(regMatcher2.group(1));
//            }
//
//            deviceInfo.serial_number.add(regMatcher3.group(1));
//
//            objInfoList.add(deviceInfo);
//        }
//
//
//        //----------------------------------
//
//        reg1="Product\\s*Number\\s*(\\w+)";
//        reg2="UDI-DI\\s*(\\w+)";
//        reg3="Serial\\s*n?N?umber\\s*(\\w+)\\s*Accessory\\s*Serial\\s*N?n?umber\\s*(\\w+)";
//
//        regPattern1 = Pattern.compile(reg1);
//        regPattern2 = Pattern.compile(reg2);
//        regPattern3 = Pattern.compile(reg3);
//
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//        regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            if(regMatcher2.group(1)!=null){
//                deviceInfo.UDI.add(regMatcher2.group(1));
//            }
//
//            deviceInfo.serial_number.add(regMatcher3.group(1));
//            deviceInfo.serial_number.add(regMatcher3.group(2));
//
//            objInfoList.add(deviceInfo);
//        }
//        //----------------------------------
//
//        reg1="Model\\s*Number\\s*(\\w+)";
//        reg2="UDI-DI:?\\s*(\\w+)";
//        reg3="Lot\\s*Code:?\\s*(\\w+)";
//
//        regPattern1 = Pattern.compile(reg1);
//        regPattern2 = Pattern.compile(reg2);
//        regPattern3 = Pattern.compile(reg3);
//
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//        regMatcher3 = regPattern3.matcher(input);
//
//        while (regMatcher1.find() && regMatcher2.find() && regMatcher3.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            if(regMatcher2.group(1)!=null){
//                deviceInfo.UDI.add(regMatcher2.group(1));
//            }
//
//            deviceInfo.lot_number.add(regMatcher3.group(1));
//
//
//            objInfoList.add(deviceInfo);
//        }      //----------------------------------
//
//        reg1="Model\\s*No\\.\\s*(\\w+)";
//        reg2="UDI-DI:?\\s*(\\(\\d\\))?\\s*(\\w+)";
//
//
//        regPattern1 = Pattern.compile(reg1);
//        regPattern2 = Pattern.compile(reg2);
//
//
//        regMatcher1 = regPattern1.matcher(input);
//        regMatcher2 = regPattern2.matcher(input);
//
//
//        while (regMatcher1.find() && regMatcher2.find()) {
//            OutputObj deviceInfo = new OutputObj();
//            deviceInfo.product_number.add(regMatcher1.group(1));
//            if(regMatcher2.group(2)!=null){
//                deviceInfo.UDI.add(regMatcher2.group(2));
//            }
//
//
//            objInfoList.add(deviceInfo);
//        }
//
//    }
//
//
//
//
//
//    public static Set<OutputObj> extractDeviceInfo(String input,String firmname, String recall) {
//        Set<OutputObj> objInfoList = new HashSet<>();
//        if(Objects.equals(firmname, "Exactech, Inc.")) {
//            DeviceInfoExtractor.Exatech(input, objInfoList,recall);
//        }
//        else if(Objects.equals(firmname, "Fresenius Medical Care Holdings, Inc.")){
//            DeviceInfoExtractor.Fresenius(input,objInfoList,recall);
//
//        }
//        else if (Objects.equals(firmname, "MEDLINE INDUSTRIES, LP - Northfield")){
//            DeviceInfoExtractor.Medline(input,objInfoList,recall);
//        }
//        else if(Objects.equals(firmname, "Boston Scientific Corporation")){
//            DeviceInfoExtractor.Boston(input,objInfoList,recall);
//        }
//        else if(Objects.equals(firmname, "Cardinal Health 200, LLC")){
//            DeviceInfoExtractor.Cardinal(input,objInfoList,recall);
//        }
//        else if (Objects.equals(firmname, "Baxter Healthcare Corporation")){
//            DeviceInfoExtractor.Baxter(input,objInfoList,recall);
//        }
//        else if(Objects.equals(firmname, "Philips North America")){
//            DeviceInfoExtractor.NonLLCPhilips(input,objInfoList,recall);
//
//        }
//        return objInfoList;
//
//
//
//
//    }
//
//}
//
