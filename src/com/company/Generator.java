package com.company;

import javafx.application.Platform;
import java.util.Random;

public class Generator extends Thread {
    public void run() {

        Random random = new Random();
        while (true) {
            try {
                sleep(random.nextInt(1000, 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("generated");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Interface.addPassenger();
                }
            });
        }
    }
}

