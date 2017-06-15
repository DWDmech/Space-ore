package org.home.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.home.game.screen.GameScreen;
import org.home.game.sprites.items.Helmet;
import org.home.game.sprites.items.Item;
import org.home.game.utils.AudioManager;

import static org.home.game.utils.Constants.*;

public class Martian extends Sprite{
    public enum State {DEAD, FALLING, JUMPING, RUNNING, STANDING}
    public State currentState;
    public State previousState;

    public World world;
    public Body body;

    private float stateTimer;
    private boolean timeToRedefineMartian;
    private boolean timeToDefineMartianWithHelmet;
    private boolean martianIsDead;
    private GameScreen screen;
    private Item item;

    public final float jumpSpeed, moveSpeed;
    public boolean isMoveRight, isMoveLeft;


    public Martian(GameScreen screen) {
        super(new Texture("img/player.png"));
        setSize(WIDTH * .125f / PPM, HEIGHT * .215f / PPM);
        setOrigin(getWidth() / 2, getHeight() / 2);
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        jumpSpeed = HEIGHT * .85f / PPM;
        moveSpeed = WIDTH * .0285f / PPM ;

        defineMartian();
    }

    public void draw(Batch batch) {
        super.draw(batch);
        if(item != null && item instanceof Helmet){
            ((Helmet)item).drawOnHead(batch, getX() + WIDTH * .125f / 3.5f / PPM, getY() + HEIGHT * .215f / 1.25f / PPM, item.getWidth(), item.getHeight());
        }
    }

    public void update(float delta) {
        if (isDead()) {
            die();
        }
        currentState = getState();
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        if (timeToRedefineMartian){
            redefineMartian();
        }
        if (timeToDefineMartianWithHelmet){
            defineMartianWithHelmet();
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;

        if (item != null) {
            item.update(delta);
            item.setPosition(body.getFixtureList().get(1).getBody().getPosition().x - item.getWidth() / 2, body.getFixtureList().get(1).getBody().getPosition().y - item.getHeight() / 2);
        }


        if (item != null && ((Helmet)item).isLiveTime()){
            timeToRedefineMartian = true;
            item = null;
        }
    }

    private void defineMartian() {
        BodyDef bdef = new BodyDef();
        bdef.position.set( WIDTH * .05f / PPM, HEIGHT / 2 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        //body
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH * .04f / PPM, HEIGHT * .075f / PPM);
        fDef.friction = .75f;
        fDef.filter.categoryBits = MARTIAN_BIT;
        fDef.filter.maskBits = ASTEROID_BIT |
                               TUBE_BIT |
                               MAP_BIT |
                               ITEM_BIT |
                               NOTHING_BIT;
        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);

        //head
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(WIDTH * 0.025f / PPM, HEIGHT * 0.01f / PPM, new Vector2(0, HEIGHT * .075f / PPM), 0);

        fDef.shape = polygonShape;
        fDef.filter.categoryBits = MARTIAN_HEAD_BIT;
        fDef.filter.maskBits = ASTEROID_BIT | ITEM_BIT ;
        body.createFixture(fDef).setUserData(this);
    }

    private void defineMartianWithHelmet() {
        Vector2 position = body.getPosition();
        Vector2 velocity = body.getLinearVelocity();
        world.destroyBody(body);

        //body
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.linearVelocity.set(velocity);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH * .04f / PPM, HEIGHT * .075f / PPM);
        fDef.friction = .75f;
        fDef.filter.categoryBits = MARTIAN_BIT;
        fDef.filter.maskBits = ASTEROID_BIT |
                TUBE_BIT |
                MAP_BIT |
                ITEM_BIT |
                NOTHING_BIT;
        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);

        //helmet
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(WIDTH * 0.025f / PPM, HEIGHT * 0.01f / PPM, new Vector2(0, HEIGHT * .075f / PPM), 0);
        fDef.shape = polygonShape;
        fDef.filter.categoryBits = MARTIAN_ITEM_BIT;
        fDef.filter.maskBits = ASTEROID_BIT;
        body.createFixture(fDef).setUserData(this);

        timeToDefineMartianWithHelmet = false;
    }

    private void redefineMartian() {
        Vector2 position = body.getPosition();
        world.destroyBody(body);

        //body
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH * .04f / PPM, HEIGHT * .075f / PPM);
        fDef.friction = .75f;
        fDef.filter.categoryBits = MARTIAN_BIT;
        fDef.filter.maskBits = ASTEROID_BIT |
                TUBE_BIT |
                MAP_BIT |
                ITEM_BIT |
                NOTHING_BIT;
        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);

        //head
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(WIDTH * 0.025f / PPM, HEIGHT * 0.01f / PPM, new Vector2(0, HEIGHT * .075f / PPM), 0);
        fDef.shape = polygonShape;
        fDef.filter.categoryBits = MARTIAN_HEAD_BIT;
        fDef.filter.maskBits = ASTEROID_BIT | ITEM_BIT;
        body.createFixture(fDef).setUserData(this);

        timeToRedefineMartian = false;
    }

    public State getState() {
        if (martianIsDead)
            return State.DEAD;
        else if ((body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (body.getLinearVelocity().x < 0)
            return State.FALLING;
        else if (body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void jump() {
        if(currentState != State.JUMPING) {
            body.applyLinearImpulse(new Vector2(0, jumpSpeed), body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void die() {
        if (!isDead()) {
            AudioManager.getInstance().playDieSound();
        }
    }

    public boolean isDead() {
        return martianIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void pickUpItem(Item item) {
        this.item = item;

        timeToDefineMartianWithHelmet = true;

    }

    public void defence(){
        timeToRedefineMartian = true;
        item = null;
    }

    public void dispose() {
        dispose();
        item.dispose();
    }
}