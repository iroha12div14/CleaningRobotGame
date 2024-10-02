package sceneutil.data;

/** ゲーム内でやり取りされるデータの要素 **/
public enum GameDataElements {
    // 使用ディレクトリ名
    ROOT_DIRECTORY,     // String

    // 保存するデータ
    FRAME_RATE,         // int
    MASTER_VOLUME,      // float

    // 場面の管理
    SCENE_MANAGER,      // SceneManager
    SCENE,              // Scene
    SCENE_ID,           // int

    // ウインドウの名前やサイズ
    WINDOW_NAME,        // String
    WINDOW_POINT,       // java.awt.Point
    WINDOW_SIZE,        // java.awt.Dimension

    CHEAT,              // boolean

    // ゲーム内情報
    STAGE_NUM,          // int
    SCORE,              // int
    HI_SCORE,           // int
}
