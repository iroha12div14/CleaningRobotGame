import scenes.scene.Scene;
import sceneutil.data.GameDataElements;
import sceneutil.data.GameDataIO;
import function.logger.MessageLogger;
import scenes.scene.SceneManager;

import java.awt.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

class Main {

    // メインでやる事
    //      1. ゲーム内で運用するデータの初期化
    //      2. ウインドウを立ち上げて最初の場面を表示
    // 以上
    public static void main(String[] args){
        Main main = new Main(); // ログ出力用
        MessageLogger.printMessage(main, "データの初期化中 ", 0);

        // dataの初期化
        GameDataIO dataIO = gameDataInit(args);

        // ウインドウを立ち上げ、最初の場面を表示する
        SceneManager sceneManager = dataIO.getSceneManager();
        sceneManager.activateDisplay(dataIO);

        MessageLogger.printMessage(main, "データの初期化完了 ", 0);
    }

    // ゲーム内で運用するデータの初期化
    private static GameDataIO gameDataInit(String[] args) {
        GameDataIO dataIO = new GameDataIO();

        // シーン切り替え時に稼働するインスタンスの場所を埋め込んでおく
        dataIO.put(GameDataElements.SCENE_MANAGER, new SceneManager() );
        dataIO.put(GameDataElements.SCENE, Scene.TITLE);

        // ウインドウの設定
        dataIO.put(GameDataElements.WINDOW_NAME,   "ロボット掃除機");
        dataIO.put(GameDataElements.WINDOW_SIZE,   new Dimension(800, 600) );
        dataIO.put(GameDataElements.WINDOW_POINT,  new Point(500, 300) );

        // JARファイルのディレクトリを取得（文字型）
        Path exePath;
        try {
            exePath = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI() );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String exeDir = exePath.getParent().toString();

        dataIO.put(GameDataElements.ROOT_DIRECTORY, exeDir);

        dataIO.put(GameDataElements.SCENE_ID, 1);

        dataIO.put(GameDataElements.FRAME_RATE, 50); // フレームレート
        dataIO.put(GameDataElements.MASTER_VOLUME, 1.0F); //主音量

        dataIO.put(GameDataElements.CHEAT, true); // デバッグ用チート

        dataIO.put(GameDataElements.STAGE_NUM, 0); // ステージ番号
        dataIO.put(GameDataElements.SCORE, 0); // スコア
        dataIO.put(GameDataElements.HI_SCORE, 0); // ハイスコア

        return dataIO;
    }
}
