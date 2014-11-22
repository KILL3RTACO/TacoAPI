package com.kill3rtaco.api.bukkit;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitCommandSender;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.schematic.SchematicFormat;

/**
 * A class to help with WorldEdit operations. <br/>
 * <br/>
 * Dependencies: WorldEdit (using this class if the required dependencies aren't
 * installed will cause issues)
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TWorldEdit {
	
	private static WorldEditPlugin	we;
	
	public static void init() {
		we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	}
	
	private static LocalConfiguration getLocalConfig() {
		return we.getWorldEdit().getConfiguration();
	}
	
	private static LocalSession getLocalSession() {
		return new LocalSession(getLocalConfig());
	}
	
	private static LocalWorld getLocalWorld(String world) {
		return new BukkitWorld(Bukkit.getServer().getWorld(world));
	}
	
	private static LocalWorld getLocalWorld(World world) {
		return new BukkitWorld(world);
	}
	
	/**
	 * Convert a Location to a Vector
	 * 
	 * @param location
	 *            The location to convert
	 * @return The converted Vector
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Vector toVector(Location location) {
		return new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	/**
	 * Pastes a schematic at a desired Location
	 * 
	 * @param world
	 *            - The world to paste the schematic
	 * @param schematic
	 *            - The schematic to paste
	 * @param location
	 *            - Where to paste the schematic
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void pasteSchematic(String world, String schematic, Location location) {
		try {
			pasteSchematicAtVector(world, schematic, toVector(location));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pastes a schematic at a desired Vector
	 * 
	 * @param world
	 *            - The world to paste in
	 * @param schematic
	 *            - The schematic to paste, the must be the full/relative path
	 *            to the file
	 * @param location
	 *            - Where to paste the schematic
	 * @throws DataException
	 *             If there was an error loading the schematic
	 * @throws IOException
	 *             If the file doesn't exist
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void pasteSchematicAtVector(String world, String schematic, Vector location) throws DataException, IOException {
		LocalSession session = getLocalSession();
		session.setBlockChangeLimit(-1);
		LocalWorld localWorld = getLocalWorld(world);
		Vector position = WorldVector.toBlockPoint(localWorld, location.getX(), location.getY(), location.getZ());
		
		File schema = new File(schematic);
		session.setClipboard(SchematicFormat.MCEDIT.load(schema));
		
		BlockBag blocks = null;
		
		EditSession es = new EditSession(localWorld, session.getBlockChangeLimit(), blocks);
		es.setFastMode(session.hasFastMode());
		es.setMask(session.getMask());
		try {
			session.getClipboard().paste(es, position, false);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		} catch (EmptyClipboardException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a BaseBlock from its ID
	 * 
	 * @param id
	 *            the id of the block; i.e. "red" for red wool
	 * @param world
	 *            the name of the world
	 * @return the BaseBlock
	 * @throws WorldEditException
	 *             if a WorldEditException occurred
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static BaseBlock getBlock(String id, String world) throws WorldEditException {
		return getBlock(id, getLocalWorld(world));
	}
	
	/**
	 * Get a BaseBlock from its ID
	 * 
	 * @param id
	 *            the id of the block; i.e. "red" for red wool
	 * @param world
	 *            the world
	 * @return the BaseBlock
	 * @throws WorldEditException
	 *             if a WorldEditException occurred
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static BaseBlock getBlock(String id, final LocalWorld world) throws WorldEditException {
		if (world == null)
			throw new IllegalArgumentException("world cannot be null");
		return we.getWorldEdit().getBlock(new BukkitCommandSender(we, we.getServerInterface(), Bukkit.getConsoleSender()) {
			
			@Override
			public LocalWorld getWorld() {
				return world;
			}
			
		}, id, true);
	}
	
	/**
	 * Get a player's WorldEdit Selection
	 * 
	 * @param player
	 *            the player
	 * @return the player's selection
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Selection getSelection(Player player) {
		return we.getSelection(player);
	}
	
	/**
	 * Save a player's clipboard as a schematic
	 * 
	 * @param player
	 *            the player
	 * @param f
	 *            the file to save to
	 * @throws EmptyClipboardException
	 *             if the player's clipboard is empty
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws DataException
	 *             if the schematic cannot be written to the file
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void savePlayerClipboardAsSchematic(Player player, File f) throws EmptyClipboardException, IOException, DataException {
		if (f == null) {
			throw new IllegalArgumentException("File cannot be null");
		} else if (f.exists()) {
			throw new IllegalArgumentException("File " + f.getAbsolutePath() + " already exists");
		}
		SchematicFormat format = SchematicFormat.getFormat("mce"); //MCEdit
		format.save(we.getSession(player).getClipboard(), f);
	}
	
	/**
	 * Save a player's selection as a schematic
	 * 
	 * @param player
	 *            the player
	 * @param f
	 *            the file to write to
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws DataException
	 *             if the schematic cannot be written to the file
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void savePlayerSelectionAsSchematic(Player player, File f) throws IncompleteRegionException, IOException, DataException {
		LocalSession session = we.getSession(player);
		EditSession editSession = new EditSession(getLocalWorld(player.getWorld()), -1);
		Region region = session.getSelection(getLocalWorld(player.getWorld()));
		Vector min = region.getMinimumPoint();
		Vector max = region.getMaximumPoint();
		Vector pos = session.getPlacementPosition(we.wrapPlayer(player));
		
		CuboidClipboard clipboard = new CuboidClipboard(
				max.subtract(min).add(Vector.ONE),
				min, min.subtract(pos));
		
		clipboard.copy(editSession);
		SchematicFormat format = SchematicFormat.getFormat("mce"); //MCEdit
		format.save(clipboard, f);
	}
	
	/**
	 * Set an area with blocks (//set). Note that this method does not set a
	 * limit on how many blocks can be changed, so use wisely/safely
	 * 
	 * @param world
	 *            the world
	 * @param minPoint
	 *            the minimum point
	 * @param maxPoint
	 *            the maximum point
	 * @param blockId
	 *            the block' id
	 * @return the amount of blocks affected
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static int setAreaWithBlock(String world, Location minPoint, Location maxPoint, String blockId, int maxBlocks) {
		LocalSession session = getLocalSession();
		RegionSelector selector = session.getRegionSelector(getLocalWorld(world));
		selector.selectPrimary(toVector(minPoint));
		selector.selectSecondary(toVector(maxPoint));
		EditSession editSession = new EditSession(getLocalWorld(world), maxBlocks);
		Pattern pattern = null;
		try {
			pattern = new SingleBlockPattern(getBlock(blockId, editSession.getWorld()));
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		int affected = -1;
		
		if (pattern instanceof SingleBlockPattern) {
			try {
				affected = editSession.setBlocks(selector.getRegion(), ((SingleBlockPattern) pattern).getBlock());
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
			} catch (IncompleteRegionException e) {
				e.printStackTrace();
			}
		} else {
			try {
				affected = editSession.setBlocks(selector.getRegion(), pattern);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return affected;
	}
}
