package org.home.game.sprites.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import org.home.game.screen.GameScreen;
import org.home.game.sprites.Martian;

import static org.home.game.utils.Constants.*;

public abstract class Item extends Sprite {
    protected GameScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected float timeOfLiveItem;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(GameScreen screen, float x, float y, float timeOfLiveItem, Texture texture) {
        super(texture);
        this.screen = screen;
        this.world = screen.getWorld();
        this.timeOfLiveItem = timeOfLiveItem;
        toDestroy = false;
        destroyed = false;

        setPosition(x, y);
        defineItem();
    }

    public abstract void defineItem();
    public abstract void use(Martian martian);

    public void update(float delta) {
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    public boolean isDestroy(){
        return destroyed;
    }

    public void destroy(){
        toDestroy = true;
    }

    public void dispose() {
        super.getTexture().dispose();
    }

}
