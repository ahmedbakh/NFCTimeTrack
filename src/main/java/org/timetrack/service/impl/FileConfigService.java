package org.timetrack.service.impl;

import org.timetrack.service.ConfigService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileConfigService implements ConfigService {

    @Override
    public Map<String, String[]> loadCardOwnershipConfig(String filePath) throws IOException {
        Map<String, String[]> cardOwnershipMap = new HashMap<>();
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();  // Ensure file exists
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    cardOwnershipMap.put(parts[0], new String[]{parts[1], parts[2]});
                }
            }
        }
        return cardOwnershipMap;
    }

    @Override
    public synchronized void updateCardOwnershipConfig(String uid, String visitorCard, String owner, String configfilePath) throws IOException {
        try (FileWriter fw = new FileWriter(configfilePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            if (new File(configfilePath).length() > 0) { // Check if file is not empty
                out.println();
            }
            out.println(uid + "|" + visitorCard + "|" + owner);
        } catch (IOException e) {
            System.err.println("Failed to write to config file: " + e.getMessage());
            throw e;
        }
    }
}
