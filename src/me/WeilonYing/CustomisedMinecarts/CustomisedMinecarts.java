package me.WeilonYing.CustomisedMinecarts;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Cart Speed Multiplier
<<<<<<< HEAD:src/me/WeilonYing/CustomisedMinecarts/CustomisedMinecarts.java
 * Allows you to set the maximum velocity of minecarts and boats.
 * @author Weilon Ying (http://github.com/WeilonYing)
=======
 * Allows you to set the maximum velocity of minecarts.
 * @author Weilon Ying (ReconWhale)
>>>>>>> origin/master:src/me/ReconWhale/CartSpeedMultiplier/CartSpeedMultiplier.java
 *
 */
public class CustomisedMinecarts extends JavaPlugin {
	
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
