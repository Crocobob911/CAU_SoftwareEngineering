package Yutnori.Model.Observer;


public interface GameEndSubject {
    void registerObserver(GameEndObserver o);
    void removeObserver(GameEndObserver o);
    void notifyObservers(int winner);
}
