package org.home.game.sprites.killingobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.home.game.screen.GameScreen;
import org.home.game.sprites.Martian;

import static org.home.game.utils.Constants.*;


public class Asteroid extends  AbstractKillingObject {
    private boolean setToDestroy;
    private boolean destroyed;

    public Asteroid(GameScreen screen, float x, float y) {
        super(screen, x, y, new Texture("img/asteroid.png"));
        setBounds(getX(), getY(), WIDTH * 0.075f / PPM, HEIGHT * 0.125f / PPM);
        setOrigin(getWidth() / 2 , getHeight() / 2);
        setToDestroy = false;
        destroyed = false;
        body.setActive(true);
    }

    @Override
    protected void defineObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(HEIGHT * .06f / PPM);
        fdef.friction = .25f;
        fdef.density = 2.5f;
        fdef.filter.categoryBits = ASTEROID_BIT;
        fdef.filter.maskBits = MAP_BIT | MARTIAN_BIT | MARTIAN_HEAD_BIT | TUBE_BIT | ASTEROID_BIT | MARTIAN_ITEM_BIT | NOTHING_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void killMartian(Martian martian) {
        if (!destroyed && !setToDestroy)
        martian.die();
    }

    @Override
    public void update(float delta) {
        if (setToDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        } else if (!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        }
    }

    @Override
    public boolean isDestroy() {
        return destroyed;
    }

    public void destroy() {
        setToDestroy = true;
    }

    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void dispose() {
        dispose();
    }
}
