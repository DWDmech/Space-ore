package org.home.game.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.home.game.utils.SettingsUtil;

import static org.home.game.utils.Constants.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SettingsScreen implements Screen{

    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private Table table;
    private CheckBox checkSounds, checkMusic;
    private TextButton btnBack;

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/UIPack.atlas");
        skin = new Skin(Gdx.files.internal("ui/menu.json"), atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        checkSounds = new CheckBox("Sounds", skin);
        checkSounds.setChecked(isSounds);
        checkSounds.addListener(new ClickListener(){
            public void clicked(InputEvent e, float x, float y){
                isSounds = checkSounds.isChecked();
            }
        });
        checkSounds.getLabel().setFontScale(HEIGHT / 480f);

        checkMusic = new CheckBox(" Music ", skin);
        checkMusic.setChecked(isMusic);
        checkMusic.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                isMusic = checkMusic.isChecked();
            }
        });
        checkMusic.getLabel().setFontScale(HEIGHT / 480f);

        btnBack = new TextButton("<- Back", skin);
        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SettingsUtil.getInstance().saveSettings();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });
        btnBack.getLabel().setFontScale(HEIGHT / 480f);

        Label label = new Label("SETTINGS", skin, "settings");
        label.setFontScale(HEIGHT / 480f);

        table.add(label).size(WIDTH * .26f, HEIGHT * .12f).padBottom(25).expandX();
        table.row();
        table.add(checkSounds).size(WIDTH * .29f, HEIGHT * .157f);
        table.row();
        table.add(checkMusic).size(WIDTH * .29f, HEIGHT * .157f);
        table.row();
//        table.add(btnBack).right().padTop(50).padRight(15).size(200, 75);
        table.add(btnBack).right().padTop(50).padRight(15).size(WIDTH * .25f, HEIGHT * .157f);
        stage.addActor(table);


        stage.addAction(sequence(moveTo(0, stage.getHeight()), moveTo(0, 0, .5f))); // coming in from top animation
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        SettingsUtil.getInstance().saveSettings();
        stage.dispose();
        skin.dispose();
        atlas.dispose();
    }
}
