package org.home.game.tween;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class ActorAccessor implements TweenAccessor<Actor> {

    public static final int
            Y = 0,
            RGB = 1,
            ALPHA = 2;



    @Override
    public int getValues(Actor actor, int i, float[] floats) {
        switch (i){
            case Y:
                floats[0] = actor.getY();
                return 1;
            case RGB:
                floats[0] = actor.getColor().r;
                floats[1] = actor.getColor().g;
                floats[2] = actor.getColor().b;
                return 3;
            case ALPHA:
                floats[0] = actor.getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Actor actor, int i, float[] floats) {
        switch (i){
            case Y:
                actor.setY(floats[0]);
            case RGB:
                actor.setColor(floats[0], floats[1], floats[2], actor.getColor().a);
                break;
            case ALPHA:
                actor.setColor(actor.getColor().r, actor.getColor().g, actor.getColor().b, floats[0]);
                break;
            default:
                assert false;
        }
    }
}
