package scenes.scene.title;

import scenes.scene.Scene;
import sceneutil.SceneBase;
import sceneutil.data.GameDataElements;
import sceneutil.data.GameDataIO;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class TitleScene extends SceneBase {

    //コンストラクタ
    public TitleScene(GameDataIO dataIO) {
        // 画面サイズ、FPS、キーアサインの初期化
        dataIO.put(GameDataElements.SCENE, Scene.TITLE);
        init(KEY_ASSIGN, dataIO);

        drawer.setWindowSize(data.getWindowSize() );
        drawer.setBlueprint();

        hiScore = data.get(GameDataElements.HI_SCORE, Integer.class);
    }

    // 描画したい内容はここ
    @Override
    protected void paintField(Graphics2D g2d) {
        drawer.drawBackground(g2d);
        drawer.drawTitle(g2d);
        drawer.drawHiScore(g2d, hiScore);
    }

    // 毎フレーム処理したい内容はここ
    @Override
    protected void actionField() {
        boolean pressEnter = key.getKeyPress(KeyEvent.VK_ENTER);

        if(pressEnter) {
            data.put(GameDataElements.SCORE, 0);
            sceneTransition(Scene.GAME);
        }
    }

    // --------------------------------------------------------------- //

    private final TitleDrawer drawer = new TitleDrawer();

    private final int hiScore;

    // キーアサインの初期化
    private static final List<Integer> KEY_ASSIGN = List.of(
            KeyEvent.VK_ENTER
    );
}
