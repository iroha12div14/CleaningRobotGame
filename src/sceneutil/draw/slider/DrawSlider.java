package sceneutil.draw.slider;

import sceneutil.draw.blueprint.Blueprint;

import java.awt.*;

/** 目盛とその間にポインタを描画し、数値を視覚的に示すスライダーを描画するクラス **/
public class DrawSlider {
    Blueprint sliderLine;
    Blueprint min, max;
    Blueprint pointerInner, pointerFrame;
    Blueprint[] bpScale;

    private final Color frameColor = new Color(250, 150, 0);

    /**
     * スライダーの描画
     * @param x         X座標
     * @param y         Y座標
     * @param width     スライダーの幅
     * @param scale     目盛の数
     * @param pointer   ポインタ(0 ≦ pointer ≦ scale)
     */
    public void drawSlider(Graphics2D g2d, int x, int y, int width, int scale, int pointer) {
        sliderLine = new Blueprint(x, y + 9, width, 2);
        sliderLine.setDrawRectangleMode();
        sliderLine.setColor(Color.WHITE);
        sliderLine.fill(g2d);

        min = new Blueprint(x, y, 2, 20);
        min.setSide(Blueprint.CENTER, Blueprint.TOP);
        min.setDrawRectangleMode();
        min.setColor(Color.WHITE);
        min.fill(g2d);

        max = new Blueprint(x + width, y, 2, 20);
        max.setSide(Blueprint.CENTER, Blueprint.TOP);
        max.setDrawRectangleMode();
        max.setColor(Color.WHITE);
        max.fill(g2d);

        bpScale = new Blueprint[scale];
        for(int i = 0; i < scale; i++) {
            int xs = width * i / scale;
            bpScale[i] = new Blueprint(x + xs, y + 5, x + xs, y + 15);
            bpScale[i].setDrawLineMode();
            bpScale[i].setColor(Color.WHITE);
            bpScale[i].draw(g2d);
        }
        int px = width * pointer / scale;
        int h = pointer == 0 || pointer == scale ? 24 : 16;
        pointerFrame = new Blueprint(x + px, y + 10, 6, h);
        pointerFrame.setSide(Blueprint.CENTER, Blueprint.CENTER);
        pointerFrame.setDrawRectangleMode();
        pointerFrame.setColor(frameColor);
        pointerFrame.fill(g2d);

        pointerInner = new Blueprint(x + px, y + 10, 2, h - 4);
        pointerInner.setSide(Blueprint.CENTER, Blueprint.CENTER);
        pointerInner.setDrawRectangleMode();
        pointerInner.setColor(Color.WHITE);
        pointerInner.fill(g2d);
    }
}
