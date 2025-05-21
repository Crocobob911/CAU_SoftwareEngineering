package Yutnori.Model.Observer;


public interface GameModelObserver {
    void onUpdate(ModelChangeType type); // 모델 변경 시 호출되는 메서드
}
