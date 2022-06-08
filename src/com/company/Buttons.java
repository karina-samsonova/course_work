package com.company;

/**
 * Класс кнопок на этаже, хранящий состояния кнопок "вверх" и "вниз"
 */
public class Buttons {
    int up;
    int down;

    /**
     * Конструктор
     * @param up - кнопка вверх
     * @param down - кнопка вниз
     */
    Buttons(int up, int down){
        this.up = up;
        this.down = down;
    }

    /** Процедура, задающая значение переменной up */
    public void setUp(int up){
        this.up = up;
    }

    /** Процедура, задающая значение переменной down */
    public void setDown(int down) {
        this.down = down;
    }
}
