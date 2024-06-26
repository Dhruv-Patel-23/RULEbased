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
        String refRegex = "REF \\w+";
        String udiregex = "UDI/DI\\s*\\d{14}";
        String lotRegex = "Lot Numbers:\\s*([\\w+,\\s]+)";

        // Compile the regex
        Pattern refPattern = Pattern.compile(refRegex);
        Matcher refMatcher = refPattern.matcher(input);

        Pattern udiPattern = Pattern.compile(udiregex);
        Matcher udiMatcher = udiPattern.matcher(input);


        Pattern lot = Pattern.compile(lotRegex);
        Matcher lotMatcher = lot.matcher(input);

        while ((refMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {

            OutputObj deviceInfo = new OutputObj();
            deviceInfo.ref_number.add(refMatcher.group().substring(4));
            deviceInfo.UDI.add(udiMatcher.group().substring(7));
            deviceInfo.lot_number.add(lotMatcher.group().substring(13));




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
        System.out.println(input);
        System.out.println("-----------------------------------");
        while ((prodMatcher.find()) && udiMatcher.find() && lotMatcher.find()) {

            OutputObj deviceInfo = new OutputObj();
            deviceInfo.product_number.add(prodMatcher.group().substring(12));
            deviceInfo.UDI.add(udiMatcher.group().substring(6));
            deviceInfo.lot_number.add(lotMatcher.group().substring(12));




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
        return objInfoList;




    }
//
//        List<OutputObj> objInfoList = new ArrayList<>();
//        // Regular expression patterns
//        String itemPattern = "(\\d{3}-\\d{2}-\\d{2})";
//        String udiPattern = "UDI/DI\\s([\\d]+)";
//        String serialPattern = "Serial Numbers:\\s([\\d,\\s]+)";
//        String catPattern = "Catalog Numbers:\\s(((\\d+)-)+)\\d";
//        String gtinPattern = "GTIN\\s([\\d]+)";
//
//        Pattern itemNumPattern = Pattern.compile(itemPattern);
//        Pattern udiNumPattern = Pattern.compile(udiPattern);
//        Pattern serialNumPattern = Pattern.compile(serialPattern);
//        Pattern catNumPattern = Pattern.compile(catPattern);
//        Pattern gtinNumPattern = Pattern.compile(catPattern);
//
//
//        // Finding all matches
//        Matcher itemNumMatcher = itemNumPattern.matcher(input);
//        Matcher udiNumMatcher = udiNumPattern.matcher(input);
//        Matcher serialNumMatcher = serialNumPattern.matcher(input);
//        Matcher catNumMatcher = catNumPattern.matcher(input);
//        Matcher gtinNumMatcher = catNumPattern.matcher(input);
//
//        String regex = "\\b(\\w{2}-\\w{3}-\\w{2}-\\d{4,5}), GTIN (\\d{14}), Serial Numbers: ([\\w, ]+);?";
//        String regex2 = "(\\d{3}-\\d{2}-\\d{2}), GTIN (\\d{14}), Serial Numbers: ([\\w, ]+);?";
//        Pattern re = Pattern.compile(regex);
//        Pattern re2 = Pattern.compile(regex2);
//
//        Matcher rema= re.matcher(input);
//        Matcher rema2= re2.matcher(input);
//
//       while (rema.find()){
//           OutputObj deviceInfo = new OutputObj();
//           deviceInfo.catalog_number.add(rema.group(1));
//           deviceInfo.GTIN.add(rema.group(2));
//
//
//           String[] serials = rema.group(3).split(",\\s*");
//           deviceInfo.serial_number.addAll(Arrays.asList(serials));
//
//
//
//           objInfoList.add(deviceInfo);
//
//       }
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
//            for (String serial : serials) {
//                deviceInfo.serial_number.add(serial);
//            }
//
//
//
//            objInfoList.add(deviceInfo);
//        }

        // From Firm : Fresenius Medical Care Holdings, Inc.
//         regex = "Product Code:\\s*(\\w+)|UDI-DI:\\s*([\\d+]+(?: \\(\\+ serial number\\))?)|Serial Numbers:\\s*([\\w\\d]+(?:\\s*to\\s*[\\w\\d]+)?)";
//        // Compile the regex
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(input);
//
//        // Initialize variables to hold extracted values
//        String productCode = null;
//        String udiDi = null;
//        String serialNumbers = null;
//
//        // Find and extract values
//        while (matcher.find()) {
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
//
//            objInfoList.add(deviceInfo);
//        }
//
//        return objInfoList;
//    }

//    public static void main(String[] args) {
//        String input = "a) Item Number 314-02-32, UDI/DI 10885862175052, Serial Numbers: 2090670, 1984370, 2086996, 2090655, 2108671, 2086997, 2086999, 2379424, 2479807, 2640374, 2142682, 2147846, 2142679, 2479798, 2090659, 2147857, 2147847, 2640379, 2090665, 2086983, 2640371, 2142675, 2147834, 2086981, 2640406, 2086998, 1984369, 2147853, 2640397, 1984379, 2628334, 2640377, 2640375, 2640384, 2752430, 3570463, 3570474, 2640372, 2142674, 1984378, 2379426, 2640380, 2640391, 2640394, 2640395, 2640403, 2640410, 2379418, 2594876, 2640385, 3616755, 3616759, 3758373, 3616754, 3570473, 2983574, 3570462, 3570461, 3570454, 1984381, 2086989, 2147814, 2086988, 2147828, 2640408, 2594877, 2958057, 3970792, 2788748, 3970793, 2752423, 3970794, 4469187, 3616757, 4587423, 2379417, 2594878, 2788750, 2752418, 3570460, 4469183, 2958061, 3570459, 4591595, 4657525, 4657526, 4807291, 4657530, 2752421, 4469189, 4209410, 3570469, 3616758, 4602056, 5349352, 4591598, 5161352, 4485953, 4141933, 4409893, 4469185, 4520246, 5540785, 5540764, 5279160, 5540774, 5540781, 5540777, 5540773, 5929633, 5929646, 4497764, 4807288, 5519031, 5161362, 5161370, 5929643, 5929635, 5929624, 4409904, 5349329, 5349336, 5349327, 6846318, 6130751, 6130754, 6130763, 6846297, 6846301, 6846314, 6823520, 4651619, 6823534, 5929622, 7008973, 7008965, 5540789, 5929640; b) Item Number 314-02-33, UDI/DI 10885862175069, Serial Numbers: 2084927, 1985029, 2084773, 2141202, 1985036, 1985027, 2084781, 2084788, 2084783, 1985020, 2084904, 2084930, 1985024, 1985031, 2084771";
//
//        List<Map<String, Object>> deviceInfoList = extractDeviceInfo(input);
//
//        // Print the extracted device information
//        for (Map<String, Object> deviceInfo : deviceInfoList) {
//            System.out.println("Item Number: " + deviceInfo.get("Item Number"));
//            System.out.println("UDI/DI: " + deviceInfo.get("UDI/DI"));
//            System.out.println("Serial Numbers: " + deviceInfo.get("Serial Numbers"));
//            System.out.println();
//        }
//    }
}

