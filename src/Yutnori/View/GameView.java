package Yutnori.View;

import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.GameModelObserver;

import java.util.function.Consumer;

public interface GameView extends GameModelObserver {
    void setThrowYutMethod(Consumer<Integer> method);
    void setThrowRandomYutMethod(Consumer<Integer> method);

}