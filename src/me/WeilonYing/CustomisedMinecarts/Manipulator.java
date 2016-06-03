package me.WeilonYing.CustomisedMinecarts;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
/**
 * Manipulator class does the world manipulation for this plugin, such as adjusting parameters
 * for minecarts.
 * @author Weilon Ying
 *
 */
public class Manipulator {
    private JavaPlugin pl;
    private FileConfiguration cf;
    private HashMap<String, ManipulationOption> manipulatorBlocks;
    
    public Manipulator (JavaPlugin pl) {
        this.pl = pl;
        this.cf = pl.getConfig();
        this.manipulatorBlocks = new HashMap<String, ManipulationOption>();
        initialiseManipulatorBlocks();
    }
    
    /**
     * Handles all redstone related things
     * @param poweredBlock
     */
    public void redstoneHandler (Block poweredBlock) {
        String blockName = poweredBlock.getType().name();
        
        @SuppressWarnings("deprecation") //As of Minecraft 1.9.4, there is currently no alternative
        Byte blockData = poweredBlock.getData(); 
        String blockDataStr = Integer.toString(Byte.toUnsignedInt(blockData)); //convert block data integer as string

        ManipulationOption action = manipulatorBlocks.get (blockName + blockDataStr); //get action depending on what block it is
        
        if (action != null) {
            switch (action) {
                case LAUNCHER: //if launcher block
                    List<Minecart> minecartList = checkMinecartAboveBlock (poweredBlock); //check for minecarts
                    if (minecartList != null) {
                        for (Minecart cart : minecartList) {
                            if (isOnPoweredRail (cart)) { //if minecart on powered rail, launch it!
                                //We don't need to check if rail is powered, as we know the block underneath it is powered.
                                launchMinecart (poweredBlock, cart);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }    
        } 
    }
    
    /**
     * Launches minecart in a specific direction if minecart isn't already moving
     * @param poweredBlock
     * @param cart
     */
    private void launchMinecart(Block poweredBlock, Minecart cart) {
        //determine if minecart is already moving
        Vector cartVelocity = cart.getVelocity();
        if (isZeroVector (cartVelocity)) { //we only manipulate the minecart if it's not moving
            //TO DO: Push cart in a given direction, based on what a sign says
        }
    }
    
    /**
     * Checks if a vector is a zero vector
     * @param v The vector to be checked
     * @return True if the vector's components are 0, true if the vector is null, and false otherwise
     */
    private boolean isZeroVector(Vector v) {
        if (v == null) return true;
        if (v.getX() == 0.0D && v.getY() == 0.0D && v.getZ() == 0.0D) {
            return true;
        }
        return false;
    }

    /**
     * Checks for minecarts above a given block
     * @param b The block to be checked
     * @return A list of minecarts if one or more exists, and null otherwise
     */
    private List<Minecart> checkMinecartAboveBlock (Block b) {
        List<Minecart> minecartList = new LinkedList<Minecart>();
        Location l = b.getLocation(); //powered block's location
        l.setY(l.getY() + 1); //set the location to the block above the powered block
        
        //Spawn armor stand as a placeholder entity to find nearby entities
        ArmorStand as = l.getWorld().spawn(l, ArmorStand.class);
        as.setVisible(false);
        Collection<Entity> entitiesAboveBlock = as.getNearbyEntities(0.5, 0.5, 0.5);
        as.remove();
        as = null; //not necessary, but ensures that an attempt to reuse armor stand is prevented now that it's removed
        
        for (Entity entity : entitiesAboveBlock) { //loop through nearby entites above the block
            if (entity instanceof Minecart) {
                minecartList.add((Minecart) entity);
            }
        }
        
        if (minecartList.size() > 0) {
            return minecartList;
        }
        return null;
    }
    
    /**
     * Checks if an entity on on a powered rail
     * @param e The entity to be checked
     * @return True if entity is standing/on a powered rail, and false otherwise.
     */
    private boolean isOnPoweredRail (Entity e) {
        Location l = e.getLocation();
        
        if (l.getBlock().getType() == Material.POWERED_RAIL) {
            return true;
        }
        
        return false;
    }
    
    /**
     * manipulateMinecart will manipulate a specific minecart's properties depending
     * on the block it has travelled over.
     * @param cart The minecart being manipulated
     */
    public void manipulateMinecart(Minecart cart) {
        Block blockUnderCart = getBlockUnderEntity (cart); //gets the block under the cart
        String blockName = blockUnderCart.getType().name(); //gets block name
        
        @SuppressWarnings("deprecation") //there is currently no alternative as of Minecraft 1.9.4
        Byte blockData = blockUnderCart.getData(); //block data, e.g. if data = 1 and block = LOG, this is a spruce log
        String blockDataStr = Integer.toString(Byte.toUnsignedInt(blockData)); //convert block data integer as string
        
        ManipulationOption action = manipulatorBlocks.get (blockName + blockDataStr); //get action depending on what block it is
        
        if (!(action == null)) { //Only switch if an action exists for the block under the minecart right now
            switch (action) {
                case FAST: //If block is associated with high speed rail
                    double boostMulti = Definitions.DEFAULT_BLOCK_FAST_SETTING;
                    if (cf.isDouble(Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_AMOUNT)) {
                        boostMulti = cf.getDouble(Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_AMOUNT); 
                    } else {
                        reportParsingError(Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_AMOUNT);
                    }
                    cart.setMaxSpeed(boostMulti * Definitions.DEFAULT_MAX_MINECART_SPEED_SETTING);
                    
                    break;
                case SLOW: //If block is associated with slow rail
                    double brakeMulti = Definitions.DEFAULT_BLOCK_SLOW_SETTING;
                    if (cf.isDouble(Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_AMOUNT)) {
                        brakeMulti = cf.getDouble(Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_AMOUNT);
                    } else {
                        reportParsingError(Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_AMOUNT);
                    }
                    cart.setMaxSpeed(brakeMulti * Definitions.DEFAULT_MAX_MINECART_SPEED_SETTING);
                    break;
                case NORMAL: //If block is associated with normal speed rail
                    cart.setMaxSpeed(Definitions.DEFAULT_MAX_MINECART_SPEED_SETTING);
                    break;
                default:
                    break;
            }
        }
        
    }
    
    /**
     * Initialises a hashmap of all blocks that will manipulate the minecart
     */
    private void initialiseManipulatorBlocks() {
        //Fast (high speed) rail
        String boostMaterialName = cf.getString(Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_NAME); //material name
        String boostMaterialData = getValidatedData(cf.getString(Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_DATA),
                Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_DATA); //data value of the material
        if (!isValidMaterial (boostMaterialName)) { // use default settings if material name isn't valid
            reportParsingError (Definitions.SETTING_BLOCK_FAST + Definitions.SETTING_NAME);
            boostMaterialName = Definitions.DEFAULT_BLOCK_FAST_NAME;
        }
        //concatenate the data value to the name, then add to the hashmap
        manipulatorBlocks.put(boostMaterialName + boostMaterialData, ManipulationOption.FAST);
        
        //Slow rail
        String brakeMaterialName = cf.getString(Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_NAME);
        String brakeMaterialData = getValidatedData (cf.getString(Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_DATA),
                Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_DATA);
        if (!isValidMaterial (brakeMaterialName)) {
            reportParsingError (Definitions.SETTING_BLOCK_SLOW + Definitions.SETTING_NAME);
            brakeMaterialName = Definitions.DEFAULT_BLOCK_SLOW_NAME;
        }
        manipulatorBlocks.put(brakeMaterialName + brakeMaterialData, ManipulationOption.SLOW);
        
        //Normal speed
        String normalMaterialName = cf.getString(Definitions.SETTING_BLOCK_NORMAL + Definitions.SETTING_NAME);
        String normalMaterialData = getValidatedData (cf.getString(Definitions.SETTING_BLOCK_NORMAL + Definitions.SETTING_DATA),
                Definitions.SETTING_BLOCK_NORMAL + Definitions.SETTING_DATA);
        if (!isValidMaterial (normalMaterialName)) {
            reportParsingError(Definitions.SETTING_BLOCK_NORMAL + Definitions.SETTING_NAME);
            normalMaterialName = Definitions.DEFAULT_BLOCK_NORMAL_NAME;
        }
        manipulatorBlocks.put(normalMaterialName + normalMaterialData, ManipulationOption.NORMAL);
        
        //Launcher block
        String launchMaterialName = cf.getString(Definitions.SETTING_BLOCK_LAUNCHER + Definitions.SETTING_NAME);
        String launchMaterialData = getValidatedData (cf.getString(Definitions.SETTING_BLOCK_LAUNCHER + Definitions.SETTING_DATA),
                Definitions.SETTING_BLOCK_LAUNCHER + Definitions.SETTING_DATA);
        if (!isValidMaterial (launchMaterialName)) {
            reportParsingError (Definitions.SETTING_BLOCK_LAUNCHER + Definitions.SETTING_NAME);
            launchMaterialName = Definitions.DEFAULT_BLOCK_LAUNCH_NAME;
        }
        manipulatorBlocks.put(launchMaterialName + launchMaterialData, ManipulationOption.LAUNCHER);
        
    }
    
    /**
     * Sets a specific minecart's slow when empty option
     * @param cart The minecart being affected
     * @param setting The boolean setting
     */
    public void setMinecartSlowWhenEmpty (Minecart cart, boolean setting) {
        try {
            cart.setSlowWhenEmpty(setting);
        } catch (Exception e) {
            reportParsingError (Definitions.SETTING_MAX_MINECART_SPEED);
            cart.setSlowWhenEmpty (Definitions.DEFAULT_SLOW_WHEN_EMPTY);
        }
    }    
    
    /**
     * Retrieves the block under a specific entity
     * @param e The entity being examined
     * @return The block under the entity
     */
    private Block getBlockUnderEntity (Entity e) {
        Location entityLocation = e.getLocation();
        Location blockLocation = new Location (entityLocation.getWorld(), 
                entityLocation.getX(), entityLocation.getY() - 1, entityLocation.getZ());
        Block block = blockLocation.getBlock();
        return block;
    }
    
    /**
     * Checks whether a material name is a valid material in the Material enum.
     * @param materialName The material's name being check
     * @return True if the material name is valid, and false otherwise
     */
    private boolean isValidMaterial (String materialName) {
        try {
            if (Material.matchMaterial(materialName) != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Validates a data value for a material.
     * To do this, it checks whether the String data is an integer
     * @param data The data value being checked.
     * @param setting The setting it is under. This is used for error reporting purposes
     * @return The data as a string if it's a valid integerm and "0" otherwise
     */
    private String getValidatedData (String data, String setting) {
        try {
            Integer.parseInt(data);
            return data;
        } catch (NumberFormatException e) {
            reportParsingError (setting);
            return "0";
        }
    }
    
    /**
     * Helper method for reporting errors from parsing the config file.
     * @param setting The setting the error is involved in
     */
    private void reportParsingError (String setting) {
        pl.getLogger().warning("Unable to parse setting " + setting + " in config.yml. Using default setting.");
    }
}
