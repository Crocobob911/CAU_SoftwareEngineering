package Yutnori.Model;

import org.junit.Test;

public class GameSettingTest{

    @Test
    public void MakeGameSetting(){
        // given
        int playerNumber = 4;
        int pieceNumber = 4;
        int boardType = 4;

        // when
        GameSetting gameSetting = new GameSetting(playerNumber, pieceNumber, boardType);

        // then
        System.out.println(gameSetting);
    }

}