package org.example.model;

import java.util.List;
import java.util.Map;

public class FileStatistic {

    private final Map<String, List<String>> dataTypes;

    private List<String> strings;
    private List<String> integers;
    private List<String> floats;
    private List<String> others;

    private boolean shortStatistic = false;
    private boolean fullStatistic = false;

    public FileStatistic(Map<String, List<String>> dataTypes, boolean shortStatistic, boolean fullStatistic) {
        this.dataTypes = dataTypes;
        this.shortStatistic = shortStatistic;
        this.fullStatistic = fullStatistic;
    }

    public void collectionStatistics() {
        for (Map.Entry<String, List<String>> data : dataTypes.entrySet()) {
            if (data.getKey().equals("strings")) {
                strings = data.getValue();
            }
            if (data.getKey().equals("integers")) {
                integers = data.getValue();
            }
            if (data.getKey().equals("floats")) {
                floats = data.getValue();
            }
            if (data.getKey().equals("others")) {
                others = data.getValue();
            }
        }
    }

    public void printStatistic() {
        if (shortStatistic) {
            shortStatistic();
        }

        if (fullStatistic) {
            fullStatistic();
        }
    }

    private void shortStatistic() {
        System.out.println("Краткая статистика");
        System.out.println(strings != null ? "Количество строк: " + strings.size() : "");
        System.out.println(integers != null ? "Количество чисел: " + integers.size() : "");
        System.out.println(floats != null ? "Количество чисел с плавающей точкой: " + floats.size() : "");
        System.out.println(others != null ? "Количество неопределенных элементов: " + others.size() : "");
        shortStatistic = false;
    }

    private void fullStatistic() {
        if (shortStatistic) {
            shortStatistic();
        }

        System.out.println("\nПолная статистика");
        stringsStatistics();
        integersStatistics();
        floatsStatistics();
        othersStatistics();
    }

    private void stringsStatistics() {
        if (strings == null) {
            return;
        }

        int maxLen = 0;
        int minLen = Integer.MAX_VALUE;

        for (String str : strings) {
            if (str.length() > maxLen) {
                maxLen = str.length();
            }
            if (str.length() < minLen) {
                minLen = str.length();
            }
        }

        System.out.println("Длина самой длинной строки: " + maxLen);
        System.out.println("Длина самой короткой строки: " + minLen);
    }

    private void integersStatistics() {
        if (integers == null) {
            return;
        }

        long min = Long.MAX_VALUE;
        long max = 0;
        long sum = 0;
        long avg = 0;

        for (String integer : integers) {
            long num = Long.parseLong(integer);
            sum += num;

            if (num > max) {
                max = num;
            }

            if (num < min) {
                min = num;
            }

        }

        avg = sum / integers.size();

        System.out.println("Минимальное число: " + min);
        System.out.println("Максимальное число: " + max);
        System.out.println("Сумма чисел: " + sum);
        System.out.println("Общее среднее число: " + avg);
    }

    private void floatsStatistics() {
        if (floats == null) {
            return;
        }

        double min = Double.MAX_VALUE;
        double max = 0;
        double sum = 0;
        double avg = 0;

        for (String fl : floats) {
            double num = Double.parseDouble(fl);
            sum += num;

            if (num > max) {
                max = num;
            }

            if (num < min) {
                min = num;
            }

        }

        avg = sum / integers.size();

        System.out.println("Минимальное число с плавающей точкой: " + min);
        System.out.println("Максимальное число с плавающей точкой: " + max);
        System.out.printf("Сумма чисел с плавающей точкой: %.2f%n", sum);
        System.out.printf("Общее среднее число с плавающей точкой: %.2f%n", avg);
    }

    private void othersStatistics() {
        int sum = 0;

        for (String other : others) {
            sum += other.length();
        }

        System.out.println("Количество символов в неопределенных строках: " + sum);
    }
}
