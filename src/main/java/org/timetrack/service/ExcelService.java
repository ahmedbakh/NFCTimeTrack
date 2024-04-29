package org.timetrack.service;


import org.timetrack.model.CardEvent;

import java.io.IOException;

public interface ExcelService {
    void writeExcelEntry(CardEvent cardEvent,String filePath) throws IOException;
}
