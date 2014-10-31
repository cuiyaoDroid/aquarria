package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ItemType {
	private static Array<ItemType> itemTypes = new Array<ItemType>();
	
	public static Array<ItemType> getItemTypes() {
		return itemTypes;
	}
	
	public static final ItemType air = new ItemType("items/air.json");
	public static final ItemType dirt = new ItemType("items/dirt.json");
	public static final ItemType stone = new ItemType("items/stone.json");
	
	private String internalName;
	private String displayName;
	private int maxStack;
	private float width;
	private float height;
	private String texturePath;
	
	private TextureRegion texture;
	
	public ItemType(String path) {
		JsonValue properties = new JsonReader().parse(Gdx.files.internal(path));
		
		internalName = properties.getString("internalName");
		displayName = properties.getString("displayName");
		maxStack = properties.getInt("maxStack", 1);
		width = properties.getFloat("width", 0f);
		height = properties.getFloat("height", 0f);
		texturePath = properties.getString("texture", null);
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public int getMaxStack() {
		return maxStack;
	}
	
	public String getTexturePath() {
		return texturePath;
	}
	
	public void getTexture(AssetManager assetManager) {
		if(texturePath != null)
			texture = assetManager.get(texturePath);
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
}