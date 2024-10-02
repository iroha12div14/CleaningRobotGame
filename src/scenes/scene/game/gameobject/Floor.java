package scenes.scene.game.gameobject;

import sceneutil.draw.blueprint.Blueprint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Floor extends GameObject {
    private static final int TATAMI = 0;
    private static final int WOODEN = 1;
    private static final int TILE   = 2;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int WIDTH = 398;
    private static final int HEIGHT = 198;

    private static final int TATAMI_HERI_HEIGHT = 10;
    private static final int WOODEN_HEIGHT = 38;

    private final Color tatamiOmoteColor = new Color(220, 190, 100);
    private final Color tatamiHeriColor  = new Color(40, 80, 20);
    private final Color woodenColor      = new Color(120, 40, 0);
    private final Color tileColor        = new Color(160, 220, 255);

    private final Random rand = new Random();

    private final int dirPattern;
    private final int floorPattern;

    /**
     * コンストラクタ
     * @param windowSize   ウインドウサイズ
     * @param floorPattern 床の模様のパターン
     * @param dirPattern   床の向き
     */
    public Floor(
            float x, float y,
            Dimension windowSize,
            int floorPattern,
            int dirPattern
    ) {
        super(x, y, 0, 0, windowSize, GameObjectAttribute.BACKGROUND);
        this.dirPattern = dirPattern;
        this.floorPattern = floorPattern;
    }

    // 使わない
    @Override
    public void accel() { }
    @Override
    public void brake() { }
    @Override
    public void turnLeft() { }
    @Override
    public void turnRight() { }

    @Override
    public List<Blueprint>  getBlueprint() {
        Point point = getPoint();

        List<Blueprint> blueprints;
        switch (floorPattern) {
            case TATAMI: // 畳
                Blueprint tatamiOmote = new Blueprint(point.x + 1, point.y + 1, 0, 0);
                tatamiOmote.setDrawRectangleMode();
                tatamiOmote.setColor(tatamiOmoteColor);

                Blueprint tatamiHeri1 = new Blueprint(point.x + 1, point.y + 1, 0, 0);
                tatamiHeri1.setDrawRectangleMode();
                tatamiHeri1.setColor(tatamiHeriColor);

                Blueprint tatamiHeri2 = new Blueprint(0, 0, 0, 0);;
                tatamiHeri2.setDrawRectangleMode();
                tatamiHeri2.setColor(tatamiHeriColor);

                // 横長
                if(dirPattern == HORIZONTAL) {
                    tatamiOmote.setDimension(new Dimension(WIDTH, HEIGHT) );
                    tatamiHeri1.setDimension(new Dimension(WIDTH, TATAMI_HERI_HEIGHT) );
                    tatamiHeri2.setDimension(new Dimension(WIDTH, TATAMI_HERI_HEIGHT) );
                    tatamiHeri2.setAnchorPoint(new Point(point.x + 1, point.y + 199 - TATAMI_HERI_HEIGHT) );
                }
                // 縦長
                else if (dirPattern == VERTICAL) {
                    tatamiOmote.setDimension(new Dimension(HEIGHT, WIDTH) );
                    tatamiHeri1.setDimension(new Dimension(TATAMI_HERI_HEIGHT, WIDTH) );
                    tatamiHeri2.setDimension(new Dimension(TATAMI_HERI_HEIGHT, WIDTH) );
                    tatamiHeri2.setAnchorPoint(new Point(point.x + 199 - TATAMI_HERI_HEIGHT, point.y + 1) );
                }
                blueprints = List.of(tatamiOmote, tatamiHeri1, tatamiHeri2);
                break;

            case WOODEN: // フローリング
                blueprints = new ArrayList<>();
                for(int i = 0; i < 5; i++) {
                    Blueprint wooden1;
                    if(dirPattern == HORIZONTAL) {
                        int x = point.x + 1;
                        int y = point.y + (WOODEN_HEIGHT + 2) * i + 1;
                        wooden1 = new Blueprint(x, y, WIDTH, WOODEN_HEIGHT);
                    } else if (dirPattern == VERTICAL) {
                        int x = point.x + (WOODEN_HEIGHT + 2) * i + 1;
                        int y = point.y + 1;
                        wooden1 = new Blueprint(x, y, WOODEN_HEIGHT, WIDTH);
                    } else {
                        wooden1 = new Blueprint(0, 0, 0, 0);
                    }
                    wooden1.setDrawRectangleMode();
                    wooden1.setColor(woodenColor);
                    blueprints.add(wooden1);
                }
                break;

            case TILE:
                blueprints = new ArrayList<>();
                Blueprint floorBackground = new Blueprint(point.x, point.y, 0, 0);
                if(dirPattern == HORIZONTAL) {
                    floorBackground.setDimension(new Dimension(400, 200) );
                } else if (dirPattern == VERTICAL) {
                    floorBackground.setDimension(new Dimension(200, 400) );
                }
                floorBackground.setDrawRectangleMode();
                floorBackground.setColor(Color.WHITE);
                blueprints.add(floorBackground);

                Blueprint tileTemp = new Blueprint(0, 0, 23, 23);
                tileTemp.setDrawRectangleMode();
                int wCount, hCount;
                if(dirPattern == HORIZONTAL) {
                    wCount = 16;
                    hCount = 8;
                } else if (dirPattern == VERTICAL) {
                    wCount = 8;
                    hCount = 16;
                } else {
                    wCount = 1;
                    hCount = 1;
                }
                for(int i = 0; i < wCount; i++) {
                    for(int j = 0; j < hCount; j++) {
                        Blueprint tile = new Blueprint(tileTemp);
                        tile.setAnchorPoint(
                                new Point(point.x + 1 + i * 25, point.y + 1 + j * 25)
                        );
                        tile.setColor(tileColor);
                        blueprints.add(tile);
                    }
                }
                break;

            default:
                Blueprint emptyBp = new Blueprint(0, 0, 0, 0);
                blueprints = List.of(emptyBp);
                break;
        }
        return blueprints;
    }
}
