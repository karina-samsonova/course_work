package com.company;

/**
 * Класс, запускающий {@link Interface} в отдельном потоке
 */
public class Launcher extends Thread{

    @Override
    public void run(){
        Interface app = new Interface();
        app.launching();
    }
}
