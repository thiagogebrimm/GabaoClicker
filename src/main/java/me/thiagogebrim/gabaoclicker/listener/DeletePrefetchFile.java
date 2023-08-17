package me.thiagogebrim.gabaoclicker.listener;

import java.io.IOException;
import java.nio.file.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeletePrefetchFile {
    private static final Logger logger = LogManager.getLogger(DeletePrefetchFile.class);

    public static void executeDeletePrefetchFile() {
        Path filePath = Paths.get("C:\\Windows\\Prefetch\\JAVAW.EXE-452B7DB8");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Erro ao tentar excluir o arquivo prefetch", e);
        }
    }
}
