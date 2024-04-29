package org.timetrack.service;


import java.io.IOException;
import java.util.Map;

public interface ConfigService {
     Map<String, String> loadCardOwnershipConfig(String filePath) throws IOException;
    void updateCardOwnershipConfig(String uid, String owner, String configfilePath) throws IOException;
}
