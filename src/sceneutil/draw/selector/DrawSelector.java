package sceneutil.draw.selector;

import sceneutil.draw.blueprint.Blueprint;
import sceneutil.font.FontUtil;

import java.awt.*;

/** 文字列配列の一覧を並べ、選択された項目を示すセレクタを作成するクラス **/
public class DrawSelector {
    FontUtil font = new FontUtil();

    Blueprint frame, inner;
    Blueprint selector;

    private final Color frameColor = new Color(250, 150, 0);

    /**
     * 横並びのセレクタを描画する
     * @param strArray  選択できる項目の一覧（文字列配列）
     * @param pointer   選択されている項目
     * @param x         X座標
     * @param y         Y座標
     * @param f         文字のフォント
     */
    public void drawSelector(Graphics2D g2d, String[] strArray, int pointer, int x, int y, Font f) {
        int i = 0;
        int height = font.strHeight(g2d, f);
        for(String str : strArray) {
            int width = font.strWidth(g2d, f, str);
            if(i == pointer) {
                frame = new Blueprint(x - 3, y + 4, width + 5, height + 4);
                frame.setSide(Blueprint.LEFT, Blueprint.BOTTOM);
                frame.setDrawRectangleMode();
                frame.setColor(frameColor);
                frame.fill(g2d);

                inner = new Blueprint(x - 1, y + 2, width + 1, height);
                inner.setSide(Blueprint.LEFT, Blueprint.BOTTOM);
                inner.setDrawRectangleMode();
                inner.setColor(Color.WHITE);
                inner.fill(g2d);

                font.setStr(g2d, f, Color.BLACK);
                font.drawStr(g2d, str, x, y);
            } else {
                font.setStr(g2d, f, Color.WHITE);
                font.drawStr(g2d, str, x, y);
            }
            x += width + 15;
            i++;
        }
    }

    /**
     * 縦並びのセレクタを描画する
     * @param strArray  選択できる項目の一覧（文字列配列）
     * @param cursor    選択されている項目
     * @param x         X座標
     * @param y         Y座標
     * @param f         文字のフォント
     * @param paddingY  項目同士でどれだけ間隔を空けるか
     */
    public void drawVerticalSelector(Graphics2D g2d, String[] strArray, int cursor, int x, int y, Font f, int paddingY) {
        int cursorY = y + paddingY * cursor;

        selector = new Blueprint(x, cursorY, 16, 0, 16);
        selector.setSide(Blueprint.LEFT, Blueprint.BOTTOM, Blueprint.VERTICAL);
        selector.fill(g2d, Color.WHITE);
        selector.draw(g2d, Color.BLACK); // 設計図を使いまわして枠線を描く

        for(String str : strArray) {
            font.setStr(g2d, f, Color.WHITE);
            font.drawStr(g2d, str, x + 24, y);
            y += paddingY;
        }
    }
}
