package org.timetrack.service;

public interface CardReaderService {
    boolean connectReader();
    boolean isCardPresent();
    byte[] getUID();
    void connectCard(String protocol);
}
