package Yutnori;

import Yutnori.View.View;
import Yutnori.View.Swing.MainFrame_Swing;

// 프로그램의 진입점.
public class StartClass {
    public static void main(String[] args) {
        View view = new MainFrame_Swing(); // Java Swing UI ToolKit 사용
//        View view = new MainApp_FX();  // JavaFX UI ToolKit 사용
        view.StartProgram();
    }
}
