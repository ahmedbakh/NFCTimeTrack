package org.timetrack.service.impl;

import org.timetrack.model.CardEvent;
import org.timetrack.service.ConfigService;
import org.timetrack.service.ExcelService;

import java.io.IOException;
import java.util.Map;

public class TimeTrackService {
    private ConfigService configService;
    private ExcelService excelService;
    private String lastScannedOwner = "";
    private long lastScannedTime = 0;

    public TimeTrackService(ConfigService configService, ExcelService excelService) {
        this.configService = configService;
        this.excelService = excelService;
    }

    public void processCardScan(String uid, String filePath, String configPath) throws IOException {
        long currentTime = System.currentTimeMillis();

        // Reload the ownership map from the configuration file every time a card is scanned
        Map<String, String> cardOwnershipMap = configService.loadCardOwnershipConfig(configPath);

        // Get owner or default to "Carte non attribuée"
        String owner = cardOwnershipMap.getOrDefault(uid, "Carte non attribuée");

        // Check if this UID was recently scanned
        if (!owner.equals(lastScannedOwner) || (currentTime - lastScannedTime > 3600000)) {
            if (owner.equals("Carte non attribuée")) {
                // Update config only if this is a new or unassigned card
                configService.updateCardOwnershipConfig(uid, owner, configPath);
                cardOwnershipMap.put(uid, owner);
                System.out.println("Updated config file with new UID.");
            }

            CardEvent event = new CardEvent(owner, uid, POIExcelService.getCurrentFormattedDate());
            event.setEventType(determineEventType(currentTime));
            excelService.writeExcelEntry(event, filePath);

            // Update the last scanned UID and time
            lastScannedOwner = owner;
            lastScannedTime = currentTime;
        }
    }

    private String determineEventType(long currentTimeMillis) {
        return currentTimeMillis % 86400000 < 43200000 ? "Entrée" : "Sortie"; // Before or after noon
    }
}
