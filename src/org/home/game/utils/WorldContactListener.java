package org.home.game.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.home.game.screen.GameScreen;
import org.home.game.screen.MainMenuScreen;
import org.home.game.sprites.Martian;
import org.home.game.sprites.items.Item;
import org.home.game.sprites.killingobject.Asteroid;

import static org.home.game.utils.Constants.*;


public class WorldContactListener implements ContactListener {
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int cDef = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;

        switch (cDef) {
            case MARTIAN_BIT | ITEM_BIT:
                if (fA.getFilterData().categoryBits == ITEM_BIT){
                    ((Item) fA.getUserData()).use((Martian) fB.getUserData());
                    ((Item) fA.getUserData()).destroy();
                } else {
                    ((Item) fB.getUserData()).use((Martian) fA.getUserData());
                    ((Item) fB.getUserData()).destroy();
                }
                break;
            case ASTEROID_BIT | TUBE_BIT:
                if (fA.getFilterData().categoryBits == ASTEROID_BIT) {
                    AudioManager.getInstance().playTubeSound();
                    ((Asteroid)fA.getUserData()).destroy();
                    GameInterface.addScore(3);
                } else {
                    AudioManager.getInstance().playTubeSound();
                    ((Asteroid)fB.getUserData()).destroy();
                    GameInterface.addScore(3);
                }
                break;
            case ASTEROID_BIT | MARTIAN_HEAD_BIT :
                if (fA.getFilterData().categoryBits == MARTIAN_HEAD_BIT){
                   if(maxScoreCount < GameInterface.getScoreCount()) {
                       maxScoreCount = GameInterface.getScoreCount();
                   }
                    AudioManager.getInstance().playDieSound();
                    ((Martian)fA.getUserData()).die();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                    GameInterface.reset();
                } else {
                    if(maxScoreCount < GameInterface.getScoreCount()) {
                        maxScoreCount = GameInterface.getScoreCount();
                    }
                    AudioManager.getInstance().playDieSound();
                    ((Martian)fB.getUserData()).die();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                    GameInterface.reset();
                }
                break;
            case ASTEROID_BIT | MARTIAN_ITEM_BIT :
                if (fA.getFilterData().categoryBits == MARTIAN_ITEM_BIT){
                    ((Martian)fA.getUserData()).defence();
                    ((Asteroid)fB.getUserData()).destroy();
                } else {
                    ((Martian)fB.getUserData()).defence();
                    ((Asteroid)fA.getUserData()).destroy();
                }
                break;
        }
    }

    public void endContact(Contact contact) {

    }

    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
