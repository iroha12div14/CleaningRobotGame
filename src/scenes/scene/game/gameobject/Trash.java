package scenes.scene.game.gameobject;

import sceneutil.draw.blueprint.Blueprint;

import java.awt.*;
import java.util.List;

public class Trash extends GameObject {
    /**
     * コンストラクタ
     * @param colRadius  当たり判定の半径
     * @param windowSize ウインドウサイズ
     */
    public Trash(
            float x,
            float y,
            float colRadius,
            Dimension windowSize
    ) {
        super(x, y, colRadius, colRadius, windowSize, GameObjectAttribute.TRASH);
    }

    // 移動する物体ではないので使わない
    @Override
    public void accel() { }
    @Override
    public void brake() { }
    @Override
    public void turnLeft() { }
    @Override
    public void turnRight() { }

    @Override
    public List<Blueprint> getBlueprint() {
        // TODO: 色と形をランダムに生成
        Point point = getPoint();
        int radius = (int) getColWidth();

        Blueprint trash = new Blueprint(point.x, point.y, radius);
        trash.setSide(Blueprint.CENTER, Blueprint.CENTER);
        trash.setDrawOvalMode();
        trash.setColor(Color.GRAY);

        return List.of(trash);
    }
}
