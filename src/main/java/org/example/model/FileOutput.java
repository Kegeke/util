package org.example.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileOutput {

    private boolean isAppend = false;
    private boolean isFullStatistic = false;
    private boolean isShortStatistic = false;
    private String directory = "";
    private String prefix = "";

    private final List<Path> files = new ArrayList<>();

    public FileOutput() {
    }

    public boolean isAppend() {
        return isAppend;
    }

    public void setAppend(boolean append) {
        isAppend = append;
    }

    public boolean isFullStatistic() {
        return isFullStatistic;
    }

    public void setFullStatistic(boolean fullStatistic) {
        isFullStatistic = fullStatistic;
    }

    public boolean isShortStatistic() {
        return isShortStatistic;
    }

    public void setShortStatistic(boolean shortStatistic) {
        isShortStatistic = shortStatistic;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<Path> getFiles() {
        return files;
    }

    public void addFile(Path path) {
        files.add(path);
    }

    @Override
    public String toString() {
        return "FileOutput{" +
                "isAppend=" + isAppend +
                ", isFullStatistic=" + isFullStatistic +
                ", isShortStatistic=" + isShortStatistic +
                ", path='" + directory + '\'' +
                ", prefix='" + prefix + '\'' +
                ", pathFiles=" + files +
                '}';
    }
}
