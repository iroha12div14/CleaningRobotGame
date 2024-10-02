package scenes.scene.game.gameobject;

import sceneutil.draw.blueprint.Blueprint;

import java.awt.*;
import java.util.List;

abstract public class GameObject {
    // 種別
    private final GameObjectAttribute attribute;
    // 生存の有無
    private boolean isAlive;
    // 座標
    private float x;
    private float y;
    // 移動量と移動方向
    protected float dir;
    protected float mov;
    // 物体の当たり判定の半身
    private final float colWidth;
    private final float colHeight;
    // 画面サイズ
    private final Dimension windowSize;
    private float wallPadding = 0;

    /**
     * コン
     * @param colWidth   当たり判定の半身（横）
     * @param colHeight  当たり判定の半身（縦）
     * @param windowSize ウインドウサイズ
     * @param attribute  属性（自機/ゴミ/背景）
      */
    protected GameObject(
            float x, float y,
            float colWidth, float colHeight,
            Dimension windowSize,
            GameObjectAttribute attribute
    ) {
        this.isAlive = true;
        this.x = x;
        this.y = y;
        this.colWidth = colWidth;
        this.colHeight = colHeight;
        this.windowSize = windowSize;
        this.attribute = attribute;

        mov = 0.0F;
        dir = 0.0F;
    }

    /**
     * 重複判定
     * @param obj 重複判定対象
     */
    public boolean isCollision(GameObject obj) {
        // 重複判定を取らない条件の一覧
        // 1. どちらかのオブジェクトが既に死亡判定されている
        boolean notCollision1
                = !this.isAlive || !obj.isAlive;
        // 2. オブジェクトは互いに同種である
        boolean notCollision2
                = this.attribute == obj.attribute;
        // 3. どちらかのオブジェクトは背景である
        boolean notCollision3
                = this.attribute == GameObjectAttribute.BACKGROUND || obj.attribute == GameObjectAttribute.BACKGROUND;
        if(notCollision1 || notCollision2 || notCollision3) {
            return false;
        }

        // X,Y座標上の重複判定
        boolean collisionX
                =  this.x + this.colWidth > obj.x - obj.colWidth
                && this.x - this.colWidth < obj.x + obj.colWidth;
        boolean collisionY
                =  this.y + this.colHeight > obj.y - obj.colHeight
                && this.y - this.colHeight < obj.y + obj.colHeight;

        return collisionX && collisionY;
    }

    // ----------------------------------------------------------- //

    // 破壊
    public void dead() {
        isAlive = false;
    }

    // 移動速度と方向の設定
    public void setMov(float val) {
        mov = val;
    }
    public void setDir(float val) {
        dir = val;
    }
    // 壁衝突の余白設定
    public void setWallPadding(float val) {
        wallPadding = val;
    }

    // 加減速に伴う物体の移動
    public void move() {
        double dirRadian = Math.toRadians(dir);
        x += (float) (mov * Math.cos(dirRadian) );
        y += (float) (mov * Math.sin(dirRadian) );

        // 壁を超えないようにする
        if(x > windowSize.width - wallPadding) x = windowSize.width - wallPadding;
        else if(x < wallPadding) x = wallPadding;
        if(y > windowSize.height - wallPadding) y = windowSize.height - wallPadding;
        else if(y < wallPadding) y = wallPadding;
    }

    // 抽象メソッド：加減速、ブレーキ、左右旋回、設計図出力
    abstract public void accel();
    abstract public void brake();
    abstract public void turnLeft();
    abstract public void turnRight();
    abstract public List<Blueprint> getBlueprint();

    public boolean isAlive() {
        return isAlive;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getColWidth() {
        return colWidth;
    }
    public float getColHeight() {
        return colHeight;
    }
    public Point getPoint() {
        return new Point( (int) getX(), (int) getY() );
    }
}
