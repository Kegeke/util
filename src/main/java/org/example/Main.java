package org.example;

import org.example.service.UtilService;

public class Main {
    public static void main(String[] args) {
        UtilService service = new UtilService();
        service.start(args);
    }
}