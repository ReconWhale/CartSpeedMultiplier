package me.WeilonYing.CustomisedMinecarts;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.java.JavaPlugin;

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
    
    public void manipulateMinecart(Minecart cart) {
        Block blockUnderCart = getBlockUnderCart (cart);
        String blockName = blockUnderCart.getType().name();
        Byte blockData = blockUnderCart.getData();
        String blockDataStr = Integer.toString(Byte.toUnsignedInt(blockData));
        
        ManipulationOption action = manipulatorBlocks.get (blockName + blockDataStr);
        if (action == null) {
            //pl.getLogger().info("ACTION IS NULL PANIC SLDFJALSADKFHASKF");
        } else {
            switch (action) {
            case BOOST:
                double boostMulti = Definitions.DEFAULT_BLOCK_BOOST_SETTING;
                if (cf.isDouble(Definitions.SETTING_BLOCK_BOOST_AMOUNT)) {
                    boostMulti = cf.getDouble(Definitions.SETTING_BLOCK_BOOST_AMOUNT); 
                } else {
                    reportParsingError(Definitions.SETTING_BLOCK_BOOST_AMOUNT);
                }
                cart.setMaxSpeed(boostMulti * Definitions.DEFAULT_MAX_MINECART_SPEED_SETTING);
                
                break;
            case BRAKE:
                double brakeMulti = Definitions.DEFAULT_BLOCK_BRAKE_SETTING;
                if (cf.isDouble(Definitions.SETTING_BLOCK_BRAKE_AMOUNT)) {
                    brakeMulti = cf.getDouble(Definitions.SETTING_BLOCK_BRAKE_AMOUNT);
                } else {
                    reportParsingError(Definitions.SETTING_BLOCK_BRAKE_AMOUNT);
                }
                cart.setMaxSpeed(brakeMulti * Definitions.DEFAULT_MAX_MINECART_SPEED_SETTING);
                break;
            }
        }
        
    }

    private void initialiseManipulatorBlocks() {
        //Boost
        String boostMaterialName = cf.getString(Definitions.SETTING_BLOCK_BOOST_NAME); //material name
        if (!isValidMaterial (boostMaterialName)) {
            reportParsingError (Definitions.SETTING_BLOCK_BOOST_NAME);
            boostMaterialName = Definitions.DEFAULT_BLOCK_BOOST_NAME;
        }
        String boostMaterialData = validateData(cf.getString(Definitions.SETTING_BLOCK_BOOST_DATA),
                Definitions.SETTING_BLOCK_BOOST_DATA);
         //concatenate the data value to the name;
        manipulatorBlocks.put(boostMaterialName + boostMaterialData, ManipulationOption.BOOST);
        
        //Brake
        String brakeMaterialName = cf.getString(Definitions.SETTING_BLOCK_BRAKE_NAME);
        
        if (!isValidMaterial (brakeMaterialName)) {
            reportParsingError (Definitions.SETTING_BLOCK_BRAKE_NAME);
            brakeMaterialName = Definitions.DEFAULT_BLOCK_BRAKE_NAME;
        }
        String brakeMaterialData = validateData (cf.getString(Definitions.SETTING_BLOCK_BRAKE_DATA),
                Definitions.SETTING_BLOCK_BRAKE_DATA);
        
        manipulatorBlocks.put(brakeMaterialName + brakeMaterialData, ManipulationOption.BRAKE);
    }
    
    
    public void setMinecartSlowWhenEmpty (Minecart cart) {
        try {
            cart.setSlowWhenEmpty(cf.getBoolean(Definitions.SETTING_SLOW_WHEN_EMPTY));
        } catch (Exception e) {
            reportParsingError (Definitions.SETTING_MAX_MINECART_SPEED);
            cart.setSlowWhenEmpty (true);
        }
    }    
    
    private Block getBlockUnderCart (Entity e) {
        Location entityLocation = e.getLocation();
        Location blockLocation = new Location (entityLocation.getWorld(), 
                entityLocation.getX(), entityLocation.getY() - 1, entityLocation.getZ());
        Block block = blockLocation.getBlock();
        return block;
    }
    
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
    
    private String validateData (String data, String setting) {
        try {
            Integer.parseInt(data);
            return data;
        } catch (NumberFormatException e) {
            reportParsingError (setting);
            return "0";
        }
    }
    
    private void reportParsingError (String setting) {
        pl.getLogger().warning("Unable to parse setting " + setting + " in config.yml. Using default setting.");
    }
}
