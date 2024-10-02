package scenes.scene.title;

import function.calc.CalcUtil;
import sceneutil.SceneDrawer;
import sceneutil.draw.blueprint.Blueprint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TitleDrawer extends SceneDrawer {
    // 背景
    public void drawBackground(Graphics2D g2d) {
        background.fill(g2d);
        background2.fill(g2d);
        for(Blueprint particle : particles) {
            particle.fill(g2d);
        }
    }

    // タイトル
    public void drawTitle(Graphics2D g2d) {
        String strTitle = "Cleaning Robot";
        int strTitleWidth = font.strWidth(g2d, fontTitle, strTitle);
        font.setStr(g2d, fontTitle, Color.GRAY);
        font.drawStr(g2d, strTitle,
                windowWidthHalf - strTitleWidth / 2 + 2,
                windowHeightHalf - 40 + 2
        );
        font.setStr(g2d, fontTitle, Color.WHITE);
        font.drawStr(g2d, strTitle,
                windowWidthHalf - strTitleWidth / 2,
                windowHeightHalf - 40
        );

        String strPEK = "PRESS ENTER KEY";
        int strPEKWidth = font.strWidth(g2d, fontPEK, strPEK);
        font.setStr(g2d, fontPEK, Color.GRAY);
        font.drawStr(g2d, strPEK,
                windowWidthHalf - strPEKWidth / 2 + 1,
                windowHeightHalf + 80 + 1
        );
        font.setStr(g2d, fontPEK, Color.WHITE);
        font.drawStr(g2d, strPEK,
                windowWidthHalf - strPEKWidth / 2,
                windowHeightHalf + 80
        );
    }

    // ハイスコア表示
    public void drawHiScore(Graphics2D g2d, int hiScore) {
        String  strHiScore = "Hi-Score: " + calc.paddingZero(hiScore, 4);
        int strHiScoreWidth = font.strWidth(g2d, fontHiScore, strHiScore);
        font.setStr(g2d, fontHiScore, Color.GRAY);
        font.drawStr(g2d, strHiScore,
                windowWidthHalf - strHiScoreWidth / 2 + 1,
                windowHeightHalf + 150 + 1
        );
        font.setStr(g2d, fontHiScore, Color.WHITE);
        font.drawStr(g2d, strHiScore,
                windowWidthHalf - strHiScoreWidth / 2,
                windowHeightHalf + 150
        );
    }

    @Override
    protected void setBlueprint() {
        background = new Blueprint(0, 0, windowSize.width, windowSize.height);
        background.setDrawRectangleMode();
        background.setColor(Color.BLACK);

        background2 = new Blueprint(background);
        background2.setColor(bg2Color);

        Blueprint particleTemp = new Blueprint(0, 0, 0, 20, 10);
        particleTemp.setSide(Blueprint.CENTER, Blueprint.BOTTOM);
        for(int i = 0 ; i < PARTICLE_COUNT; i++) {
            int x = windowSize.width * i / PARTICLE_COUNT;
            int y = rand.nextInt(windowSize.height);

            Blueprint particle = new Blueprint(particleTemp);
            particle.setAnchorPoint(new Point(x, y) );
            particle.setColor(particleColor);
            particles.add(particle);

            Blueprint particle2 = new Blueprint(particle);
            particle2.setHeight(-10);
            particle2.setColor(particleColor);
            particles.add(particle2);
        }
    }

    @Override
    protected void setAnimationTimer(int frameRate) { }

    @Override
    protected void pastAnimationTimer() { }

    // --------------------------------------------------- //

    private final Random rand = new Random();
    private final CalcUtil calc = new CalcUtil();

    private Blueprint background;
    private Blueprint background2;
    private final List<Blueprint> particles = new ArrayList<>();

    private final Font fontTitle   = font.MSGothic(72, font.BOLD);
    private final Font fontPEK     = font.MSGothic(32, font.BOLD);
    private final Font fontHiScore = font.MSGothic(20, font.BOLD);

    private final Color bg2Color      = new Color(100, 180, 220);
    private final Color particleColor = new Color(255, 230, 160);

    private static final int PARTICLE_COUNT = 20;
}
