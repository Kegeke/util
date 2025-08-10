package org.example.service;

import org.example.handler.FileProcessingException;
import org.example.model.FileOutput;
import org.example.model.FileStatistic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilService {

    private final static String ONLY_STRING_REGEX = "^[\\p{L}\\s]+$";
    private final static String ONLY_INTEGER_REGEX = "^[-+]?\\d+$";
    private final static String ONLY_FLOATS_REGEX = "^[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?$";
    private final static String INVALID_OPTION_REGEX = "^(?i)(--.*|-[^aopsf].*|-[aopsf].+|-)$";

    private final FileOutput fileOutput = new FileOutput();

    public void start(String[] params) {
        checkParam(params);
        try {
            validateFile();
        } catch (FileProcessingException e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e);
        }

        Map<String, List<String>> dataTypes = fileReader();
        var statistic = new FileStatistic(dataTypes, fileOutput.isShortStatistic(), fileOutput.isFullStatistic());
        statistic.collectionStatistics();

        try {
            saveFiles(dataTypes,
                    fileOutput.getPrefix(),
                    Path.of(fileOutput.getDirectory()),
                    fileOutput.isAppend());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }

        statistic.printStatistic();
    }

    private void checkParam(String[] params) {
        int len = params.length;

        for (int i = 0; i < len; i++) {
            if (params[i].endsWith(".txt")) {
                fileOutput.addFile(Path.of(params[i]));
                continue;
            }

            if (params[i].matches(INVALID_OPTION_REGEX)) {
                System.out.printf("Недопустимая опция '%s'%n", params[i]);
                continue;
            }

            switch (params[i]) {
                case "-s" -> {
                    fileOutput.setShortStatistic(true);
                }
                case "-f" -> {
                    fileOutput.setFullStatistic(true);
                }
                case "-a" -> {
                    fileOutput.setAppend(true);
                }
                case "-o" -> {
                    if (i + 1 >= len) {
                        System.out.println("Отсутствует путь директории при опции '-о'");
                        continue;
                    }

                    String str = params[i + 1];

                    if (str.matches(".+\\.+$") || str.matches("^(?i)(-[aopsf]$)") || str.isEmpty()) {
                        System.out.println("Неверный формат '-o'");
                        i++;
                        continue;
                    }

                    fileOutput.setDirectory(str);
                    i++;
                }
                case "-p" -> {
                    if (i + 1 >= len) {
                        System.out.println("Отсутствует префикс '-p'");
                        continue;
                    }

                    String str = params[i + 1];

                    if (str.isEmpty() || str.matches(".+\\.+$") || str.matches("^(?i)(-[aopsf]$)")) {
                        System.out.println("Неверный формат '-p'");
                        i++;
                        continue;
                    }

                    fileOutput.setPrefix(str);
                    i++;
                }
            }
        }
    }

    private void validateFile() throws FileProcessingException, IOException {
        List<Path> files = fileOutput.getFiles();

        if (files.isEmpty()) {
            throw new FileProcessingException("Отсутствуют входящие файлы");
        }

        for (Path file : files) {
            if (!Files.exists(file)) {
                throw new FileProcessingException("Файл не найден: " + file);
            }

            if (!Files.isReadable(file)) {
                throw new FileProcessingException("Нет прав на чтение файла: " + file);
            }

            if (Files.readString(file).isEmpty()) {
                System.out.println("Данный файл пуст: " + file.getFileName());
            }
        }
    }

    private Map<String, List<String>> fileReader() {
        Map<String, List<String>> dataTypes = new HashMap<>();
        try {
            List<Path> files = fileOutput.getFiles();
            for (Path file : files) {
                List<String> lines = Files.readAllLines(file);
                sortStrings(lines, dataTypes);
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения файла");
        }

        return dataTypes;
    }

    private void sortStrings(List<String> lines, Map<String, List<String>> dataTypes) {
        String type = "";

        for (String str : lines) {
            if (str.matches(ONLY_STRING_REGEX)) {
                type = "strings";
                dataTypes.computeIfAbsent(type, k -> new ArrayList<>()).add(str);
            } else if (str.matches(ONLY_INTEGER_REGEX)) {
                type = "integers";
                dataTypes.computeIfAbsent(type, k -> new ArrayList<>()).add(str);
            } else if (str.matches(ONLY_FLOATS_REGEX)) {
                type = "floats";
                dataTypes.computeIfAbsent(type, k -> new ArrayList<>()).add(str);
            } else {
                type = "others";
                dataTypes.computeIfAbsent(type, k -> new ArrayList<>()).add(str);
            }
        }
    }

    private void saveFiles(Map<String, List<String>> dataTypes, String prefix,
                           Path directory, boolean isAppend) throws IOException {
        if (!Files.isDirectory(directory)) {
            System.out.println("Не распознан путь сохранения файла");
            fileOutput.setDirectory("");
        }

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        for (Map.Entry<String, List<String>> entry : dataTypes.entrySet()) {
            String fileName = prefix + entry.getKey() + ".txt";
            Path pathFile = directory.resolve(fileName);

            StandardOpenOption[] options;

            if (isAppend && Files.exists(pathFile)) {
                options = new StandardOpenOption[]{
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                };
            } else {
                options = new StandardOpenOption[]{
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                };
            }

            Files.write(
                    pathFile,
                    entry.getValue(),
                    StandardCharsets.UTF_8,
                    options);

        }
    }
}
