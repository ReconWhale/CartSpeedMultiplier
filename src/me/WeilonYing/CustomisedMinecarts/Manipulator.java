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
    private HashMap<Material, ManipulationOption> manipulatorBlocks;
    
    public Manipulator (JavaPlugin pl) {
        this.pl = pl;
        this.cf = pl.getConfig();
        this.manipulatorBlocks = new HashMap<Material, ManipulationOption>();
        initialiseManipulatorMaterials();
    }
    
    public void manipulateMinecart(Minecart cart) {
        Material blockMaterial = getMaterialUnderEntity (cart);
        ManipulationOption action = manipulatorBlocks.get (blockMaterial);
        
        switch (action) {
            case BOOST:
                if (cf.isDouble(Definitions.SETTING_BLOCK_BOOST_AMOUNT)) {
                    cart.setMaxSpeed(cf.getDouble(Definitions.SETTING_BLOCK_BOOST_AMOUNT));
                } else {
                    pl.getLogger().warning(Definitions.SETTING_BLOCK_BOOST_AMOUNT);
                    cart.setMaxSpeed(Definitions.DEFAULT_BLOCK_BOOST_SETTING);
                }
                break;
            case BRAKE:
                if (cf.isDouble(Definitions.SETTING_BLOCK_BRAKE_AMOUNT))
                break;
        }
    }
    
    private void initialiseManipulatorMaterials() {
        //Boost
        String boostMaterialName = cf.getString(Definitions.SETTING_BLOCK_BOOST_NAME);
        Material boostMaterial = Material.getMaterial(boostMaterialName);
        manipulatorBlocks.put(boostMaterial, ManipulationOption.BOOST);
        
        //Brake
        String brakeMaterialName = cf.getString(Definitions.SETTING_BLOCK_BRAKE_NAME);
        Material brakeMaterial = Material.getMaterial(brakeMaterialName);
        manipulatorBlocks.put(brakeMaterial, ManipulationOption.BRAKE);
    }
    
    
    public void setMinecartSlowWhenEmpty (Minecart cart) {
        try {
            cart.setSlowWhenEmpty(cf.getBoolean(Definitions.SETTING_SLOW_WHEN_EMPTY));
        } catch (Exception e) {
            reportParsingError (Definitions.SETTING_MAX_MINECART_SPEED);
            cart.setSlowWhenEmpty (true);
        }
    }    
    
    private Material getMaterialUnderEntity (Entity e) {
        Location entityLocation = e.getLocation();
        Location blockLocation = new Location (entityLocation.getWorld(), 
                entityLocation.getX(), entityLocation.getY() - 1, entityLocation.getZ());
        Block block = blockLocation.getBlock();
        Material blockMaterial = block.getType();
        
        return blockMaterial;
    }
    
    private void reportParsingError (String setting) {
        pl.getLogger().warning("Unable to parse setting " + setting + " in config.yml. Using default setting.");
    }
}
