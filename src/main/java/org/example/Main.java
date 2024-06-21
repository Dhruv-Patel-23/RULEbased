package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.example.DeviceInfoExtractor.extractDeviceInfo;

public class Main {

    public static ArrayList<OutputObj> outputObs() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            RecallData recallData = objectMapper.readValue(new File("/Users/ddhpatel/Desktop/2324.json"), RecallData.class);
            List<RecallResult> recalls = recallData.results;
            Map<String, Integer> FirmObjCount = new HashMap<>();
            ArrayList <OutputObj> AllDeviceInfos = new ArrayList<>();
            for (RecallResult result : recalls) {
                String firmName = result.recalling_firm;
                Set<OutputObj> deviceInfoList = extractDeviceInfo(result.product_description + " " + result.code_info,result.recalling_firm,result.recall_number);
                if(!deviceInfoList.isEmpty()){

                    if (!FirmObjCount.containsKey(firmName)) {
                        FirmObjCount.put(firmName,1);
                    }
                    else{
                        FirmObjCount.put(firmName, FirmObjCount.get(firmName) + 1);
                    }

                    String outputFilePath = firmName + ".txt";
                    AllDeviceInfos.addAll(deviceInfoList);
                }

            }


            String outputFilePath ="finalOut.txt";
            try (FileWriter writer = new FileWriter(outputFilePath,true)) {
                //  writer.write("\nObject Number: " + FirmObjCount.get(firmName) + "\n");
                for (OutputObj deviceInfo : AllDeviceInfos) {
                    writer.write(deviceInfo.toString());
                    writer.write("\n");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        return AllDeviceInfos;

        } catch (IOException e) {
            e.printStackTrace();
        }

       return null;
    }
}