package org.home.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.home.game.screen.GameScreen;
import static org.home.game.utils.Constants.*;

public class GameInterface implements Disposable{
    public Stage stage;
    private Table table;
    private Skin skin;
    private TextureAtlas atlas;
    private TextButton btnLeft, btnRight, btnJump;
    private static Label scoreLabel;
    private static int scoreCount;

    public GameInterface(final GameScreen screen) {
        scoreCount = 0;
        stage = new Stage(new ScreenViewport());
        table =  new Table();
        table.setBounds(0, 0, WIDTH, HEIGHT);

        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("ui/UIPack.atlas");
        skin = new Skin(Gdx.files.internal("ui/menu.json"), atlas);

        // Score label
        scoreLabel = new Label(String.format("Score: %06d", scoreCount), skin, "game");
        scoreLabel.setPosition(2, 2);
        scoreLabel.setFontScale(HEIGHT / 480f);

        // Create Buttons
        btnLeft = new TextButton("", skin, "gameLeft");
        btnLeft.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.isMoveLeft = false;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.isMoveLeft = true;
                return true;
            }
        });
        btnLeft.padLeft(25);
        btnLeft.padRight(25);

        btnRight = new TextButton("", skin, "gameRight");
        btnRight.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.isMoveRight = false;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.isMoveRight = true;
                return true;
            }
        });
        btnRight.padLeft(25);
        btnRight.padRight(25);

        btnJump = new TextButton("", skin, "gameUp");
        btnJump.addListener(new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int btn) {
                screen.player.jump();
                return true;
            }
        });
        btnJump.padLeft(25);
        btnJump.padRight(25);

        table.add(scoreLabel).expand().colspan(3).top();
        table.row();
        table.add(btnLeft).left().size(stage.getWidth() * .15f, stage.getHeight() * .15f);
        table.add(btnRight).left().size(stage.getWidth() * .15f, stage.getHeight() * .15f);
        table.add(btnJump).expandX().right().size(stage.getWidth() * .15f, stage.getHeight() * .15f);
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public void update(float delta) {
    }

    public static void reset(){
        scoreCount = 0;
        scoreLabel.setText(String.format("Score: %06d", scoreCount));
    }

    public static void addScore(int i) {
        scoreCount += i;
        scoreLabel.setText(String.format("Score: %06d", scoreCount));
    }

    public static int getScoreCount() {
        return scoreCount;
    }
}
