package org.home.game.sprites.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import org.home.game.screen.GameScreen;
import org.home.game.sprites.Martian;
import org.home.game.utils.RadialSprite;

import static org.home.game.utils.Constants.*;

public class Helmet extends Item {
    private UIItemRender render;
    private boolean flag;

    public Helmet(GameScreen screen, float x, float y) {
        super(screen, x, y, 15, new Texture("img/helmet.png"));
        setSize(WIDTH * .0565f / PPM, HEIGHT * .0945f / PPM);
        body.setLinearVelocity(new Vector2(0, -5f));
        render = new UIItemRender(HEIGHT * .2f / PPM, HEIGHT * .2f  / PPM, getTexture());
        flag = false;
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH * .02f / PPM, HEIGHT * .0334f / PPM);
        fdef.filter.categoryBits = ITEM_BIT;
        fdef.filter.maskBits = MARTIAN_BIT | MAP_BIT | TUBE_BIT | ITEM_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Martian martian) {
        martian.pickUpItem(this);
        flag = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        if (render.currentTime <= timeOfLiveItem && flag)
            render.update(delta);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void drawOnHead(Batch batch, float x, float y, float width, float height){
        batch.draw(getTexture(), x, y, width, height);
        if (render.currentTime <= timeOfLiveItem && flag)
            render.draw(batch);
    }

    public boolean isLiveTime() {
        return render.currentTime >= timeOfLiveItem;
    }

    private class UIItemRender {
        public  float x, y, width, height, currentTime, angle;
        public  RadialSprite render;

        public  UIItemRender(float width, float height, Texture texture) {
            x = .1f;
            y = Gdx.graphics.getHeight() / PPM - height - .15f ;
            render = new RadialSprite(new TextureRegion(texture));
            this.width = width;
            this.height = height;
            angle = 360 / timeOfLiveItem;
        }

        public  void update(float delta) {
            currentTime += delta;
            render.setAngle(angle * currentTime);
        }

        public  void draw(Batch batch) {
            if (currentTime <= timeOfLiveItem)
                render.draw(batch, x, y, width, height);
        }
    }
}
