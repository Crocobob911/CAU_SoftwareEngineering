package Yutnori.YutPackage;

public enum YutResult {
    BACK_DO(-1),
    DO(1),
    GAE(2),
    GEOL(3),
    YUT(4),
    MO(5);

    private final int steps;

    YutResult(int steps) {
        this.steps = steps;
    }
    public int getSteps() {return steps;}
    public boolean isBouns() {
        return steps == 4 || steps == 5;
    }
}
