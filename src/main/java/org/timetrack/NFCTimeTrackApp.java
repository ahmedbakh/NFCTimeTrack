package org.timetrack;

import org.timetrack.service.CardReaderService;
import org.timetrack.service.ConfigService;
import org.timetrack.service.ExcelService;
import org.timetrack.service.impl.CardReaderServiceACR122U;
import org.timetrack.service.impl.FileConfigService;
import org.timetrack.service.impl.POIExcelService;
import org.timetrack.service.impl.TimeTrackService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class NFCTimeTrackApp {
    public static void main(String[] args) {
        try {
            ConfigService configService = new FileConfigService();
            ExcelService excelService = new POIExcelService();
            String configPath = "D:\\AstrelyaTimeTrackApp\\EmplyeeCardAssign\\employes_cards_config.txt";
            TimeTrackService service = new TimeTrackService(configService, excelService);
            String baseFilePath = "D:\\AstrelyaTimeTrackApp\\TimeTrackExcel\\";
            CardReaderService reader = new CardReaderServiceACR122U("PcSC");

            if (!reader.connectReader()) {
                System.err.println("Failed to connect to the card reader. Exiting...");
                return;
            }

            while (true) {
                if (reader.isCardPresent()) {
                    reader.connectCard("*");
                    byte[] uid = reader.getUID();
                    if (uid != null) {
                        String scannedUID = byteArrayToHexWithSeparator(uid);
                        System.out.println(scannedUID);
                        String filePath = generateFilePath(baseFilePath);
                        service.processCardScan(scannedUID, filePath, configPath);
                    } else {
                        System.out.println("Unable to read the card UID.");
                    }
                } else {
                    System.out.println("No card detected. Waiting for the card...");
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateFilePath(String baseFilePath) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM_yyyy");
        return baseFilePath + "TimeTrack_" + sdf.format(new Date()) + ".xlsx";
    }

    private static String byteArrayToHexWithSeparator(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02X", bytes[i]));
            if (i < bytes.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }
}
