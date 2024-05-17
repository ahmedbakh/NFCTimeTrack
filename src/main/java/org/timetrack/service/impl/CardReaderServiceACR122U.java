package org.timetrack.service.impl;

import com.java.smartcard.acr122u.ACR122UReaderHelper;
import org.timetrack.service.CardReaderService;
import com.java.smartcard.acr122u.ACR122UReaderHelper;
import org.timetrack.service.CardReaderService;

import javax.smartcardio.CardException;

public class CardReaderServiceACR122U implements CardReaderService {
    private ACR122UReaderHelper reader;

    public CardReaderServiceACR122U(String readerName) {
        this.reader = ACR122UReaderHelper.getInstance();
    }

    @Override
    public boolean connectReader() {
        try {
            return reader.connectReader();
        } catch (CardException e) {
            System.err.println("Failed to connect to reader: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isCardPresent() {
        try {
            return reader.isCardPresent();
        } catch (CardException e) {
            System.err.println("Failed to check card presence: " + e.getMessage());
            return false;
        }
    }

    @Override
    public byte[] getUID() {
        if (!isCardPresent()) {
            return null;
        }
        return reader.getUID();
    }

    @Override
    public void connectCard(String protocol) {
        try {
            reader.connectCard(protocol);
        } catch (CardException e) {
            System.err.println("Failed to connect to the card using protocol " + protocol + ": " + e.getMessage());
        }
    }
}
