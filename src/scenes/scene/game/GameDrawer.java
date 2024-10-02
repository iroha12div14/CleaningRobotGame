package scenes.scene.game;

import function.calc.CalcUtil;
import scenes.scene.game.gameobject.Floor;
import scenes.scene.game.gameobject.Trash;
import sceneutil.SceneDrawer;
import sceneutil.draw.blueprint.Blueprint;
import scenes.scene.game.gameobject.OwnMachine;

import java.awt.*;
import java.util.List;

public class GameDrawer extends SceneDrawer {
    // 背景
    public void drawBackground(Graphics2D g2d) {
        background.fill(g2d);
    }

    // 自機の描画
    public void drawOwnMachine(Graphics2D g2d, OwnMachine own) {
        List<Blueprint> ownMachineBp = own.getBlueprint();
        for(Blueprint parts : ownMachineBp) {
            parts.fill(g2d);
        }
    }

    // ゴミの描画
    public void drawTrash(Graphics2D g2d, List<Trash> trashes) {
        for(Trash trash : trashes) {
            if(trash.isAlive() ) {
                List<Blueprint> trashBp = trash.getBlueprint();
                for(Blueprint parts : trashBp) {
                    parts.fill(g2d);
                }
            }
        }
    }

    // 床
    public void drawFloor(Graphics2D g2d, Floor[] floors) {
        for(Floor floor : floors) {
            List<Blueprint> floorBp = floor.getBlueprint();
            for(Blueprint parts : floorBp) {
                parts.fill(g2d);
            }
        }
    }

    // ステージ番号、カウントダウン、ステージクリア表示
    public void drawLiteralCenter(
            Graphics2D g2d,
            int stageNum,
            int startCountDown,
            boolean finishGameDelay,
            boolean playTimerIsZero,
            int playState
    ) {
        String literalCenter;
        if(startCountDown > 90) literalCenter = "STAGE " + (stageNum + 1);
        else if(startCountDown > 60) literalCenter = "3";
        else if(startCountDown > 30) literalCenter = "2";
        else if(startCountDown > 0) literalCenter = "1";
        else literalCenter = "";

        if(finishGameDelay) {
            centerLiteralBack.fill(g2d);
            if(playState == STAGE_CLEAR) {
                literalCenter = "STAGE CLEAR!";
            }
            else if(playState == STAGE_FAILED) {
                literalCenter = "STAGE FAILED";
            }
        }
        int w = font.strWidth(g2d, fontLiteralCenter, literalCenter);
        font.setStr(g2d, fontLiteralCenter, Color.GRAY);
        font.drawStr(g2d, literalCenter,
                windowWidthHalf - w / 2 + 2, windowHeightHalf - 10 + 2
        );
        font.setStr(g2d, fontLiteralCenter, Color.WHITE);
        font.drawStr(g2d, literalCenter,
                windowWidthHalf - w / 2, windowHeightHalf - 10
        );

        if(finishGameDelay && playTimerIsZero) {
            String pek;
            if(playState == STAGE_CLEAR) {
                if (stageNum < 2) {
                    pek = "Press Enter Next Stage.";
                }
                else {
                    pek = "Press Enter Title Back.";
                }
            }
            else if(playState == STAGE_FAILED) {
                pek = "Press Enter Title Back.";
            }
            else {
                pek = "";
            }
            int pekW = font.strWidth(g2d, fontPek, pek);
            font.setStr(g2d, fontPek, Color.GRAY);
            font.drawStr(g2d, pek,
                    windowWidthHalf - pekW / 2 + 1, windowHeightHalf + 50 + 1
            );
            font.setStr(g2d, fontPek, Color.WHITE);
            font.drawStr(g2d, pek,
                    windowWidthHalf - pekW / 2, windowHeightHalf + 50
            );
        }
    }

    // プレータイム
    public void drawPlayTime(Graphics2D g2d, int playTime) {
        String playTimeStr = "TIME: " + calc.paddingZero(calc.div(playTime, 6), 3);
        font.setStr(g2d, fontPlayTime, Color.GRAY);
        font.drawStr(g2d, playTimeStr, 11, 37);
        font.setStr(g2d, fontPlayTime, Color.RED);
        font.drawStr(g2d, playTimeStr, 10, 36);
    }

    // 点数
    public void drawScore(Graphics2D g2d, int score) {
        String scoreStr = "SCORE: " + calc.paddingZero(score, 4);
        int w = font.strWidth(g2d, fontPlayTime, scoreStr);
        int x = windowSize.width - w - 12;
        int y = 36;
        font.setStr(g2d, fontPlayTime, Color.GRAY);
        font.drawStr(g2d, scoreStr, x + 1, y + 1);
        font.setStr(g2d, fontPlayTime, Color.RED);
        font.drawStr(g2d, scoreStr, x, y);
    }

    // 操作説明
    public void drawOperationExp(Graphics2D g2d) {
        String operationExp = "操作：↑で前進、←→で旋回";
        int oeW = font.strWidth(g2d, fontOe, operationExp);
        int oeH = font.strHeight(g2d, fontOe);
        int x = windowWidthHalf - oeW / 2 + 1;
        int y = windowSize.height - 20;

        Blueprint oeBack = new Blueprint(
                x - 2, y - oeH + 1,
                oeW + 4, oeH + 4
        );
        oeBack.setDrawRectangleMode();
        oeBack.setColor(new Color(0, 0, 0, 200) );
        oeBack.fill(g2d);

        font.setStr(g2d, fontOe, Color.GRAY);
        font.drawStr(g2d, operationExp, x, y + 1);
        font.setStr(g2d, fontOe, Color.WHITE);
        font.drawStr(g2d, operationExp, x, y);
    }

    @Override
    protected void setBlueprint() {
        background = new Blueprint(0, 0, windowSize.width, windowSize.height);
        background.setDrawRectangleMode();
        background.setColor(Color.BLACK);

        centerLiteralBack = new Blueprint(background);
        centerLiteralBack.setColor(colorLiteralBack);
    }

    @Override
    protected void setAnimationTimer(int frameRate) { }

    @Override
    protected void pastAnimationTimer() { }

    // --------------------------------------------------- //

    private final CalcUtil calc = new CalcUtil();

    private Blueprint background;
    private Blueprint centerLiteralBack;

    private final Font fontLiteralCenter = font.Arial(72, font.BOLD);
    private final Font fontPlayTime      = font.Arial(30, font.BOLD);
    private final Font fontPek           = font.Arial(24, font.BOLD);
    private final Font fontOe            = font.MSGothic(18, font.BOLD);

    private final Color colorLiteralBack  = new Color(0, 0, 0, 100);

    private static final int STAGE_CLEAR  = 1;
    private static final int STAGE_FAILED = 2;
}
