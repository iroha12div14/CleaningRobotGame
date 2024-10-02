package sceneutil.draw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/** 直線の描画を行うクラス **/
public class DrawLine implements Draw {
    /**
     * ある点から長さと角度で線を引く
     * @param c     色
     * @param param 描画パラメータ
     */
    public void drawLA(Graphics2D g2d, Color c, Map<Param, Integer> param){
        Map<Param, Integer> p = cartesianFixParam(param);
        draw(g2d, c, p);
    }

    /**
     * 曲座標変換
     * @param param パラメータ
     * @return 変換後のパラメータ
     */
    private Map<Param, Integer> cartesianFixParam(Map<Param, Integer> param){
        double radians = Math.toRadians(param.get(A));
        int px = param.get(X) + (int) (param.get(L) * Math.cos(radians));
        int py = param.get(Y) - (int) (param.get(L) * Math.sin(radians));

        // 部分的な書き換え
        Map<Param, Integer> p = new HashMap<>(param);
        p.put(X2, px);
        p.put(Y2, py);
        return p;
    }

    /**
     * 線の描画
     * @param c     色
     * @param param 描画パラメータ
     */
    @Override
    public void draw(Graphics2D g2d, Color c, Map<Param, Integer> param) {
        g2d.setColor(c);
        g2d.drawLine(param.get(X), param.get(Y), param.get(X2), param.get(Y2));
    }
    /**
     * fillは使わない 中身なし
     */
    @Override
    public void fill(Graphics2D g2d, Color c, Map<Param, Integer> param) { }

    // パラメータの簡略表記
    public final Param X = Param.X;
    public final Param Y = Param.Y;
    public final Param X2 = Param.X2;
    public final Param Y2 = Param.Y2;
    public final Param L = Param.LENGTH;
    public final Param A = Param.ANGLE;
}
