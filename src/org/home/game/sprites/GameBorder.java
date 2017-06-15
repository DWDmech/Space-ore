package org.home.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.home.game.screen.GameScreen;
import static org.home.game.utils.Constants.*;

public class GameBorder extends Sprite{
    public World world;
    private GameScreen screen;

    public GameBorder(GameScreen screen) {
        super(new Texture("img/bg.png"));
        setPosition(0, 0);
        setSize(WIDTH / PPM, HEIGHT / PPM);
        this.screen = screen;
        this.world = screen.getWorld();

        defineBottomBorder();
        defineLeftBorder();
        defineRightBorder();
    }

    private void defineBottomBorder() {
        float screenWidth = WIDTH,
              screenHeight = HEIGHT;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(screenWidth / 2 / PPM, screenHeight * 0.2f / PPM);
        Body body = world.createBody(bdef);

        //Bottom border
        ChainShape shape = new ChainShape();
        shape.createChain(new Vector2[]{new Vector2(-screenWidth, 0), new Vector2(screenWidth, 0)});
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = MAP_BIT;
        fdef.filter.maskBits = ASTEROID_BIT | MARTIAN_BIT | ITEM_BIT;
        body.createFixture(fdef).setUserData(this);
        shape.dispose();
    }

    private void defineLeftBorder() {
        float screenWidth = WIDTH,
                screenHeight = HEIGHT;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);
        Body body = world.createBody(bdef);

        ChainShape shape = new ChainShape();
        FixtureDef fdef = new FixtureDef();
        shape.createChain(new Vector2[]{new Vector2(0, -screenHeight), new Vector2(0, screenHeight)});
        fdef.shape = shape;
        fdef.filter.categoryBits = MAP_BIT;
        fdef.filter.maskBits = ASTEROID_BIT | MARTIAN_BIT | ITEM_BIT;
        body.createFixture(fdef);
    }

    private void defineRightBorder() {
        float screenWidth = WIDTH,
                screenHeight = HEIGHT;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(screenWidth / PPM, 0);
        Body body = world.createBody(bdef);

        ChainShape shape = new ChainShape();
        FixtureDef fdef = new FixtureDef();
        shape.createChain(new Vector2[]{new Vector2(0, -screenHeight), new Vector2(0, screenHeight)});
        fdef.shape = shape;
        fdef.filter.categoryBits = MAP_BIT;
        fdef.filter.maskBits = ASTEROID_BIT | MARTIAN_BIT | ITEM_BIT;
        body.createFixture(fdef);
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public void dispose() {
       getTexture().dispose();
    }
}
