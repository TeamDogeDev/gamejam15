package de.dogedevs.photoria.model.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.dogedevs.photoria.content.weapons.*;
import de.dogedevs.photoria.model.entity.ComponentMappers;
import de.dogedevs.photoria.model.entity.components.PlayerComponent;
import de.dogedevs.photoria.model.entity.components.PositionComponent;
import de.dogedevs.photoria.model.entity.components.TargetComponent;
import de.dogedevs.photoria.model.entity.components.VelocityComponent;
import de.dogedevs.photoria.model.entity.components.stats.EnergyComponent;
import de.dogedevs.photoria.rendering.overlay.GameOverlay;
import de.dogedevs.photoria.screens.GameScreen;
import de.dogedevs.photoria.utils.assets.MusicManager;
import de.dogedevs.photoria.utils.assets.enums.Musics;

import java.util.UUID;

/**
 * Created by Furuha on 21.12.2015.
 */
public class PlayerControllSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    public static final int SPEED = 128*4;

    public PlayerControllSystem() {
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void removedFromEngine (Engine engine) {

    }


    boolean wasActive = false;

    @Override
    public void update (float deltaTime) {
//        MainGame.log("update: "+entities.size());


        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            GameOverlay.addTextbox(UUID.randomUUID().toString() + " [#f0f00f]Yay22[]", 3);
            GameOverlay.addTextbox(UUID.randomUUID().toString() + " [#f0f00f]Yay22[]");
            GameOverlay.addTextbox(UUID.randomUUID().toString() + " [#f0f00f]Yay22[]", 1);
            GameOverlay.addTextbox(UUID.randomUUID().toString() + " [#f0f00f]Yay22[]", 1);
            GameOverlay.addTextbox(UUID.randomUUID().toString() + " [#f0f00f]Yay22[]");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)) {
            MusicManager.playMusic(Musics.TITLE, false);

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            MusicManager.playMusic(Musics.AMBIENT, false);
        }

        if (entities.size() == 0) {
            return;
        }
        final Entity e = entities.get(0);

        EnergyComponent energyComponent = ComponentMappers.energy.get(e);
        PositionComponent positionComponent = ComponentMappers.position.get(e);
        energyComponent.energy += 2*deltaTime;
        energyComponent.energy = MathUtils.clamp(energyComponent.energy, 0f, energyComponent.maxEnergy);

        if(!Gdx.input.isTouched()){
            if(wasActive){
                wasActive = false;
                e.remove(TargetComponent.class);
            }
        } else {
            TargetComponent target = ComponentMappers.target.get(e);
            if(target == null){
                target = GameScreen.getAshley().createComponent(TargetComponent.class);
                e.add(target);
            }
            target.x = Gdx.input.getX() - Gdx.graphics.getWidth() / 2 + positionComponent.x;
            target.y = (Gdx.graphics.getHeight() - Gdx.input.getY()) - Gdx.graphics.getHeight() / 2 +positionComponent.y;
            target.on = true;
            if(!wasActive){
                wasActive = true;
                AttackManager am = new AttackManager();
                Weapon weapon = new Laser();
                weapon.setRange(350);
                weapon.setColors(Color.RED, Color.YELLOW);
                am.createAttack(e, weapon);
                weapon = new Flamethrower();
                am.createAttack(e, weapon);
                weapon = new Shooter();
                am.createAttack(e, weapon);
            }
        }


        VelocityComponent velocity = ComponentMappers.velocity.get(e);
        velocity.speed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){

            if(energyComponent.energy >= 1) {
                energyComponent.energy--;
                energyComponent.energy = MathUtils.clamp(energyComponent.energy, 0f, energyComponent.maxEnergy);
                AttackManager am = new AttackManager();
                Vector2 dir = new Vector2();

                if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    dir.set(-1, 1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    dir.set(1, 1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    dir.set(1, -1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    dir.set(-1, -1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    dir.set(0, 1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    dir.set(-1, 0);
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    dir.set(0, -1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    dir.set(1, 0);
                } else if (Gdx.input.isTouched()) {
                    dir.set(Gdx.input.getX() - Gdx.graphics.getWidth() / 2 + positionComponent.x, (Gdx.graphics.getHeight() - Gdx.input.getY()) - Gdx.graphics.getHeight() / 2 +positionComponent.y);
                }
                //                am.shootNormal(e, dir, null);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.NORTH_WEST;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.NORTH_EAST;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.SOUTH_EAST;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.SOUTH_WEST;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.NORTH;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.WEST;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.SOUTH;
            return;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            camera.translate(0,32);
            velocity.speed = SPEED;
            velocity.direction = VelocityComponent.EAST;
            return;
        }


//
//        if( Gdx.input.isKeyPressed(Input.Keys.A)){
////            camera.translate(-32,0);
//            velocity.speed = SPEED;
//            velocity.direction = VelocityComponent.WEST;
//            return;
//        }
//        if( Gdx.input.isKeyPressed(Input.Keys.D)) {
////            camera.translate(32,0);
//            velocity.speed = SPEED;
//            velocity.direction = VelocityComponent.EAST;
//            return;
//        }
//        if( Gdx.input.isKeyPressed(Input.Keys.S)){
////            camera.translate(0,-32);
//            velocity.speed = SPEED;
//            velocity.direction = VelocityComponent.SOUTH;
//            return;
//        }

//        if( Gdx.input.isKeyPressed(Input.Keys.A))
//            camera.translate(-40*camera.zoom,0);
//        if( Gdx.input.isKeyPressed(Input.Keys.D))
//            camera.translate(40*camera.zoom,0);
//        if( Gdx.input.isKeyPressed(Input.Keys.S))
//            camera.translate(0,-40*camera.zoom);
//        if (Gdx.input.isKeyPressed(Input.Keys.W))
//            camera.translate(0,40*camera.zoom);
//        deltaSum += deltaTime;
//        if(deltaSum > 3){
//            deltaSum = 0;
//            entityNumber = MathUtils.random(entities.size()-1);
//        }


    }
}
