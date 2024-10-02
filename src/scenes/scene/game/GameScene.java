package scenes.scene.game;

import function.logger.MessageLogger;
import scenes.scene.Scene;
import scenes.scene.game.gameobject.Floor;
import scenes.scene.game.gameobject.OwnMachine;
import scenes.scene.game.gameobject.Trash;
import sceneutil.animtimer.AnimationTimer;
import sceneutil.data.GameDataElements;
import sceneutil.data.GameDataIO;
import sceneutil.SceneBase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class GameScene extends SceneBase {
    //コンストラクタ
    public GameScene(GameDataIO dataIO) {
        // 画面サイズ、FPS、キーアサインの初期化
        dataIO.put(GameDataElements.SCENE, Scene.GAME);
        init(KEY_ASSIGN, dataIO);

        Dimension windowSize = data.getWindowSize();
        frameRate = data.getFrameRate();

        drawer.setWindowSize(windowSize);
        drawer.setBlueprint();

        cheat = data.get(GameDataElements.CHEAT, Boolean.class);

        // 自機の配置
        ownMachine = new OwnMachine(400, 430, windowSize);
        ownMachine.setWallPadding(ownMachine.getColWidth() );
        ownMachine.setDir(-90.0F);

        // ゴミの配置
        stageNum = data.get(GameDataElements.STAGE_NUM, Integer.class);
        trashCount = TRASH_COUNT_DEF[stageNum];
        for(int i = 0; i < trashCount; i++) {
            float x = PLACE_PADDING_X + (windowSize.width - PLACE_PADDING_X * 2) * i / trashCount;
            float y = PLACE_PADDING_Y + rand.nextFloat(windowSize.height - PLACE_PADDING_Y * 2);
            int radius = TRASH_SIZE_DEF[stageNum];
            Trash trash = new Trash(x, y, radius, windowSize);
            trashes.add(trash);
        }

        // 床の配置
        floors = new Floor[6];
        floors[0] = new Floor(  0,   0, windowSize, stageNum, 0);
        floors[1] = new Floor(400,   0, windowSize, stageNum, 0);
        floors[2] = new Floor(200, 200, windowSize, stageNum, 0);
        floors[3] = new Floor(200, 400, windowSize, stageNum, 0);
        floors[4] = new Floor(  0, 200, windowSize, stageNum, 1);
        floors[5] = new Floor(600, 200, windowSize, stageNum, 1);

        // タイマー定義
        startCountDownTimer   = new AnimationTimer(frameRate, 180, false);
        playTimer = new AnimationTimer(frameRate, PLAY_TIMER_VAL);
        finishGameDelayTimer  = new AnimationTimer(frameRate, 30);
        scoreBonusBeforeTimer = new AnimationTimer(frameRate, 60);

        // 変数の初期化
        score = data.get(GameDataElements.SCORE, Integer.class);
        playState = PLAYING;
    }

    // 描画したい内容はここ
    @Override
    protected void paintField(Graphics2D g2d) {
        drawer.drawBackground(g2d);         // 背景
        drawer.drawFloor(g2d, floors);      // 床
        drawer.drawTrash(g2d, trashes);     // ゴミ
        drawer.drawOwnMachine(g2d, ownMachine); // 自機
        drawer.drawPlayTime(g2d, playTimer.getTimer() ); // タイマー
        drawer.drawScore(g2d, score);       // スコア
        drawer.drawOperationExp(g2d);       // 操作説明
        drawer.drawLiteralCenter(g2d,
                stageNum,
                startCountDownTimer.getTimer(),
                finishGameDelayTimer.isZero(),
                playTimer.isZero(),
                playState
        ); // 中央のデカ文字
    }

    // 毎フレーム処理したい内容はここ
    @Override
    protected void actionField() {
        // デバッグ用
        debugOnKeyPress(
                key.getKeyPress(KeyEvent.VK_D),
                key.getKeyPress(KeyEvent.VK_G),
                key.getKeyPress(KeyEvent.VK_A),
                key.getKeyPress(KeyEvent.VK_R),
                key.getKeyPress(KeyEvent.VK_Q)
        );

        // 自機の操作と動き
        ownMachineControl(
                key.getKeyHold(KeyEvent.VK_UP),
                key.getKeyHold(KeyEvent.VK_LEFT),
                key.getKeyHold(KeyEvent.VK_RIGHT)
        );

        // ゴミと自機の当たり判定
        trashCollision();

        // 合否の分岐
        judgeClearOrFailed();

        // Enterキーを押してシーン移動
        sceneTransitionOnPressEnter(
                key.getKeyPress(KeyEvent.VK_ENTER)
        );

        // タイマーによる時間制御
        timerControl();
    }

    // --------------------------------------------------------------- //

    // 自機の操作と挙動
    private void ownMachineControl(boolean keyHoldUP, boolean keyHoldLEFT, boolean keyHoldRIGHT) {
        // カウントダウン後かつ合否がまだ決まってない時に、動ける
        if (startCountDownTimer.isZero() && playState == PLAYING) {
            // ↑を押してアクセル、↑を押していなければブレーキ
            if (keyHoldUP) {
                ownMachine.accel();
            }
            else {
                ownMachine.brake();
            }

            // 方向転換（←→両方を押している場合は動かない）
            if (keyHoldLEFT && !keyHoldRIGHT) {
                ownMachine.turnLeft();
            }
            else if (keyHoldRIGHT && !keyHoldLEFT) {
                ownMachine.turnRight();
            }
        }
        // それ以外は常にブレーキ
        else {
            ownMachine.brake();
        }

        // 自機の座標更新（常時）
        ownMachine.move();
    }

    // ゴミと自機の当たり判定
    private void trashCollision() {
        if(startCountDownTimer.isZero() && playState == PLAYING) {
            // ゴミと自機の当たり判定
            for(Trash trash : trashes) {
                if (trash.isCollision(ownMachine) ) {
                    trash.dead();
                    trashCount--;
                    score += 10;
                }
            }
        }
    }

    // 合否の分岐
    private void judgeClearOrFailed() {
        if(playState == PLAYING) {
            int frameRate = data.getFrameRate();
            // ゴミが0個以下になった場合はクリア
            if (trashCount <= 0) {
                playState = CLEAR;
                // ボーナス用のタイマーを定義
                int timeBonusFrame = playTimer.getTimer();
                scoreBonusTimer = new AnimationTimer(frameRate, timeBonusFrame);
            }
            // プレータイマーが0になったらゲームオーバー
            else if (playTimer.isZero() ) {
                playState = FAILED;
                scoreBonusTimer = new AnimationTimer(frameRate, 0);
            }
        }
    }

    // Enterキーを押してシーン移動
    private void sceneTransitionOnPressEnter(boolean keyPressENTER) {
        if (playTimer.isZero() && finishGameDelayTimer.isZero() && keyPressENTER) {
            // クリア時の分岐
            if (playState == CLEAR) {
                // STAGE1,2をクリアしたら進出、STAGE3をクリアしたらTITLEに戻る
                if (stageNum < STAGE_MAX - 1) {
                    proceedNextStage();
                } else {
                    backTitle();
                }
            }
            // ゲームオーバーならTITLEに戻る
            else if (playState == FAILED) {
                backTitle();
            }
        }
    }
    // スコアを持ち越して次のステージに進む
    private void proceedNextStage() {
        data.put(GameDataElements.SCORE, score);
        data.put(GameDataElements.STAGE_NUM, stageNum + 1);
        sceneTransition(Scene.GAME);
    }
    // ハイスコアを更新してタイトルに戻る
    private void backTitle() {
        if(data.get(GameDataElements.HI_SCORE, Integer.class) < score) {
            data.put(GameDataElements.HI_SCORE, score);
        }
        data.put(GameDataElements.STAGE_NUM, 0);
        sceneTransition(Scene.TITLE);
    }

    // タイマーによる時間制御
    private void  timerControl() {
        // カウントダウン
        if ( !startCountDownTimer.isZero() ) {
            startCountDownTimer.pass();
        }
        // 残り時間の消費
        else if (playState == PLAYING) {
            playTimer.pass();
        }
        // ゲーム終了の表示にちょっと間を置く
        else if ( !finishGameDelayTimer.isZero() ) {
            finishGameDelayTimer.pass();
        }
        // ステージクリア時の制御
        else if (playState == CLEAR) {
            // タイムボーナスの加算にちょっと間を置く
            if ( !scoreBonusBeforeTimer.isZero() ) {
                scoreBonusBeforeTimer.pass();
            }
            // タイムボーナスの加算
            else if ( !scoreBonusTimer.isZero() ) {
                for (int i = 0; i < 10; i++) {
                    if ( !playTimer.isZero() || !scoreBonusTimer.isZero() ) {
                        score++;
                    }
                    scoreBonusTimer.pass();
                    playTimer.pass();
                }
            }
        }
    }

    // デバッグ用チート
    private void debugOnKeyPress(
            boolean keyPressD, boolean keyPressG, boolean keyPressA, boolean keyPressR, boolean keyPressQ
    ) {
        // Dでコンソールにゲーム内情報を出力
        if (keyPressD) {
            printDataDump();
        }
        if (cheat) {
            // Gでプレータイマーを0、Aでゴミ全消去、Rでリトライ、Qでタイトルに戻る
            if (keyPressG) {
                playTimer.setZero();
                MessageLogger.printMessage(this, "  Debug: プレータイマーを0にしました", 1);
            }
            else if (keyPressA) {
                for (Trash trash : trashes) {
                    trash.dead();
                }
                trashCount = 0;
                MessageLogger.printMessage(this, "  Debug: 全てのゴミを消去しました", 1);
            }
            else if (keyPressR) {
                sceneTransition(Scene.GAME);
                MessageLogger.printMessage(this, "  Debug: 同ステージをリセットします", 1);
            }
            else if (keyPressQ) {
                data.put(GameDataElements.STAGE_NUM, 0);
                MessageLogger.printMessage(this, "  Debug: タイトルに戻ります", 2);
                sceneTransition(Scene.TITLE);
            }
        }
    }

    // コンソールにデバッグ情報を表示
    private void printDataDump() {
        MessageLogger.printMessage(this, "[DEBUG]");
        System.out.println("**** TIMER ****");
        System.out.println("  StartCountDown: " + startCountDownTimer.getTimer() );
        System.out.println("  PlayTimer: " + playTimer.getTimer() );
        System.out.println("  FinishGameDelay: " + finishGameDelayTimer.getTimer() );
        System.out.println("  ScoreBonusBefore: " + scoreBonusBeforeTimer.getTimer() );
        if(scoreBonusTimer != null) {
            System.out.println("  ScoreBonus: " + scoreBonusTimer.getTimer() );
        } else {
            System.out.println("  ScoreBonus: null");
        }
    }

    // --------------------------------------------------------------- //

    private final Random rand = new Random();

    // 描画用インスタンス
    private final GameDrawer drawer = new GameDrawer();

    // ゲーム上のオブジェクト
    private final OwnMachine ownMachine;
    private final List<Trash> trashes = new ArrayList<>();
    private final Floor[] floors;

    // タイマー
    private final AnimationTimer startCountDownTimer;
    private final AnimationTimer playTimer;
    private final AnimationTimer finishGameDelayTimer;
    private final AnimationTimer scoreBonusBeforeTimer;
    private AnimationTimer scoreBonusTimer; // ステージクリアかゲームオーバー時に確定する

    // ステージ定義
    private static final int STAGE_MAX = 3;
    private static final int[] TRASH_COUNT_DEF = new int[] {7, 40, 230};
    private static final int[] TRASH_SIZE_DEF = new int[] {16, 8, 4};
    private static final int PLAY_TIMER_VAL = 2400;
    private static final float PLACE_PADDING_X = 30.0F;
    private static final float PLACE_PADDING_Y = 40.0F;
    private static final int PLAYING = 0;
    private static final int CLEAR   = 1;
    private static final int FAILED  = 2;

    private final int frameRate;
    private final int stageNum;

    // 変数
    private int score;
    private int trashCount;
    private int playState;

    private final boolean cheat;

    // キーアサインの初期化
    private static final List<Integer> KEY_ASSIGN = Arrays.asList(
            KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_ENTER,
            KeyEvent.VK_D, KeyEvent.VK_G, KeyEvent.VK_A, KeyEvent.VK_R, KeyEvent.VK_Q
    );
}
