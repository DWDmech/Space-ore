package org.home.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.home.game.tween.ActorAccessor;
import org.home.game.utils.AudioManager;
import org.home.game.utils.SettingsUtil;

import static org.home.game.utils.Constants.*;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


public class MainMenuScreen implements Screen {

    private Stage stage;
    private Table table;
    private TextButton btnExit, btnPlay, btnSettings;
    private Label heading, scoreLabel;
    private TextureAtlas atlas;
    private Skin skin;
    private TweenManager tweenManager;


    @Override
    public void show() {
        if (isMusic) {
            AudioManager.getInstance().playMainMusic();
        } else if(AudioManager.getInstance().isPlayingMainMusic()) {
            AudioManager.getInstance().pauseMainMusic();
        }

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/UIPack.atlas");
        skin = new Skin(Gdx.files.internal("ui/menu.json"), atlas);

        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //creating heading
        heading = new Label("", skin);

        //create scoreLabel
        scoreLabel = new Label("Max score: " + maxScoreCount, skin, "main_menu");
        scoreLabel.setFontScale(HEIGHT / 480f);

        //creating button
        btnPlay = new TextButton("New game", skin);
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });
        btnPlay.pad(15f);
        btnPlay.getLabel().setFontScale(HEIGHT / 480f);

        btnExit = new TextButton("Exit", skin);
        btnExit.addListener(new ClickListener(){
           public void clicked(InputEvent e, float x, float y){
               SettingsUtil.getInstance().saveSettings();
               Gdx.app.exit();
           }
        });
        btnExit.getLabel().setFontScale(HEIGHT / 480f);

        btnSettings = new TextButton("Settings", skin);
        btnSettings.addListener(new ClickListener(){
            public void clicked(InputEvent e, float x, float y){
                dispose();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
            }
        });
        btnSettings.pad(10f);
        btnSettings.getLabel().setFontScale(HEIGHT / 480f);

        table.add(heading).spaceBottom(50).size(WIDTH * .438f, HEIGHT * .1669f);
        table.row();
        table.add(scoreLabel).spaceBottom(30);
        table.row();
        table.add(btnPlay).padBottom(10).size(WIDTH * .313f, HEIGHT * .157f);
        table.row();
        table.add(btnSettings).padBottom(10).size(WIDTH * .25f, HEIGHT * .157f);
        table.row();
        table.add(btnExit).padBottom(15).size(WIDTH * .15f, HEIGHT * .146f);
        stage.addActor(table);

        //create animation
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //changing color of label
//        Timeline.createSequence()
//                .beginSequence()
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,0,1))
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,1,0))
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,1,1))
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,0,0))
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,0,1))
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,1,0))
//                .push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,1,1))
//                .end()
//                .repeat(Tween.INFINITY, 0)
//                .start(tweenManager);

        //btn animation
        Timeline.createSequence()
                .beginSequence()
                .push(Tween.set(scoreLabel, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(btnPlay, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(btnSettings, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(btnExit, ActorAccessor.ALPHA).target(0))
                .push(Tween.from(heading, ActorAccessor.ALPHA, .5f).target(0))
                .push(Tween.to(scoreLabel, ActorAccessor.ALPHA, .5f).target(1))
                .push(Tween.to(btnPlay, ActorAccessor.ALPHA, .5f).target(1))
                .push(Tween.to(btnSettings, ActorAccessor.ALPHA, .5f).target(1))
                .push(Tween.to(btnExit, ActorAccessor.ALPHA, .5f).target(1))
                .end().start(tweenManager);

        //table hide
        Tween.from(table, ActorAccessor.ALPHA, .5f).target(0).start(tweenManager);
        Tween.from(table, ActorAccessor.Y, .5f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        stage.act(delta);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }
}
