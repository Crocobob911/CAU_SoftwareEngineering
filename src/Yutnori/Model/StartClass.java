package Yutnori.Model;

import java.util.Scanner;

public class StartClass {
    public static void main(String[] args) {
        // setting gameBoard
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Player Number 2 ~ 4 :");
        int playerNumber = sc.nextInt();
        System.out.println("Enter Piece Number :");
        int pieceNumber = sc.nextInt();
        System.out.println("Enter boardType :");
        int boardType = sc.nextInt();

        // alert Game Start with setting
        System.out.println("Player : " + playerNumber + "Piece : " + pieceNumber + "BoardType : " + boardType);
        System.out.println("Game start");
        GameSetting gameSetting = new GameSetting(playerNumber,pieceNumber,boardType);


        new GameManager(gameSetting).startScene();       //테스팅
    }
}
