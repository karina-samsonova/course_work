package com.company;

public class Launcher extends Thread{

    @Override
    public void run(){
        Interface app = new Interface();
        app.launching();
    }
}
