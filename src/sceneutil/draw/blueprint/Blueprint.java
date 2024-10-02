package sceneutil.draw.blueprint;

import sceneutil.draw.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/** 図形を描画するためのパラメータ（設計図）を設定し、描画を行うクラス **/
public class Blueprint {
    // 描画スタイル
    public static final DrawPolygon DRAW_RECT = new DrawRect();
    public static final DrawPolygon DRAW_OVAL = new DrawOval();
    public static final DrawTrapezoid DRAW_TRAPEZOID = new DrawTrapezoid();
    public static final DrawArc DRAW_ARC = new DrawArc();
    public static final DrawLine DRAW_LINE = new DrawLine();

    // 定数
    public static final int CENTER =  0;
    public static final int LEFT   = -1;
    public static final int RIGHT  =  1;
    public static final int TOP    = -1;
    public static final int BOTTOM =  1;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    // 描画モード
    private enum DrawMode {
        POLYGON,
        REGULAR,
        TRAPEZOID,
        ARC,
        LINE,
        LINE_ANGLE,
        RECTANGLE,
        RECTANGLE_REGULAR,
        OVAL,
        OVAL_REGULAR
    }

    // フィールド
    private int anchorX;
    private int anchorY;
    private int width;
    private int height;
    private int radius;
    private int topWidth; // 台形の上辺
    private int bottomWidth; // 台形の下辺
    private int angle;
    private int angle2;
    private int length;
    private int sideX = LEFT;
    private int sideY = TOP;
    private int dir = HORIZONTAL; // 台形の横向き・縦向き
    private DrawMode drawMode; // 描画モード
    private Color color;

    // ----------------------------------------------------------------------- //

    /**
     * 長方形や楕円のパラメータ設定
     * @param anchorX   アンカーのX座標
     * @param anchorY   アンカーのY座標
     * @param width     幅（描画モードがLINE_ANGLEなら長さ扱い）
     * @param height    高さ（描画モードがLINE_ANGLEなら角度扱い）
     */
    public Blueprint(int anchorX, int anchorY, int width, int height) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.width = width;
        this.height = height;
        drawMode = DrawMode.POLYGON;
    }
    /**
     * 正方形や正円のパラメータ設定
     * @param anchorX   アンカーのX座標
     * @param anchorY   アンカーのY座標
     * @param radius    半径
     */
    public Blueprint(int anchorX, int anchorY, int radius) {
        this(anchorX, anchorY, 0, 0);
        this.radius = radius;
        drawMode = DrawMode.REGULAR;
    }
    /**
     * 台形や三角形のパラメータ設定
     * @param anchorX       アンカーのX座標
     * @param anchorY       アンカーのY座標
     * @param topWidth      上辺
     * @param bottomWidth   下辺
     * @param height        高さ
     */
    public Blueprint(int anchorX, int anchorY, int topWidth, int bottomWidth, int height) {
        this(anchorX, anchorY, 0, height);
        this.topWidth = topWidth;
        this.bottomWidth = bottomWidth;
        drawMode = DrawMode.TRAPEZOID;
    }
    /**
     * 弧や扇形のパラメータ設定
     * @param anchorX   アンカーのX座標
     * @param anchorY   アンカーのY座標
     * @param width     幅
     * @param height    高さ
     * @param angle     角度①
     * @param angle2    角度②(角度①からの増分ではない)
     */
    public Blueprint(int anchorX, int anchorY, int width, int height, int angle, int angle2) {
        this(anchorX, anchorY, width, height);
        this.angle = angle;
        this.angle2 = angle2;
        drawMode = DrawMode.ARC;
    }
    /**
     * 設計図のデータをコピー
     */
    public Blueprint(Blueprint bp) {
        anchorX     = bp.anchorX;
        anchorY     = bp.anchorY;
        width       = bp.width;
        height      = bp.height;
        radius      = bp.radius;
        topWidth    = bp.topWidth;
        bottomWidth = bp.bottomWidth;
        angle       = bp.angle;
        angle2      = bp.angle2;
        length      = bp.length;
        sideX       = bp.sideX;
        sideY       = bp.sideY;
        dir         = bp.dir;
        drawMode    = bp.drawMode;
        color       = null;             // 色はコピーしない
    }

    // ----------------------------------------------------------------------- //

    // 上下左右寄せと台形であれば向きの設定
    /**
     * 上下左右寄せの設定
     * @param sideX 左右寄せ LEFTなら左寄せ、RIGHTなら右寄せ
     * @param sideY 上下寄せ TOPなら上寄せ、BOTTOMなら下寄せ
     */
    public void setSide(int sideX, int sideY) {
        this.sideX = sideX;
        this.sideY = sideY;
    }
    /**
     * 台形の上下左右寄せと向きの設定
     * @param sideX 左右寄せ LEFTなら左寄せ、RIGHTなら右寄せ
     * @param sideY 上下寄せ TOPなら上寄せ、BOTTOMなら下寄せ
     * @param dir   向き HORIZONTALなら上下辺が水平、VERTICALなら垂直
     */
    public void setSide(int sideX, int sideY, int dir) {
       setSide(sideX, sideY);
       this.dir = dir;
    }

    /** 描画モードを図形から線（タテ・ヨコ）にする **/
    public void setDrawLineMode() {
        if(drawMode == DrawMode.POLYGON) {
            drawMode = DrawMode.LINE;
        }
    }
    /** 描画モードを図形から線（長さ・角度）にする **/
    public void setDrawLineAngleMode() {
        if(drawMode == DrawMode.POLYGON) {
            drawMode = DrawMode.LINE_ANGLE;
            length = width;
            angle = height;
            width = 0;
            height = 0;
        }
    }
    // 描画モードを指定する
    public void setDrawRectangleMode() {
        if(drawMode == DrawMode.POLYGON) {
            drawMode = DrawMode.RECTANGLE;
        } else if(drawMode == DrawMode.REGULAR) {
            drawMode = DrawMode.RECTANGLE_REGULAR;
        }
    }
    public void setDrawOvalMode() {
        if(drawMode == DrawMode.POLYGON) {
            drawMode = DrawMode.OVAL;
        } else if(drawMode == DrawMode.REGULAR) {
            drawMode = DrawMode.OVAL_REGULAR;
        }
    }

    // パラメータ変更用
    public void setAnchorX (int anchorX) {
        this.anchorX = anchorX;
    }
    public void setAnchorY (int anchorY) {
        this.anchorY = anchorY;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    public void setTopWidth(int topWidth) {
        this.topWidth = topWidth;
    }
    public void setBottomWidth(int bottomWidth) {
        this.bottomWidth = bottomWidth;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }
    public void setAngle2(int angle2) {
        this.angle2 = angle2;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    // パラメータまとめて変更用
    public void setAnchorPoint(Point point){
        setAnchorX(point.x);
        setAnchorY(point.y);
    }
    public void setDimension(Dimension dimension) {
        setWidth(dimension.width);
        setHeight(dimension.height);
    }
    public void setAngles(int angle, int angle2) {
        setAngle(angle);
        setAngle2(angle2);
    }
    public void setTrapezoidDimension(int topWidth, int bottomWidth, int height) {
        setTopWidth(topWidth);
        setBottomWidth(bottomWidth);
        setHeight(height);
    }


    // ----------------------------------------------------------------------- //

    // 座標の取得
    public int X() {
        return anchorX - width * (sideX + 1) / 2;
    }
    public int Y() {
        return anchorY - height * (sideY + 1) / 2;
    }
    // X,Y座標の取得
    public Point getPoint() {
        return new Point(X(), Y());
    }
    public Point getAnchorPoint() {
        return new Point(anchorX, anchorY);
    }
    // Width,Heightの取得
    public Dimension getDimension() {
        return new Dimension(width, height);
    }
    // 色の取得
    public Color getColor() {
        return color;
    }
    // パラメータの取得
    public int[] getParameter() {
        return switch(drawMode) {
            case POLYGON, LINE, RECTANGLE, OVAL
                    -> new int[] {anchorX, anchorY, width, height};
            case REGULAR, RECTANGLE_REGULAR, OVAL_REGULAR
                    -> new int[] {anchorX, anchorY, radius};
            case TRAPEZOID
                    -> new int[] {anchorX, anchorY, topWidth, bottomWidth, height};
            case ARC
                    -> new int[] {anchorX, anchorY, width, height, angle, angle2};
            case LINE_ANGLE
                    -> new int[] {anchorX, anchorY, length, angle};
        };
    }

    // ----------------------------------------------------------------------- //

    // 描画パラメータの作成（汎用）
    private Map<Draw.Param, Integer> param() {
        return switch (drawMode) {
            case POLYGON, RECTANGLE, OVAL
                            -> paramPoly();
            case REGULAR, RECTANGLE_REGULAR, OVAL_REGULAR
                            -> paramReg();
            case TRAPEZOID  -> paramTz();
            case ARC        -> paramArc();
            case LINE       -> paramLine();
            case LINE_ANGLE -> paramLineAngle();
        };
    }
    private Map<Draw.Param, Integer> paramPoly() {
        Map<Draw.Param, Integer> param = new HashMap<>();
        param.put(Draw.Param.X, anchorX);
        param.put(Draw.Param.Y, anchorY);
        param.put(Draw.Param.WIDTH, width);
        param.put(Draw.Param.HEIGHT, height);
        return param;
    }
    private Map<Draw.Param, Integer> paramReg() {
        Map<Draw.Param, Integer> param = new HashMap<>();
        param.put(Draw.Param.X, anchorX);
        param.put(Draw.Param.Y, anchorY);
        param.put(Draw.Param.RADIUS, radius);
        return param;
    }
    private Map<Draw.Param, Integer> paramTz() {
        Map<Draw.Param, Integer> param = new HashMap<>();
        param.put(Draw.Param.X, anchorX);
        param.put(Draw.Param.Y, anchorY);
        param.put(Draw.Param.WIDTH_TOP, topWidth);
        param.put(Draw.Param.WIDTH_BOTTOM, bottomWidth);
        param.put(Draw.Param.HEIGHT, height);
        return param;
    }
    private Map<Draw.Param, Integer> paramArc() {
        Map<Draw.Param, Integer> param = new HashMap<>(paramPoly() );
        param.put(Draw.Param.ANGLE, angle);
        param.put(Draw.Param.ANGLE2, angle2);
        return param;
    }
    private Map<Draw.Param, Integer> paramLine() {
        Map<Draw.Param, Integer> param = new HashMap<>();
        param.put(Draw.Param.X, anchorX);
        param.put(Draw.Param.Y, anchorY);
        param.put(Draw.Param.X2, width);
        param.put(Draw.Param.Y2, height);
        return param;
    }
    private Map<Draw.Param, Integer> paramLineAngle() {
        Map<Draw.Param, Integer> param = new HashMap<>();
        param.put(Draw.Param.X, anchorX);
        param.put(Draw.Param.Y, anchorY);
        param.put(Draw.Param.LENGTH, length);
        param.put(Draw.Param.ANGLE, angle);
        return param;
    }

    // 描画サイドの作成
    private Map<Draw.Side, Integer> side() {
        return switch (drawMode) {
            case POLYGON, REGULAR, ARC, RECTANGLE, OVAL, RECTANGLE_REGULAR, OVAL_REGULAR
                    -> sideStd();
            case TRAPEZOID -> sideTz();
            default -> new HashMap<>();
        };
    }
    private Map<Draw.Side, Integer> sideStd() {
        Map<Draw.Side, Integer> side = new HashMap<>();
        side.put(Draw.Side.X, sideX);
        side.put(Draw.Side.Y, sideY);
        return side;
    }
    private Map<Draw.Side, Integer> sideTz() {
        Map<Draw.Side, Integer> side = new HashMap<>();
        side.put(Draw.Side.X, sideX);
        side.put(Draw.Side.Y, sideY);
        side.put(Draw.Side.DIR, dir);
        return side;
    }

    // ----------------------------------------------------------------------- //

    /**
     * 設計図の描画（線無し）
     * @param drawStyle 描画スタイル
     */
    public void draw(Graphics2D g2d, DrawPolygon drawStyle, Color color) {
        switch(drawMode) {
            case POLYGON:
                drawStyle.draw(g2d, color, param(), side() );
                break;

            case REGULAR:
                drawStyle.drawRegular(g2d, color, param(), side() );
                break;

            case TRAPEZOID:
                DRAW_TRAPEZOID.draw(g2d, color, param(), side() );
                break;

            case ARC:
                DRAW_ARC.draw(g2d, color, param(), side() );
                break;

            case LINE:
                DRAW_LINE.draw(g2d, color, param() );
                break;

            case LINE_ANGLE:
                DRAW_LINE.drawLA(g2d, color, param() );
                break;

            case RECTANGLE:
                DRAW_RECT.draw(g2d, color, param(), side() );
                break;

            case RECTANGLE_REGULAR:
                DRAW_RECT.drawRegular(g2d, color, param(), side() );
                break;

            case OVAL:
                DRAW_OVAL.draw(g2d, color, param(), side() );
                break;

            case OVAL_REGULAR:
                DRAW_OVAL.drawRegular(g2d, color, param(), side() );
                break;
        }
    }
    /** 設計図の描画（線無し、描画スタイル省略） **/
    public void draw(Graphics2D g2d, Color color) {
        switch(drawMode) {
            // POLYGONとREGULARの場合のみ除けて実行
            case POLYGON, REGULAR: break;

            default: draw(g2d, null, color);
        }
    }
    /** 設計図の描画（中塗りあり） **/
    public void fill(Graphics2D g2d, DrawPolygon drawStyle, Color color) {
        switch(drawMode) {
            case POLYGON:
                drawStyle.fill(g2d, color, param(), side() );
                break;

            case REGULAR:
                drawStyle.fillRegular(g2d, color, param(), side() );
                break;

            case TRAPEZOID:
                DRAW_TRAPEZOID.fill(g2d, color, param(), side() );
                break;

            case ARC:
                DRAW_ARC.fill(g2d, color, param(), side() );
                break;

            case RECTANGLE:
                DRAW_RECT.fill(g2d, color, param(), side() );
                break;

            case RECTANGLE_REGULAR:
                DRAW_RECT.fillRegular(g2d, color, param(), side() );
                break;

            case OVAL:
                DRAW_OVAL.fill(g2d, color, param(), side() );
                break;

            case OVAL_REGULAR:
                DRAW_OVAL.fillRegular(g2d, color, param(), side() );
                break;

            default: break;
        }
    }
    /** 設計図の描画（中塗りあり、描画スタイル省略） **/
    public void fill(Graphics2D g2d, Color color) {
        switch(drawMode) {
            // POLYGONとREGULARの場合のみ除けて実行
            case POLYGON, REGULAR: break;

            default: fill(g2d, null, color); break;
        }
    }

    // 色省略版の描画
    public void draw(Graphics2D g2d, DrawPolygon drawStyle) {
        if(color != null) {
            draw(g2d, drawStyle, color);
        }
    }
    public void draw(Graphics2D g2d) {
        if(color != null) {
            draw(g2d, color);
        }
    }
    public void fill(Graphics2D g2d, DrawPolygon drawStyle) {
        if(color != null) {
            fill(g2d, drawStyle, color);
        }
    }
    public void fill(Graphics2D g2d) {
        if(color != null) {
            fill(g2d, color);
        }
    }

    // ----------------------------------------------------------------------- //

}
