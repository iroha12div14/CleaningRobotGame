package scenes.scene.game.gameobject;

import sceneutil.draw.blueprint.Blueprint;

import java.awt.*;
import java.util.List;

public class OwnMachine extends GameObject {
    private static final float MOV_MAX = 4.0F;
    private static final float MOV_ACCEL = 0.06F;
    private static final float TURN_VAL = 3.0F;

    private static final float BODY_RADIUS = 40;

    private final Color bodyColor1 = new Color(230, 230, 230);
    private final Color bodyColor2 = new Color(180, 180, 180);
    private final Color bodyColor3 = new Color(80, 80, 80);
    private final Color bodyColor4 = new Color(255, 60, 60);

    /**
     * コンストラクタ
     * @param windowSize ウインドウサイズ
     */
    public OwnMachine(int x, int y, Dimension windowSize) {
        super(x, y, BODY_RADIUS, BODY_RADIUS, windowSize, GameObjectAttribute.OWN);
    }

    @Override
    public void accel() {
        if(mov < MOV_MAX) mov += MOV_ACCEL; else mov = MOV_MAX;
    }
    @Override
    public void brake() {
        if(mov > 0.0F) mov -= 0.6F; else mov = 0.0F;
    }
    @Override
    public void turnLeft() {
        dir -= TURN_VAL;
    }
    @Override
    public void turnRight() {
        dir += TURN_VAL;
    }

    @Override
    public List<Blueprint> getBlueprint() {
        Point point = getPoint();
        int radius = (int) getColWidth();

        // 正円を多用するのでテンプレを用意してコピーして使う
        Blueprint circle = new Blueprint(point.x, point.y, radius);
        circle.setSide(Blueprint.CENTER, Blueprint.CENTER);
        circle.setDrawOvalMode();

        Blueprint body0 = new Blueprint(circle);
        body0.setRadius( (int) (radius * 1.25F) );
        body0.setColor(Color.GRAY);

        Blueprint body1 = new Blueprint(circle);
        body1.setRadius( (int) (radius * 1.2F) );
        body1.setColor(bodyColor1);

        Blueprint body2 = new Blueprint(circle);
        body2.setRadius( (int) (radius * 1.0F) );
        body2.setColor(bodyColor2);

        Blueprint body3 = new Blueprint(
                point.x, point.y,
                (int) (radius * 1.5F), (int) (radius * 1.5F),
                (int) (-dir + 60), (int) (-dir - 60)
        );
        body3.setSide(Blueprint.CENTER, Blueprint.CENTER);
        body3.setColor(bodyColor3);

        Blueprint body4 = new Blueprint(circle);
        body4.setRadius( (int) (radius * 0.18F) );
        body4.setColor(bodyColor4);

        return List.of(body0, body1, body2, body3, body4);
    }
}
