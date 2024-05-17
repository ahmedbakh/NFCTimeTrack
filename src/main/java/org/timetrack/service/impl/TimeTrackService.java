package org.timetrack.service.impl;

import org.timetrack.model.CardEvent;
import org.timetrack.service.ConfigService;
import org.timetrack.service.ExcelService;

import java.io.IOException;
import java.util.Map;

public class TimeTrackService {
    private ConfigService configService;
    private ExcelService excelService;

    private long lastScannedTime = 0;

    public TimeTrackService(ConfigService configService, ExcelService excelService) {
        this.configService = configService;
        this.excelService = excelService;
    }

    public void processCardScan(String uid, String filePath, String configPath) throws IOException {
        long currentTime = System.currentTimeMillis();

        // Reload the ownership map from the configuration file every time a card is scanned
        Map<String, String[]> cardOwnershipMap = configService.loadCardOwnershipConfig(configPath);

        // Get owner and visitor card or default to "Carte non attribuée"
        String owner = "Carte non attribuée";
        String visitorCard = "CarteVisiteur?";

        if (cardOwnershipMap.containsKey(uid)) {
            String[] values = cardOwnershipMap.get(uid);
            visitorCard = values[0];
            owner = values[1];
        } else if (currentTime - lastScannedTime > 5000) {
            // Update config only if this is a new or unassigned card
            configService.updateCardOwnershipConfig(uid, visitorCard, owner, configPath);
            System.out.println("Updated config file with new UID.");
        }

        // Add the entry to the Excel file only if the card is assigned to someone
        if (!owner.equals("Carte non attribuée")) {
            CardEvent event = new CardEvent(owner, uid, POIExcelService.getCurrentFormattedDate());
            event.setEventType(determineEventType(currentTime));
            excelService.writeExcelEntry(event, filePath, visitorCard);
        }

        lastScannedTime = currentTime;
    }

    private String determineEventType(long currentTimeMillis) {
        return currentTimeMillis % 86400000 < 43200000 ? "Entrée" : "Sortie"; // Before or after noon
    }
}
