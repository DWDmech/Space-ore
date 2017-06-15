package org.home.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.home.game.tween.SpriteAccessor;
import org.home.game.utils.SettingsUtil;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;


public class LoadingScreen implements Screen {

    private SpriteBatch sb;
    private Sprite loading;
    private TweenManager tweenManager;
    @Override
    public void show() {
        sb = new SpriteBatch();

        Texture loadingTexture = new Texture(Gdx.files.internal("img/loading.png"));
        loading = new Sprite(loadingTexture);
        loading.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.set(loading, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(loading, SpriteAccessor.ALPHA, 2)
                .target(1)
                .repeatYoyo(1, 2)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        dispose();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                    }
                 }).start(tweenManager);
        new Thread(){
            @Override
            public void run() {
                SettingsUtil.getInstance().loadingSettings();
            }
        }.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        sb.begin();
        loading.draw(sb);
        sb.end();
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
        sb.dispose();
        loading.getTexture().dispose();
    }
}
