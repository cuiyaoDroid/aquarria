/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.github.antag99.aquarria.world.World;

public abstract class Entity {
	private World world;
	private float x, y;
	/*
	 * TODO: make some distinction between raw velocity and resulting velocity,
	 * as this would be quite useful for slowing down/speeding up movement.
	 */
	private float velocityX, velocityY;
	private int directionX = 1, directionY = 1;
	private EntityView view;
	private boolean active = true;
	private Rectangle tmpRectangle = new Rectangle();
	private int health;

	public Entity() {
		health = getMaxHealth();
	}

	public void update(float delta) {
		boolean inWater = inWater();

		/* apply gravity to the velocity */
		velocityY = velocityY - (35f * getWeight()) * delta;

		/* move entity and detect collisions */
		float moveX = velocityX * delta * (inWater ? 0.5f : 1f);
		x += moveX;
		if (isSolid() && inCollision()) {
			x -= moveX;
			while (!inCollision())
				x += Math.signum(moveX) * 0.1f;
			x -= Math.signum(moveX) * 0.1f;
			velocityX = 0f;
		}

		float moveY = velocityY * delta * (inWater ? 0.5f : 1f);
		y += moveY;
		if (isSolid() && inCollision()) {
			if (velocityY < -50f) {
				int fallDamage = (int) (-velocityY * 2) - 100;
				setHealth(Math.max(getHealth() - fallDamage, 0));
			}

			y -= moveY;
			while (!inCollision())
				y += Math.signum(moveY) * 0.1f;
			y -= Math.signum(moveY) * 0.1f;
			velocityY = 0f;
		}

		directionX = velocityX > 0f ? 1 : velocityX < 0f ? -1 : directionX;
		directionY = velocityY > 0f ? 1 : velocityY < 0f ? -1 : directionY;
	}

	private Rectangle tmpBounds = new Rectangle();
	private Rectangle tmpBounds2 = new Rectangle();

	public boolean inCollision() {
		if (x < 0f || y < 0f || x + getWidth() > world.getWidth() ||
				y + getHeight() > world.getHeight()) {
			return true;
		}

		tmpBounds.set(x, y, getWidth(), getHeight());
		tmpBounds2.set(0f, 0f, 1f, 1f);

		int startX = MathUtils.floor(x);
		int startY = MathUtils.floor(y);

		int endX = MathUtils.ceil(x + getWidth());
		int endY = MathUtils.ceil(y + getHeight());

		for (int i = startX; i < endX; ++i) {
			tmpBounds2.x = i;
			for (int j = startY; j < endY; ++j) {
				tmpBounds2.y = j;

				if (world.getTileType(i, j).isSolid() && tmpBounds.overlaps(tmpBounds2)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean inWater() {
		int startX = MathUtils.floor(x);
		int startY = MathUtils.floor(y);

		int endX = MathUtils.ceil(x + getWidth());
		int endY = MathUtils.ceil(y + getHeight());

		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				if (world.getLiquid(i, j) >= 64) {
					return true;
				}
			}
		}

		return false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelocityX() {
		return velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	public int getDirectionX() {
		return directionX;
	}

	public int getDirectionY() {
		return directionY;
	}

	public void setDirectionX(int directionX) {
		this.directionX = directionX;
	}

	public void setDirectionY(int directionY) {
		this.directionY = directionY;
	}

	public float getWidth() {
		return 0f;
	}

	public float getHeight() {
		return 0f;
	}

	public Rectangle getBounds() {
		return tmpRectangle.set(x, y, getWidth(), getHeight());
	}

	public EntityView getView() {
		if (view == null)
			view = createView();
		return view;
	}

	public void setView(EntityView view) {
		this.view = view;
	}

	protected abstract EntityView createView();

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return 0;
	}

	public float getWeight() {
		return 1f;
	}

	public boolean isSolid() {
		return true;
	}
}
