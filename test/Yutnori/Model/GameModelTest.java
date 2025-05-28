package Yutnori.Model;

import org.junit.Test;

public class GameModelTest{

    @Test
    public void initNewPieceTest(){
        // given
        GameModel gameModel = new GameModel();
        GameSetting gameSetting = new GameSetting(4,4,4);
        gameModel.startModel(gameSetting);

        // when
        gameModel.initNewPiece();

        // then
        System.out.println(gameModel.getPiece(-1).getOwnerID()
                + " "
                + gameModel.getPiece(-1).getPosition());

    }

}