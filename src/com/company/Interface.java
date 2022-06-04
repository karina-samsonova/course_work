package com.company;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Interface extends Application {
    static int pass_index = 0;
    static final ArrayList<Passenger> waiting_list = new ArrayList<>();
    static int floors_num;
    static int pass_limit;
    static int lifts_num;
    static StackPane[] floors;
    static Random random = new Random();
    static String[] Urls = new String[]{"file:src/man.png", "file:src/woman.png"};
    static Buttons[] buttons;

    public static void main(String[] args){

    }
    public void launching(){
        Application.launch();
    }
    @Override
    public void start(Stage stage) {
        pass_limit = 5;
        lifts_num = 4;
        floors_num = 10;

        Text txt = new Text("Настройте параметры симуляции:\n");
        Label lbl1 = new Label("Число этажей: 10.0");
        Slider slider1 = new Slider(2.0, 102.0, 10.0);
        slider1.setShowTickMarks(true);
        slider1.setShowTickLabels(true);
        slider1.setBlockIncrement(2.0);
        slider1.setMajorTickUnit(5.0);
        slider1.setMinorTickCount(4);
        slider1.setSnapToTicks(true);
        slider1.valueProperty().addListener((obs, oldval, newVal) ->
                slider1.setValue(newVal.intValue()));
        slider1.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> changed, Number oldValue, Number newValue){
                lbl1.setText("Число этажей: " + newValue);
            }
        });
        Label lbl2 = new Label("Число лифтов: 4.0");
        Slider slider2 = new Slider(1.0, 11.0, 4.0);
        slider2.setShowTickMarks(true);
        slider2.setShowTickLabels(true);
        slider2.setBlockIncrement(2.0);
        slider2.setMajorTickUnit(5.0);
        slider2.setMinorTickCount(4);
        slider2.setSnapToTicks(true);
        slider2.valueProperty().addListener((obs, oldval, newVal) ->
                slider2.setValue(newVal.intValue()));
        slider2.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> changed, Number oldValue, Number newValue){
                lbl2.setText("Число лифтов: " + newValue);
            }
        });
        Label lbl3 = new Label("Вместимость кабины: 5.0");
        Slider slider3 = new Slider(1.0, 21.0, 5.0);
        slider3.setShowTickMarks(true);
        slider3.setShowTickLabels(true);
        slider3.setBlockIncrement(2.0);
        slider3.setMajorTickUnit(5.0);
        slider3.setMinorTickCount(4);
        slider3.setSnapToTicks(true);
        slider3.valueProperty().addListener((obs, oldval, newVal) ->
                slider3.setValue(newVal.intValue()));
        slider3.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> changed, Number oldValue, Number newValue){
                lbl3.setText("Вместимость кабины: " + newValue);
            }
        });
        Button button = new Button("Далее");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pass_limit = (int)slider3.getValue();
                lifts_num = (int)slider2.getValue();
                floors_num = (int)slider1.getValue();
                stage.close();
                Simulation(stage);
            }
        });
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10, txt, lbl1, slider1, lbl2, slider2, lbl3, slider3, button);
        root.setAlignment(Pos.CENTER);
        root.setColumnHalignment(HPos.CENTER);
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.show();
    }
    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
    public void Simulation(Stage stage){
        floors = new StackPane[floors_num];
        buttons = new Buttons[floors_num];
        StackPane[] cabins = new StackPane[lifts_num];
        for (int i = 0; i < lifts_num; ++i){
            Rectangle temp = new Rectangle(45, 50);
            temp.setFill(Color.MEDIUMTURQUOISE);
            temp.setStroke(Color.PALETURQUOISE);
            temp.setStrokeWidth(2);
            cabins[i] = new StackPane();
            cabins[i].getChildren().addAll(temp);
        }
        for (int i = 0; i < floors_num; ++i){
            buttons[i] = new Buttons(0, 0);
            Rectangle rec = new Rectangle(100, 50, Color.GREY);
            rec.setStroke(Color.DARKGREY);
            Text text = new Text(Integer.toString(floors_num-i));
            text.setFill(Color.DARKGREY);
            text.setStyle("-fx-font: 20 arial;");
            floors[i] = new StackPane(rec, text);
            double size = 5.0;
            Polygon up_button = new Polygon(size*1, 0.0, 0.0, size*2, size*2, size*2);
            up_button.setFill(Color.DARKGREY);
            Polygon down_button = new Polygon(0.0, 0.0, size*1, size*2, size*2, 0.0);
            down_button.setFill(Color.DARKGREY);
            floors[i].setMargin(up_button, new Insets(0, 0, 20, 70));
            floors[i].getChildren().add(up_button);
            floors[i].setMargin(down_button, new Insets(20, 0, 0, 70));
            floors[i].getChildren().add(down_button);
        }

        int width = 53*lifts_num+120;
        int height = floors_num*50;
        if (height > 500){
            height = 500;
        }
        VBox stare = new VBox(floors);
        HBox root = new HBox();
        root.setFillHeight(true);
        root.setSpacing(5);
        for (StackPane cabin : cabins) {
            root.getChildren().add(cabin);
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(cabin);
            transition.setByY(25.65*(floors_num-1));
            transition.play();
        }
        root.getChildren().add(stare);
        ScrollPane scroll = new ScrollPane(root);
        scroll.setStyle("-fx-background: rgb(120,120,120);\n -fx-background-color: rgb(120,120,120)");
        scroll.setVvalue(1);
        Scene scene = new Scene(scroll, width, height, Color.GREY);
        stage.setTitle("Elevator system simulator");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        int[] cur_floors = new int[lifts_num];
        Arrays.fill(cur_floors, 1);
        int[] dirs = new int[lifts_num];
        Arrays.fill(dirs, 0);
        Lift[] lifts = new Lift[lifts_num];
        for (int i = 0; i < lifts_num; ++i){
            lifts[i] = new Lift(i, pass_limit, floors_num, cabins[i], floors, lifts_num, cur_floors, dirs);
            lifts[i].startThread();
        }

        Generator gen = new Generator();
        gen.start();
    }
    public static void addPassenger() {
        int dep = 1;
        int dest = floors_num;
        int r = Math.abs(random.nextInt()%4);
        if (r == 0){
            dep = 1;
            dest = random.nextInt(2, floors_num + 1);
        }
        else if (r == 1){
            dep = random.nextInt(2, floors_num + 1);
            dest = 1;
        }
        else {
            dep = random.nextInt(1, floors_num + 1);
            dest = random.nextInt(1, floors_num + 1);
        }
        if (dep == dest) {
            dest = (dest + 1) % (floors_num + 1);
            if (dest == 0)
                dest++;
        }
        r = Math.abs(random.nextInt()%2);
        waiting_list.add(new Passenger(dep, dest, pass_index, 0, new ImageView(Urls[r])));
        if (waiting_list.get(waiting_list.size()-1).direction == 1) {
            if (buttons[waiting_list.get(waiting_list.size() - 1).dep-1].up == 1)
                waiting_list.get(waiting_list.size()-1).setStatus(1);
            else {
                floors[floors_num - waiting_list.get(waiting_list.size() - 1).dep].getChildren().get(2).setStyle("-fx-fill: aqua;");
                buttons[waiting_list.get(waiting_list.size() - 1).dep - 1].setUp(1);
            }
        }
        else {
            if (buttons[waiting_list.get(waiting_list.size() - 1).dep-1].down == 1)
                waiting_list.get(waiting_list.size()-1).setStatus(1);
            else {
                floors[floors_num - waiting_list.get(waiting_list.size() - 1).dep].getChildren().get(3).setStyle("-fx-fill: aqua;");
                buttons[waiting_list.get(waiting_list.size() - 1).dep - 1].setDown(1);
            }
        }
        floors[floors_num-1].setMargin(waiting_list.get(waiting_list.size()-1).image, new Insets(10, 90-(15*waiting_list.size())%50, 0, 0));
        floors[floors_num-1].getChildren().add(waiting_list.get(waiting_list.size()-1).image);
        TranslateTransition transition = new TranslateTransition(Duration.millis(1), waiting_list.get(waiting_list.size()-1).image);
        transition.setByY(-51.3*(waiting_list.get(waiting_list.size()-1).dep-1));
        FadeTransition fade = new FadeTransition(Duration.millis(100), waiting_list.get(waiting_list.size()-1).image);
        fade.setFromValue(10);
        fade.setToValue(0.1);
        fade.setCycleCount(4);
        fade.setAutoReverse(true);
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(transition, fade);
        sequentialTransition.play();
        pass_index++;
    }
    public static void moveCabin(int direction, StackPane cabin){
        TranslateTransition trans_l = new TranslateTransition(Duration.millis(800), cabin);
        trans_l.setByY(-1 * direction * 51.3);
        trans_l.play();
    }
    public static void moveFellow(int direction, Passenger fellow) {
        TranslateTransition trans_f = new TranslateTransition(Duration.millis(800), fellow.image);
        trans_f.setByY(-1 * direction * 51.3);
        trans_f.play();
    }
    public static void getInCabin(Passenger pass, int index) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), pass.image);
        transition.setByX(-51*(lifts_num-index));
        transition.play();
    }
    public static void dropOff(Passenger pass) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), pass.image);
        transition.setByX(50*lifts_num+150);
        transition.play();
    }
}
