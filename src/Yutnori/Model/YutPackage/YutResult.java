package Yutnori.Model.YutPackage;

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
    public boolean isBonus() {
        return steps == 4 || steps == 5;
    }

    public static YutResult fromSteps(int steps) {  //gpt 산 int to enum 코드
        for (YutResult result : values()) {
            if (result.getSteps() == steps) {
                return result;
            }
        }
        throw new IllegalArgumentException("Invalid steps: " + steps);
    }

    public static boolean contains(int steps) {
        for (YutResult result : values()) {
            if (result.steps == steps) {
                return true;
            }
        }
        return false;
    }
}
