package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class DeviceInfoExtractor {
    public static void Exatech(String input, Set<OutputObj> objInfoList){

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
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.catalog_number.add(rema.group(1));
            deviceInfo.GTIN.add(rema.group(2));


            String[] serials = rema.group(3).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));



            objInfoList.add(deviceInfo);

        }

        while (rema2.find()){
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.catalog_number.add(rema2.group(1));
            deviceInfo.GTIN.add(rema2.group(2));
            String[] serials = rema2.group(3).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));
            objInfoList.add(deviceInfo);

        }

        while ((itemNumMatcher.find()) && udiNumMatcher.find() && serialNumMatcher.find()) {
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.item_number.add(itemNumMatcher.group(1));
            deviceInfo.UDI.add(udiNumMatcher.group(1));


            String[] serials = serialNumMatcher.group(1).split(",\\s*");
            deviceInfo.serial_number.addAll(Arrays.asList(serials));



            objInfoList.add(deviceInfo);
        }

    }


    public static  void Fresenius(String input, Set<OutputObj> objInfoList) {
        String regex = "(?=.*Product Code:\\s*(\\w+))(?=.*UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?))(?=.*Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?))";
//        String regex = "Product Code:\\s*(\\w+)|UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?)|Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?)";
        // Compile the regex
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);


        // Find and extract values
        int i=0;
        while (matcher.find()) {
            if(i>0) break;
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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



    public static void Medline(String input, Set<OutputObj> objInfoList){



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

            OutputObj deviceInfo = new OutputObj();
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

            OutputObj deviceInfo = new OutputObj();
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

            OutputObj deviceInfo = new OutputObj();
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

            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.item_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher3.group(1));
            deviceInfo.UDI.add(regMatcher3.group(2));
            deviceInfo.lot_number.add(regMatcher2.group(1));

            objInfoList.add(deviceInfo);
        }



    }

    public static void Boston(String input, Set<OutputObj> objInfoList){

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

            OutputObj deviceInfo = new OutputObj();
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

            OutputObj deviceInfo = new OutputObj();
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

            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();

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
            OutputObj deviceInfo = new OutputObj();

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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();

            deviceInfo.UPN.add(matcher.group(2));
            deviceInfo.UDI.add(matcher.group(3));
            deviceInfo.lot_number.add("ALL LOT CODES");

            objInfoList.add(deviceInfo);
        }

        //--------------------------------------------------------------------------------------------------


        String regex4 = "REF\\s*\\w*", regex2="UDI/DI\\s*\\d{14}",regex3="Lot\\s*Numbers?:[\\s,\\d]*";

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

            OutputObj deviceInfo = new OutputObj();

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
//            OutputObj deviceInfo = new OutputObj();
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
//            OutputObj deviceInfo = new OutputObj();
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



    public static void Cardinal (String input, Set<OutputObj> objInfoList){

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
        OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();

            deviceInfo.UDI.add(regMatcher1.group().substring(7));
            deviceInfo.lot_number.add(regMatcher2.group(1));
            if(regMatcher2.group(2)!=null){
                deviceInfo.ref_number.add(regMatcher2.group(2));
            }

            objInfoList.add(deviceInfo);

        }




    }


    public static void Baxter(String input, Set<OutputObj> objInfoList) {
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.item_number.add(regMatcher1.group(1));
            deviceInfo.UDI.add(regMatcher1.group(2));


            objInfoList.add(deviceInfo);
        }

        //-------------------------------------------------------------------------------------
        reg1="P?p?roduct\\s*C?c?ode:?\\s*(\\w+),\\s*UDI/DI:?\\s*(\\d+)";
        regPattern1 = Pattern.compile(reg1);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj();

            deviceInfo.UDI.add(regMatcher1.group(2));
            deviceInfo.product_number.add(regMatcher1.group(1));


            objInfoList.add(deviceInfo);
        }
        //-------------------------------------------------------------------------------------
        reg1="([\\w-]+):\\s*UDI/DI\\s*([\\w-]+)";
        regPattern1 = Pattern.compile(reg1);


        regMatcher1 = regPattern1.matcher(input);


        while (regMatcher1.find() ) {
            OutputObj deviceInfo = new OutputObj();

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
            OutputObj deviceInfo = new OutputObj();

            deviceInfo.UDI.add(regMatcher1.group(1));
            deviceInfo.ref_number.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher1.group(2));


            objInfoList.add(deviceInfo);
        }



    }


    public static void NonLLCPhilips(String input, Set<OutputObj> objInfoList){

        String reg1 ="Model\\s*No[\\s.]+(\\d+)\\s*UDI-DI\\s*([\\w/]+)";


        Pattern regPattern1 = Pattern.compile(reg1);


        Matcher regMatcher1 = regPattern1.matcher(input);




        while(regMatcher1.find()){
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
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
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.product_number.add(regMatcher1.group(1));
            if(regMatcher2.group(2)!=null){
                deviceInfo.UDI.add(regMatcher2.group(2));
            }


            objInfoList.add(deviceInfo);
        }

    }





    public static Set<OutputObj> extractDeviceInfo(String input,String firmname) {
        Set<OutputObj> objInfoList = new HashSet<>();
        if(Objects.equals(firmname, "Exactech, Inc.")) {
            DeviceInfoExtractor.Exatech(input, objInfoList);
        }
        else if(Objects.equals(firmname, "Fresenius Medical Care Holdings, Inc.")){
            DeviceInfoExtractor.Fresenius(input,objInfoList);

        }
        else if (Objects.equals(firmname, "MEDLINE INDUSTRIES, LP - Northfield")){
            DeviceInfoExtractor.Medline(input,objInfoList);
        }
        else if(Objects.equals(firmname, "Boston Scientific Corporation")){
            DeviceInfoExtractor.Boston(input,objInfoList);
        }
        else if(Objects.equals(firmname, "Cardinal Health 200, LLC")){
            DeviceInfoExtractor.Cardinal(input,objInfoList);
        }
        else if (Objects.equals(firmname, "Baxter Healthcare Corporation")){
            DeviceInfoExtractor.Baxter(input,objInfoList);
        }
        else if(Objects.equals(firmname, "Philips North America")){
            DeviceInfoExtractor.NonLLCPhilips(input,objInfoList);

        }
        return objInfoList;




    }

}

