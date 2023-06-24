package org.wanghailu.detector.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WriteTextLogger implements ILogger {

    private String fileName;

    private BufferedWriter writer;

    public WriteTextLogger(String fileName) throws IOException {
        this.fileName = fileName;
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        writer = new BufferedWriter(fileWriter);
    }

    @Override
    public void info(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void error(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "WriteTextLogger{" + "fileName='" + fileName + '\'' + '}';
    }
}
