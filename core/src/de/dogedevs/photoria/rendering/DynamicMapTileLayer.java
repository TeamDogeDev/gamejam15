package de.dogedevs.photoria.rendering;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.dogedevs.photoria.MainGame;
import de.dogedevs.photoria.generators.AbstractMapGenerator;
import de.dogedevs.photoria.generators.ChunkDebugMapGenerator;
import de.dogedevs.photoria.generators.PerlinMapGenerator;
import de.dogedevs.photoria.generators.VoronoiMapGenerator;
import de.dogedevs.photoria.rendering.tiles.Tile;

import java.util.HashMap;

/** @brief Layer for a TiledMap */
public class DynamicMapTileLayer extends TiledMapTileLayer {

	private AbstractMapGenerator generator;
	private int width;
	private int height;

	private float tileWidth;
	private float tileHeight;

//	public Chunk[][] chunks;
	public HashMap<String, Chunk> chunks;

	/** @return layer's width in tiles */
	public int getWidth () {
		return width;
	}

	/** @return layer's height in tiles */
	public int getHeight () {
		return height;
	}

	/** @return tiles' width in pixels */
	public float getTileWidth () {
		return tileWidth;
	}

	/** @return tiles' height in pixels */
	public float getTileHeight () {
		return tileHeight;
	}

	/** Creates TiledMap layer
	 * 
	 * @param tileWidth tile width in pixels
	 * @param tileHeight tile height in pixels */
	public DynamicMapTileLayer(AbstractMapGenerator generator, int tileWidth, int tileHeight) {
		super(1, 1, tileWidth, tileHeight);
		this.width = Integer.MAX_VALUE;
		this.height = Integer.MAX_VALUE;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.chunks = new HashMap<>();
		this.generator = generator;
	}

	/** @param x X coordinate
	 * @param y Y coordinate
	 * @return {@link Cell} at (x, y) */
	public Cell getCell (int x, int y) {
		Chunk chunk = chunks.get((x/64)+"_"+(y/64));
		if(chunk == null){
			MainGame.log("Generate chunk: " + (x / 64) + " " + (y / 64));
			chunk = new Chunk();
			chunk.x = x/64;
			chunk.y = y/64;
			chunks.put(chunk.getHashCode(), chunk);

			int[][] generatedMap = generator.generate(chunk.x, chunk.y, 64);

			for (int row = 0; row < 32 << 1; row++) {
				for (int col = 0; col < 32 << 1; col++) {
					TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
					if(generator instanceof ChunkDebugMapGenerator){
						if(generatedMap[row][col] == 1){
							cell.setTile(Tile.DEBUG);
						}
						chunk.setCell(cell, row, col);
						continue;
					}
					switch(generatedMap[row][col]) {
						case 0 : cell.setTile(Tile.HEIGHT0); break;
						case 1 : cell.setTile(Tile.HEIGHT1); break;
						case 2 : cell.setTile(Tile.HEIGHT2); break;
						case 3 : cell.setTile(Tile.HEIGHT3); break;
						case 4 : cell.setTile(Tile.HEIGHT4); break;
						case 5 : cell.setTile(Tile.HEIGHT5); break;
						case 6 : cell.setTile(Tile.HEIGHT6); break;
						case 7 : cell.setTile(Tile.HEIGHT7); break;
						case 8 : cell.setTile(Tile.HEIGHT8); break;
						case 9 : cell.setTile(Tile.HEIGHT9); break;
						case 10 : cell.setTile(Tile.HEIGHT10); break;
						case 11 : cell.setTile(Tile.HEIGHT11); break;
						case 12 : cell.setTile(Tile.HEIGHT12); break;
						default: cell.setTile(Tile.HEIGHT4);
					}
					chunk.setCell(cell, row, col);
				}
			}

		}
		return chunk.getCell(x, y);
	}

	/** Sets the {@link Cell} at the given coordinates.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param cell the {@link Cell} to set at the given coordinates. */
	public void setCell (int x, int y, Cell cell) {
//		if (x < 0 || x >= width) return;
//		if (y < 0 || y >= height) return;
//		cells[x][y] = cell;
	}

	public static class Chunk{
		public int x;
		public int y;

		Cell[][] cells;

		public Chunk(int x, int y, Cell[][] cells) {
			this.x = x;
			this.y = y;
			this.cells = cells;
		}

		public Chunk() {
			this.cells = new Cell[64][64];
		}

		public void setCell(Cell cell, int x, int y){
			cells[x][y] = cell;
		}

		public Cell getCell(int x, int y){
			return cells[x%64][y%64];
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Chunk chunkPos = (Chunk) o;

			if (x != chunkPos.x) return false;
			return y == chunkPos.y;

		}

		public String getHashCode(){
			return x+"_"+y;
		}
	}

}