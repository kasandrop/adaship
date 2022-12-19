package com.ada.marcin.screen.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class ActorBase extends Actor {

    // -- attributes --
    private final Circle collisionShape = new Circle();
    private TextureRegion region;

    // -- constructor --
    public ActorBase() {
    }


    // -- public methods --
    public void setCollisionRadius(float radius) {
        collisionShape.setRadius(radius);
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (region == null) {
            return;
        }

        batch.draw(region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }

//    @Override
//    protected void drawDebugBounds(ShapeRenderer shapeRenderer) {
//        if(!getDebug()){
//            return;
//        }
//        Color oldColor = shapeRenderer.getColor().cpy();
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.x(collisionShape.x,collisionShape.y,0.1f);
//        shapeRenderer.circle(collisionShape.x,collisionShape.y,collisionShape.radius, 30);
//
//        shapeRenderer.setColor(oldColor);
//    }

    @Override
    protected void positionChanged() {
        updateCollisionShape();
    }

    @Override
    protected void sizeChanged() {
        updateCollisionShape();
    }

    // -- private methods --
    private void updateCollisionShape() {
        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;
        collisionShape.setPosition(getX() + halfWidth, getY() + halfHeight);
    }

    public Circle getCollisionShape() {
        return collisionShape;
    }
}
