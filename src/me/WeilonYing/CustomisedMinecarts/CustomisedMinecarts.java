package me.WeilonYing.CustomisedMinecarts;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * CustomisedMinecarts
 * Allows you to set the maximum velocity of minecarts and boats.
 * @author Weilon Ying (http://github.com/WeilonYing)
 *
 */
public class CustomisedMinecarts extends JavaPlugin {
	
	@Override
	public void onEnable() {
		//copy default configuration if none found
		saveDefaultConfig();
		
		//in case this plugin was reloaded, we refresh the config
		reloadConfig();
		
		//register event listener
		getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
	}
	
	@Override 
	public void onDisable()  {
		//nothing needed to be done when disabled
	}
	
}
