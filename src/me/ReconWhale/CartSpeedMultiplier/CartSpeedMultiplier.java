package me.ReconWhale.CartSpeedMultiplier;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Cart Speed Multiplier
 * Allows you to set the maximum velocity of minecarts.
 * @author Weilon Ying (ReconWhale)
 *
 */
public class CartSpeedMultiplier extends JavaPlugin {
	
	@Override
	public void onEnable() {
		//copy default configuration if none found
		saveDefaultConfig();
		
		//register event listener
		getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
	}
	
	@Override 
	public void onDisable()  {
		//nothing needed to be done when disabled
	}
	
}
