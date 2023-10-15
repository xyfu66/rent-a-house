package com.rent.house.renthouseofjap.until;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetPath {

    // 根据path从resources里面取得对应的File对象，并且返回。如果发现对应path下面没有目标文件，则自动创建。
    public static File getFile4TemplateOrHistory(String path) {
        // AbsolutePath
        Path filePath = Paths.get(new File(path).getAbsolutePath());
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
            }
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new File(path);
    }

    public static Boolean isExist(String path) {
        return Files.exists(Paths.get(path));
    }

}



