package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;

public class HammerItemType extends ItemType {
	public HammerItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public HammerItemType(JsonValue properties) {
		super(properties);
	}

	@Override
	public boolean useItem(PlayerEntity player, Item item) {
		Vector2 worldFocus = player.getWorldFocus();

		int tileX = MathUtils.floor(worldFocus.x);
		int tileY = MathUtils.floor(worldFocus.y);

		return player.destroyWall(tileX, tileY);
	}

	@Override
	public boolean canUseItem(PlayerEntity player, Item item) {
		return true;
	}
}
