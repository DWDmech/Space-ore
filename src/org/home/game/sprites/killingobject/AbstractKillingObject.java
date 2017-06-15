package org.home.game.sprites.killingobject;

        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.physics.box2d.Body;
        import com.badlogic.gdx.physics.box2d.World;

        import org.home.game.screen.GameScreen;
        import org.home.game.sprites.Martian;

public abstract class AbstractKillingObject extends Sprite {
    protected World world;
    protected GameScreen screen;
    public Body body;

    public AbstractKillingObject(GameScreen screen, float x, float y, Texture texture){
        super(texture);
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineObject();
        body.setActive(false);
    }

    protected abstract void defineObject();
    public abstract void killMartian(Martian martian);
    public abstract void update(float delta);
    public abstract boolean isDestroy();
}
