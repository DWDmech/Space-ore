package org.home.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.home.game.screen.GameScreen;
import static org.home.game.utils.Constants.*;

public class Tube extends Sprite {
    private Body bodyTube;
    private World world;
    private GameScreen screen;

    public Tube(GameScreen screen) {
        super(new Texture("img/tube.png"));
        setSize(WIDTH * .19f / PPM, HEIGHT * .63f / PPM);
        setPosition(WIDTH * .02f / PPM, -HEIGHT * .290f / PPM);
        world = screen.getWorld();
        this.screen = screen;
        defineTube(world,
                WIDTH * .125f / PPM,
                HEIGHT * .260f / PPM,
                0f,
                HEIGHT * .1f / PPM);
    }

    private void defineTube(World world, float x, float y, float width, float height){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x,y);
        bodyTube = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(new Vector2[]{new Vector2(width, -height), new Vector2(width, height)});
        fdef.shape = chainShape;
        fdef.filter.categoryBits = TUBE_BIT;
        fdef.filter.maskBits = ASTEROID_BIT | MARTIAN_BIT | ITEM_BIT;

        bodyTube.createFixture(fdef).setUserData(this);

        ChainShape chainShapeTop = new ChainShape();
        chainShapeTop.createChain(new Vector2[]{new Vector2(-WIDTH * .635f / PPM, height), new Vector2(WIDTH * .01f / PPM, height)});
        fdef.shape = chainShapeTop;
        fdef.filter.categoryBits = NOTHING_BIT;
        fdef.filter.maskBits = ASTEROID_BIT | MARTIAN_BIT | ITEM_BIT;

        //new Vector2(-WIDTH * .635f / PPM, height)

        bodyTube.createFixture(fdef).setUserData(this);
        chainShape.dispose();
    }

    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void update(float delta) {

    }
}
