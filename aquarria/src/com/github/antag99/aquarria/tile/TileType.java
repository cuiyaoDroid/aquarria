package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;
import com.github.antag99.aquarria.ui.world.TileRenderer;

public class TileType extends AbstractType {
	public static Array<TileType> getTileTypes() {
		return AbstractType.getTypes(TileType.class);
	}

	public static TileType forName(String internalName) {
		return AbstractType.forName(TileType.class, internalName);
	}

	public static final TileType air = new TileType("tiles/air.json");
	public static final TileType dirt = new DropTileType("tiles/dirt.json");
	public static final TileType stone = new DropTileType("tiles/stone.json");
	public static final TileType grass = new DropTileType("tiles/grass.json");

	private String displayName;
	private boolean solid;
	private String skinPath;
	private FrameStyle style;
	private FrameSkin skin;
	private TileRenderer renderer;

	public TileType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public TileType(JsonValue properties) {
		super(properties.getString("internalName"));

		displayName = properties.getString("displayName", "");
		skinPath = properties.getString("skin", null);
		solid = properties.getBoolean("solid", true);
		style = FrameStyle.forName(properties.getString("style", "block"));
		renderer = TileRenderer.forName(properties.getString("renderer", "normal"));
	}

	public void destroyed(PlayerEntity player, int x, int y) {
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean isSolid() {
		return solid;
	}

	public FrameSkin getSkin() {
		return skin;
	}

	public FrameStyle getStyle() {
		return style;
	}

	public TileRenderer getRenderer() {
		return renderer;
	}

	@Override
	protected void queueAssets(AssetManager assetManager) {
		if (skinPath != null) {
			assetManager.load(skinPath, TextureAtlas.class);
		}
	}

	@Override
	protected void getAssets(AssetManager assetManager) {
		if (skinPath != null) {
			skin = new FrameSkin(assetManager.get(skinPath, TextureAtlas.class));
		}
	}

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return TileType.class;
	}
}
