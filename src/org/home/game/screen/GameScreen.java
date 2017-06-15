package org.home.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.home.game.sprites.Tube;
import org.home.game.sprites.items.Helmet;
import org.home.game.sprites.items.Item;
import org.home.game.sprites.items.ItemDef;
import org.home.game.sprites.killingobject.Asteroid;
import org.home.game.sprites.GameBorder;
import org.home.game.sprites.Martian;
import org.home.game.utils.AudioManager;
import org.home.game.utils.GameInterface;
import org.home.game.utils.WorldContactListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static org.home.game.utils.Constants.*;



public class GameScreen extends ScreenAdapter {
    // Camera screen
    private SpriteBatch batch;

    private OrthographicCamera cam;
    private Viewport gamePort;
    private GameInterface gameInterface;
    //Box2d variables
    private World world;

    private Box2DDebugRenderer b2dr;
    // Sprites
    public Martian player;

    private List<Asteroid> asteroids;
    private List<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;
    //Map variables
    private GameBorder map;
    private final Tube tube;

    public GameScreen() {
        Gdx.input.setCatchBackKey(true);
        batch = new SpriteBatch();

        cam = new OrthographicCamera();
        gamePort = new FitViewport(WIDTH / PPM, HEIGHT / PPM, cam);
        gameInterface = new GameInterface(this);

        cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -HEIGHT * 1.5f / PPM), true);
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        map = new GameBorder(this);
        tube = new Tube(this);
        player = new Martian(this);

        asteroids = new ArrayList<Asteroid>();
        for(int i = 0; i < 1; i++){
            asteroids.add(new Asteroid(this, getRandomX(), getRandomY()));
        }

        items = Collections.synchronizedList(new ArrayList<Item>());
        for(int i = 0; i < 1; i++){
            items.add(new Helmet(this, getRandomX(), getRandomY()));
        }
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();
        Timer.schedule(new Timer.Task() {
            public void run() {
                spawnItem(new ItemDef(new Vector2(getRandomX(), getRandomY()), Asteroid.class));
            }
        }, 3, 3f);

        Timer.schedule(new Timer.Task() {
            public void run() {
                spawnItem(new ItemDef(new Vector2(getRandomX(), getRandomY()), Helmet.class));
            }
        }, 3, 20);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        map.draw(batch);
        player.draw(batch);
        for (Asteroid as0: asteroids ) {
            as0.draw(batch);
        }
        tube.draw(batch);
        for (Item it0: items){
            it0.draw(batch);
        }
        batch.end();

//        b2dr.render(world, cam.combined);

        gameInterface.stage.draw();
    }

    private void update(float delta) {
        handleInput(delta);
        handleSpawnItem();
        handleCheckDestroy();
        world.step(1/60f, 6, 2);

        player.update(delta);

        for (Asteroid st0: asteroids){
            st0.update(delta);
        }

        for (Item it0: items){
            it0.update(delta);
        }

        gameInterface.update(delta);

    }

    private void handleCheckDestroy(){
        if (!asteroids.isEmpty()){
            for (Iterator itr0 = asteroids.iterator(); itr0.hasNext();) {
                Asteroid as0 = (Asteroid) itr0.next();
                if (as0.isDestroy()){
                    itr0.remove();
                }
            }
        }
        if (!items.isEmpty()){
            for (Iterator itr0 = items.iterator(); itr0.hasNext();){
                Item it0 = (Item) itr0.next();
                if (it0.isDestroy()){
                    itr0.remove();
                }
            }
        }
    }

    private void handleSpawnItem() {
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Asteroid.class){
                asteroids.add(new Asteroid(this, idef.position.x, idef.position.y));
            }
            if(idef.type == Helmet.class){
                items.add(new Helmet(this, idef.position.x, idef.position.y));
            }
        }
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
        }

        if (player.isMoveLeft) {
            player.body.applyLinearImpulse(new Vector2(-player.moveSpeed, 0), player.body.getWorldCenter(), true);
        } else if (player.isMoveRight) {
            player.body.applyLinearImpulse(new Vector2(player.moveSpeed, 0), player.body.getWorldCenter(), true);
        }
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public World getWorld() {
        return world;
    }

    public GameInterface getGameInterface() {
        return gameInterface;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private float getRandomX() {
        return MathUtils.random(WIDTH * .25f / PPM, Gdx.graphics.getWidth() / PPM);
    }

    private float getRandomY() {
        return MathUtils.random(Gdx.graphics.getHeight() + 50, Gdx.graphics.getHeight() + 250) / PPM;
    }
}
