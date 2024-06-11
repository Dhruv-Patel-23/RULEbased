package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.RecallData;
import org.example.RecallResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.DeviceInfoExtractor.extractDeviceInfo;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse JSON file
            RecallData recallData = objectMapper.readValue(new File("/Users/ddhpatel/Desktop/FilteredResults.json"), RecallData.class);

            // Get the list of recall results
            List<RecallResult> recalls = recallData.results;

            // Filter results by recalling firm name
            String firmName = "Exactech, Inc.";
            List<RecallResult> filteredRecalls = filterByFirmName(recalls, firmName);




            String outputFilePath = "device_info.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                writer.write("Total number of object identified: "+ filteredRecalls.size()+"\n");
                int i=1;
                for (RecallResult r : filteredRecalls) {
                    writer.write("\nObject Number : "+i+"\n");

                    List<OutputObj> deviceInfoList = extractDeviceInfo(r.product_description+" "+r.code_info );
                    for (OutputObj deviceInfo: deviceInfoList) {
                        writer.write(deviceInfo.toString());

                    }
                   i++;
                }
                System.out.println("Device information written to " + outputFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }









            // Write filtered results to a file
            writeFilteredResultsToFile(filteredRecalls, "/Users/ddhpatel/Desktop/FilteredRecallsOutput.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to filter recall results by recalling firm name
    public static List<RecallResult> filterByFirmName(List<RecallResult> recalls, String firmName) {
        return recalls.stream()
                .filter(recall -> firmName.equals(recall.recalling_firm))
                .toList();
    }

    // Function to write filtered results to a file
    public static void writeFilteredResultsToFile(List<RecallResult> filteredRecalls, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (RecallResult result : filteredRecalls) {
                writer.write(result.toString());
                writer.newLine();  // Adds a new line after each result
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
