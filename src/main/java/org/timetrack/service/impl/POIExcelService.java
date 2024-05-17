package org.timetrack.service.impl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.timetrack.model.CardEvent;
import org.timetrack.service.ExcelService;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class POIExcelService implements ExcelService {

    @Override
    public void writeExcelEntry(CardEvent event, String filePath, String cardNumber) {
        final int MAX_RETRIES = 5;
        int attempt = 0;
        boolean success = false;

        while (attempt < MAX_RETRIES && !success) {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                try {
                    // Create new workbook and sheet if file doesn't exist or is empty
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("TimeTrack");
                    createHeaderRow(sheet);
                    appendRow(sheet, event, cardNumber); // Add the first entry immediately
                    writeFile(workbook, file);
                    success = true;
                } catch (IOException e) {
                    System.err.println("Error creating new workbook: " + e.getMessage());
                }
            } else {
                try (FileInputStream fis = new FileInputStream(file); // First, open FileInputStream
                     BufferedInputStream bis = new BufferedInputStream(fis); // Wrap it in BufferedInputStream
                     Workbook workbook = WorkbookFactory.create(bis)) { // Use BufferedInputStream
                    Sheet sheet = workbook.getSheetAt(0);

                    appendRow(sheet, event, cardNumber);
                    writeFile(workbook, file);
                    success = true;
                } catch (FileNotFoundException e) {
                    System.err.println("Attempt " + (attempt + 1) + ": Unable to access the file (" + filePath + "). It may be locked by another process.");
                    attempt++;
                    delayRetry();
                } catch (IOException e) {
                    System.err.println("IO Exception on attempt " + (attempt + 1) + ": " + e.getMessage());
                    attempt++;
                }
            }
        }

        if (!success) {
            System.err.println("Failed to write to Excel after " + MAX_RETRIES + " attempts. Moving on without writing this entry.");
        }
    }

    private void appendRow(Sheet sheet, CardEvent event, String cardNumber) {
        int lastRowNum = sheet.getLastRowNum() + 1;
        Row newRow = sheet.createRow(lastRowNum);
        newRow.createCell(0).setCellValue(event.getUid());
        newRow.createCell(1).setCellValue(cardNumber);
        newRow.createCell(2).setCellValue(event.getOwner());
        newRow.createCell(3).setCellValue(event.getDate());
        newRow.createCell(4).setCellValue(determineEventType()); // Determine event type based on time
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("UID");
        headerRow.createCell(1).setCellValue("Card Number");
        headerRow.createCell(2).setCellValue("Owner");
        headerRow.createCell(3).setCellValue("Date");
        headerRow.createCell(4).setCellValue("Event Type");
    }

    private void writeFile(Workbook workbook, File file) throws IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            workbook.write(os);
        }
    }

    private void delayRetry() {
        try {
            Thread.sleep(1000); // wait for 1 second before retrying
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private String determineEventType() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour < 12 ? "Entrée" : "Sortie";
    }

    public static String getCurrentFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
