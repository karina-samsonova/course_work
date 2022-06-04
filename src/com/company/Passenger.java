package com.company;

import javafx.scene.image.ImageView;

public class Passenger {
    int dep;
    int dest;
    int direction;
    int status; // 0-нет лифта, 1-ожидает конкретный лифт, 2-едет
    int index;
    ImageView image;

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
    void setStatus(int i){
        status = i;
    }
}
