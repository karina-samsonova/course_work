package com.company;

import javafx.scene.image.ImageView;

/**
 * Класс пассажира
 */
public class Passenger {
    int dep;
    int dest;
    int direction;
    int status;
    int index;
    ImageView image;

    /**
     * Конструктор
     * @param dep - этаж отправления
     * @param dest - этаж назначения
     * @param index - индекс пассжира
     * @param status - статус пассажира (0-свободен, 1-ожидает конкретный лифт, 2-едет)
     * @param image - иконка пассажира
     */
    public Passenger(int dep, int dest, int index, int status, ImageView image){
        this.dep = dep;
        this.dest = dest;
        this.status = status;
        this.index = index;
        this.image = image;

        if (dep < dest){
            this.direction = 1;
        }
        else if (dep > dest){
            this.direction = -1;
        }
        else{
            this.direction = 0;
        }

        System.out.println("PASSENGER ADDED: "+dep+", "+dest+"");
    }

    /** Процедура, задающая значение переменной status */
    void setStatus(int i){
        status = i;
    }
}
