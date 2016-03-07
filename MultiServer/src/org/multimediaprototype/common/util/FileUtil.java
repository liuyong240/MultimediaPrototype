package org.multimediaprototype.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by haihong.xiahh on 2015/12/29.
 */
public class FileUtil {
    public static Logger log = LogManager.getLogger(FileUtil.class);

    public static String getFileContent(String fileName) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            String content;
            StringBuilder stringBuilder = new StringBuilder();
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content.trim());
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
