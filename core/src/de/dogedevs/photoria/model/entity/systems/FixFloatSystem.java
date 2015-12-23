package de.dogedevs.photoria.model.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.dogedevs.photoria.model.entity.components.PositionComponent;
import de.dogedevs.photoria.model.map.OffsetHolder;

/**
 * Created by Furuha on 21.12.2015.
 */
public class FixFloatSystem extends EntitySystem {



    private final OrthographicCamera camera;
    private ImmutableArray<Entity> entities;

    public FixFloatSystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
    }

    @Override
    public void removedFromEngine (Engine engine) {

    }

    @Override
    public void update (float deltaTime) {
//        MainGame.log("update: "+entities.size());
        PositionComponent position;

        if(camera.position.x >= 5000 || camera.position.y >= 5000){
            for (int i = 0; i < entities.size(); ++i) {
                Entity e = entities.get(i);
                position = ComponentMappers.position.get(e);
                if(camera.position.x >= 5000 ){
                    position.x -= 2000;
                }
                if(camera.position.y >= 5000 ){
                    position.y -= 2000;
                }
            }
            if(camera.position.x >= 5000 ){
                OffsetHolder.offsetX -= 2000;
                camera.position.x -= 2000;
            }
            if(camera.position.y >= 5000 ){
                OffsetHolder.offsetY -= 2000;
                camera.position.y -= 2000;
            }
        }

        if(camera.position.x <= 1000 || camera.position.y <= 1000){
            for (int i = 0; i < entities.size(); ++i) {
                Entity e = entities.get(i);
                position = ComponentMappers.position.get(e);
                if(camera.position.x <= 1000 ){
                    position.x += 2000;
                }
                if(camera.position.y <= 1000 ){
                    position.y += 2000;
                }
            }
            if(camera.position.x <= 1000 ){
                OffsetHolder.offsetX += 2000;
                camera.position.x += 2000;
            }
            if(camera.position.y <= 5000 ){
                OffsetHolder.offsetY += 2000;
                camera.position.y += 2000;
            }
        }


    }
}
