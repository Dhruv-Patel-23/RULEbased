
package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import java.io.IOException;

public class DeviceInfoExtractor {

    public static void ExtractData(String input, Set<OutputObj> objInfoList,String recall,String name) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonFileName="/Users/ddhpatel/Desktop/RULEbased/src/main/res/"+name;
    if(name.endsWith(".")){
        jsonFileName=jsonFileName+"json";
    }
    else{
        jsonFileName=jsonFileName+".json";
    }
    JsonNode rootNode = objectMapper.readTree(new File(jsonFileName));
    JsonNode patterns = rootNode.get("patterns");


    for (JsonNode patternNode : patterns) {
        String udiIdx = patternNode.get("multvaludi").asText();
        List<Integer> udiIdxList = grpIdx(udiIdx);

        String gtinIdx = patternNode.get("multvalgtin").asText();
        List<Integer> gtinIdxList = grpIdx(gtinIdx);

        String modelIdx = patternNode.get("modelidx").asText();
        List<Integer> modelIdxList = grpIdx(modelIdx);

        String catalogIdx = patternNode.get("catalogidx").asText();
        List<Integer> catalogIdxList = grpIdx(catalogIdx);

        String refIdx = patternNode.get("refidx").asText();
        List<Integer> refIdxList = grpIdx(refIdx);

        String reorderIdx = patternNode.get("reorderidx").asText();
        List<Integer> reorderIdxList = grpIdx(reorderIdx);

        String productIdx = patternNode.get("productidx").asText();
        List<Integer> productIdxList = grpIdx(productIdx);

        String itemIdx = patternNode.get("itemidx").asText();
        List<Integer> itemIdxList = grpIdx(itemIdx);

        String batchIdx = patternNode.get("batchidx").asText();
        List<Integer> batchIdxList = grpIdx(batchIdx);

        String lotIdx = patternNode.get("lotidx").asText();
        List<Integer> lotIdxList = grpIdx(lotIdx);

        String serialIdx = patternNode.get("serialidx").asText();
        List<Integer> serialIdxList = grpIdx(serialIdx);

        String partIdx = patternNode.get("partidx").asText();
        List<Integer> partIdxList = grpIdx(partIdx);

        String upnIdx = patternNode.get("upnidx").asText();
        List<Integer> upnIdxList = grpIdx(upnIdx);

        String upcIdx = patternNode.get("upcidx").asText();
        List<Integer> upcIdxList = grpIdx(upcIdx);

        String kitIdx = patternNode.get("kitidx").asText();
        List<Integer> kitIdxList = grpIdx(kitIdx);

        String model = patternNode.get("model").asText();
        String udi = patternNode.get("udi").asText();
        String serial = patternNode.get("serial").asText();
        String gtin = patternNode.get("gtin").asText();
        String batch = patternNode.get("batch").asText();
        String lot = patternNode.get("lot").asText();
        String product = patternNode.get("product").asText();
        String part = patternNode.get("part").asText();
        String upn = patternNode.get("upn").asText();
        String upc = patternNode.get("upc").asText();
        String kit = patternNode.get("kit").asText();
        String item = patternNode.get("item").asText();
        String catalog = patternNode.get("catalog").asText();
        String ref = patternNode.get("ref").asText();
        String reorder = patternNode.get("reorder").asText();

        Pattern modelPat = Pattern.compile(model, Pattern.CASE_INSENSITIVE);
        Pattern udiPat = Pattern.compile(udi, Pattern.CASE_INSENSITIVE);
        Pattern serialPat = Pattern.compile(serial, Pattern.CASE_INSENSITIVE);
        Pattern gtinPat = Pattern.compile(gtin, Pattern.CASE_INSENSITIVE);
        Pattern batchPat = Pattern.compile(batch, Pattern.CASE_INSENSITIVE);
        Pattern lotPat = Pattern.compile(lot, Pattern.CASE_INSENSITIVE);
        Pattern productPat = Pattern.compile(product, Pattern.CASE_INSENSITIVE);
        Pattern partPat = Pattern.compile(part, Pattern.CASE_INSENSITIVE);
        Pattern upnPat = Pattern.compile(upn, Pattern.CASE_INSENSITIVE);
        Pattern upcPat = Pattern.compile(upc, Pattern.CASE_INSENSITIVE);
        Pattern kitPat = Pattern.compile(kit, Pattern.CASE_INSENSITIVE);
        Pattern refPat = Pattern.compile(ref, Pattern.CASE_INSENSITIVE);
        Pattern reorderPat = Pattern.compile(reorder, Pattern.CASE_INSENSITIVE);
        Pattern catalogPat = Pattern.compile(catalog, Pattern.CASE_INSENSITIVE);
        Pattern itemPat = Pattern.compile(item, Pattern.CASE_INSENSITIVE);

        Matcher modelMat = modelPat.matcher(input);
        Matcher udiMat = udiPat.matcher(input);
        Matcher serialMat = serialPat.matcher(input);
        Matcher gtinMat = gtinPat.matcher(input);
        Matcher batchMat = batchPat.matcher(input);
        Matcher lotMat = lotPat.matcher(input);
        Matcher productMat = productPat.matcher(input);
        Matcher partMat = partPat.matcher(input);
        Matcher upnMat = upnPat.matcher(input);
        Matcher upcMat = upcPat.matcher(input);
        Matcher kitMat = kitPat.matcher(input);
        Matcher refMat = refPat.matcher(input);
        Matcher reorderMat = reorderPat.matcher(input);
        Matcher catalogMat = catalogPat.matcher(input);
        Matcher itemMat = itemPat.matcher(input);
        while (modelMat.find() && udiMat.find() && serialMat.find() && gtinMat.find() && upnMat.find() && upcMat.find()
                && itemMat.find() && productMat.find() && partMat.find() && catalogMat.find() && batchMat.find()
                && lotMat.find() && kitMat.find() && refMat.find() && reorderMat.find()) {
            OutputObj deviceInfo = new OutputObj();
            deviceInfo.recalling_firm = name;
            deviceInfo.recall_number = recall;
            if (!model.isEmpty()) {
                for (Integer idx : modelIdxList) {
                    deviceInfo.model_number.add(modelMat.group(idx));
                }
            }

            if (!udi.isEmpty()) {
                for (Integer idx : udiIdxList) {
                    if(udiMat.group(idx)!=null) {
                        deviceInfo.UDI.add(udiMat.group(idx));
                    }
                    else{
                        System.out.println(recall);
                    }
                }
            }

            if (!serial.isEmpty()) {
                for (Integer idx : serialIdxList) {
                    deviceInfo.serial_number.add(serialMat.group(idx));
                }

            }

            if (!gtin.isEmpty()) {

                for (Integer idx : gtinIdxList) {
                    deviceInfo.GTIN.add(gtinMat.group(idx));
                }

            }

            if (!upn.isEmpty()) {
                for (Integer idx : upnIdxList) {
                    deviceInfo.UPN.add(upnMat.group(idx));
                }
            }

            if (!upc.isEmpty()) {
                for (Integer idx : upcIdxList) {
                    deviceInfo.UPC.add(upcMat.group(idx));
                }

            }

            if (!item.isEmpty()) {
                for (Integer idx : itemIdxList) {
                    deviceInfo.item_number.add(itemMat.group(1));
                }

            }

            if (!product.isEmpty()) {
                for (Integer idx : productIdxList) {
                    deviceInfo.product_number.add(productMat.group(idx));
                }

            }

            if (!part.isEmpty()) {
                for (Integer idx : partIdxList) {
                    deviceInfo.part_number.add(partMat.group(idx));
                }

            }

            if (!catalog.isEmpty()) {
                for (Integer idx : catalogIdxList) {
                    deviceInfo.catalog_number.add(catalogMat.group(idx));
                }

            }

            if (!batch.isEmpty()) {
                for (Integer idx : batchIdxList) {
                    deviceInfo.batch_number.add(batchMat.group(idx));
                }

            }

            if (!lot.isEmpty()) {
                for (Integer idx : lotIdxList) {
                    deviceInfo.lot_number.add(lotMat.group(idx));
                }

            }

            if (!kit.isEmpty()) {
                for (Integer idx : kitIdxList) {
                    deviceInfo.kit_number.add(kitMat.group(idx));
                }

            }

            if (!ref.isEmpty()) {
                for (Integer idx : refIdxList) {
                    deviceInfo.ref_number.add(refMat.group(idx));
                }

            }

            if (!reorder.isEmpty()) {
                for (Integer idx : reorderIdxList)
                    deviceInfo.reorder_number.add(reorderMat.group(idx));

            }

            objInfoList.add(deviceInfo);

        }

    }

}


    private static List<Integer> grpIdx(String Idx){
        List<Integer> udiIdxList = new ArrayList<>();
        if(!Idx.isEmpty()) {
            String[] parts = Idx.split(",");

            // Convert each part to an integer and store in a list

            for (String part : parts) {
                udiIdxList.add(Integer.parseInt(part));
            }
        }
        else{
            udiIdxList.add(1);
        }
        return udiIdxList;

    }

    public static void Baxter(String input, Set<OutputObj> objInfoList,String recall,String name) {

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
            deviceInfo.recalling_firm=name;
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
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;            deviceInfo.recalling_firm=name;

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
            OutputObj deviceInfo = new OutputObj(); deviceInfo.recall_number=recall;            deviceInfo.recalling_firm=name;

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
            deviceInfo.recalling_firm=name;

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
            deviceInfo.recalling_firm=name;

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
            deviceInfo.recalling_firm=name;

            deviceInfo.UDI.add(regMatcher1.group(1));
            deviceInfo.ref_number.add(regMatcher2.group(1));
            deviceInfo.lot_number.add(regMatcher1.group(2));


            objInfoList.add(deviceInfo);
            done =true;
        }






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

       public static Set<OutputObj> extractDeviceInfo(String input, String firmname, String recall) throws IOException {
        Set<OutputObj> objInfoList = new HashSet<>();

            if(Objects.equals(firmname, "Exactech, Inc."))
        {
            DeviceInfoExtractor.ExtractData(input, objInfoList, recall, "Exactech, Inc.");
        }
            else if (Objects.equals(firmname, "Fresenius Medical Care Holdings, Inc."))
        {
            DeviceInfoExtractor.ExtractData(input, objInfoList, recall, "Fresenius Medical Care Holdings, Inc.");
        }


         else if (Objects.equals(firmname, "MEDLINE INDUSTRIES, LP - Northfield")) {
            DeviceInfoExtractor.ExtractData(input, objInfoList, recall,firmname);
        } else if (Objects.equals(firmname, "Boston Scientific Corporation")) {
            DeviceInfoExtractor.ExtractData(input, objInfoList, recall,firmname);
        } else if (Objects.equals(firmname, "Cardinal Health 200, LLC")) {
            DeviceInfoExtractor.ExtractData(input, objInfoList, recall,firmname);
        } else if (Objects.equals(firmname, "Baxter Healthcare Corporation")) {
            DeviceInfoExtractor.Baxter(input, objInfoList, recall,firmname);
        } else if (Objects.equals(firmname, "Philips North America")) {
            DeviceInfoExtractor.ExtractData(input, objInfoList, recall,firmname);
        }
        else if(Objects.equals(firmname, "Howmedica Osteonics Corp.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Becton Dickinson & Co.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Angiodynamics, Inc.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Universal Meditech Inc.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Aizu Olympus Co., Ltd.")){
            DeviceInfoExtractor.Aizu(input,objInfoList,recall);
        }
        else if(Objects.equals(firmname, "Zimmer, Inc.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Philips North America Llc")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Integra LifeSciences Corp.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Medtronic Inc., Cardiac Rhythm and Heart Failure (CRHF)")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Hobbs Medical, Inc.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "ROi CPS LLC")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "C.R. Bard Inc")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "Stryker Corporation")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if(Objects.equals(firmname, "American Contract Systems, Inc.")){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if((Objects.equals(firmname, "Biomerieux Inc"))){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if((Objects.equals(firmname, "Linkbio Corp."))){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,firmname);
        }
        else if((Objects.equals(firmname, "Stradis Medical, LLC dba Stradis Healthcare"))){
            DeviceInfoExtractor.ExtractData(input,objInfoList,recall,"Stradis Medical, LLC dba Stradis Healthcare");
        }
        return objInfoList;

    }

}

