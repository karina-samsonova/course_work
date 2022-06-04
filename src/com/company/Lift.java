package com.company;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.*;

import static com.company.Interface.buttons;
import static com.company.Interface.waiting_list;

public class Lift extends Thread {
    int direction; //направление движения, если движение происходит (-1 - вниз, 0 - остановка, 1 - вверх)
    int load; //текущая весовая нагрузка кабины (в количестве людей)
    int cur_floor;
    int index;
    int pass_limit;
    int floors_num;
    ArrayList<Passenger> fellows;
    Passenger main_pass;
    StackPane cabin;
    StackPane[] floors;
    int lifts_num;
    int[] cur_floors;
    final int[] dirs;

    public Lift(int i, int pass_limit, int floors_num, StackPane cabin, StackPane[] floors, int lifts_num, int[] cur_floors, int[] dirs){
        this.direction = 0;
        this.load = 0;
        this.cur_floor = 1;
        this.index = i;
        this.pass_limit = pass_limit;
        this.fellows = new ArrayList<>();
        this.main_pass = null;
        this.cabin = cabin;
        this.floors_num = floors_num;
        this.floors = floors;
        this.lifts_num = lifts_num;
        this.cur_floors = cur_floors;
        this.dirs = dirs;
    }

    public int getDir(int dep, int dest) {
        if (dep < dest){
             return 1;
        }
        else if (dep > dest){
            return -1;
        }
        else{
            return 0;
        }
    }
    public void startThread(){
        this.start();
    }
    public synchronized void move(int direction) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Interface.moveCabin(direction, cabin);
            }
        });
        for (Passenger fellow : fellows){
            if (fellow.status == 2){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Interface.moveFellow(direction, fellow);
                    }
                });
            }
        }
        System.out.println(index + " transitioned to " + cur_floor + "");
    }
    public synchronized void getInCabin(Passenger pass) {
        try {
            sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pass.setStatus(2);
        Interface.getInCabin(pass, index);
    }
    public synchronized void dropOff(Passenger pass) {
        try {
            sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Interface.dropOff(pass);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pass.image.setImage(null);
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (waiting_list) {
                synchronized (dirs) {
                    if (direction == 0 && !waiting_list.isEmpty()) {
                        for (Passenger passenger : waiting_list) {
                            if (passenger.status == 0) {
                                int j = 0;
                                for (; j < lifts_num; ++j) {
                                    if (dirs[j] == 0 && Math.abs(passenger.dep - cur_floor) > Math.abs(passenger.dep - cur_floors[j]))
                                        break;
                                }
                                if (j == lifts_num) {
                                    passenger.setStatus(1);
                                    main_pass = passenger;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (main_pass != null) {
                direction = getDir(cur_floor, main_pass.dep);
                dirs[index] = direction;
                while (cur_floor != main_pass.dep) {
                    if (main_pass.direction == 1 && buttons[main_pass.dep - 1].up == 0 || main_pass.direction == -1 && buttons[main_pass.dep - 1].down == 0){
                        main_pass = null;
                        break;
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (cabin) {
                        move(direction);
                        cur_floor += direction;
                        cur_floors[index] = cur_floor;
                        System.out.println(index + " is on " + cur_floor + "");
                    }
                }
                if (main_pass == null){
                    direction = 0;
                    dirs[index] = 0;
                    continue;
                }
                direction = main_pass.direction;
                dirs[index] = direction;
                main_pass = null;
                synchronized (waiting_list){
                    for (int i = 0; i < waiting_list.size(); ++i){
                        if (cur_floor == waiting_list.get(i).dep && direction == waiting_list.get(i).direction){
                            if (load < pass_limit) {
                                waiting_list.get(i).setStatus(2);
                                fellows.add(waiting_list.remove(i));
                                i--;
                                load++;
                                getInCabin(fellows.get(fellows.size()-1));
                                if(direction == 1) {
                                    floors[floors_num - cur_floor].getChildren().get(2).setStyle("-fx-fill: darkgray;");
                                    buttons[cur_floor - 1].setUp(0);
                                }
                                else if(direction == -1) {
                                    floors[floors_num - cur_floor].getChildren().get(3).setStyle("-fx-fill: darkgray;");
                                    buttons[cur_floor - 1].setDown(0);
                                }
                            }
                            else{
                                waiting_list.get(i).setStatus(0);
                                if(direction == 1) {
                                    floors[floors_num - cur_floor].getChildren().get(2).setStyle("-fx-fill: aqua;");
                                    buttons[cur_floor - 1].setUp(1);
                                }
                                else if(direction == -1) {
                                    floors[floors_num - cur_floor].getChildren().get(3).setStyle("-fx-fill: aqua;");
                                    buttons[cur_floor - 1].setDown(1);
                                }
                            }
                        }
                    }
                }
                while (load > 0) {
                    try {
                        sleep(900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (cabin) {
                        move(direction);
                        cur_floor += direction;
                        cur_floors[index] = cur_floor;
                        System.out.println(index + " is on " + cur_floor + "");
                    }
                    for (int i = 0; i < fellows.size(); ++i) {
                        if (cur_floor == fellows.get(i).dest) {
                            dropOff(fellows.get(i));
                            fellows.remove(i);
                            i--;
                            load--;
                            System.out.println("dropped off a fellow " + load + "");
                        }
                    }
                    synchronized (waiting_list){
                        for (int i = 0; i < waiting_list.size(); ++i){
                            if (cur_floor == waiting_list.get(i).dep && direction == waiting_list.get(i).direction){
                                if (load < pass_limit) {
                                    waiting_list.get(i).setStatus(2);
                                    fellows.add(waiting_list.remove(i));
                                    i--;
                                    load++;
                                    getInCabin(fellows.get(fellows.size()-1));
                                    if(direction == 1) {
                                        floors[floors_num - cur_floor].getChildren().get(2).setStyle("-fx-fill: darkgray;");
                                        buttons[cur_floor - 1].setUp(0);
                                    }
                                    else if(direction == -1) {
                                        floors[floors_num - cur_floor].getChildren().get(3).setStyle("-fx-fill: darkgray;");
                                        buttons[cur_floor - 1].setDown(0);
                                    }
                                }
                                else{
                                    waiting_list.get(i).setStatus(0);
                                    if(direction == 1) {
                                        floors[floors_num - cur_floor].getChildren().get(2).setStyle("-fx-fill: aqua;");
                                        buttons[cur_floor - 1].setUp(1);
                                    }
                                    else if(direction == -1) {
                                        floors[floors_num - cur_floor].getChildren().get(3).setStyle("-fx-fill: aqua;");
                                        buttons[cur_floor - 1].setDown(1);
                                    }
                                }
                            }
                        }
                    }
                }
                if (load == 0 && !fellows.isEmpty()) {
                    System.out.println("Oops");
                } else {
                    direction = 0;
                    dirs[index] = 0;
                    System.out.println(index+" is done");
                }
            }
        }
    }
}
