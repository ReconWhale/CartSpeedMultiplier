package me.ReconWhale.CartSpeedMultiplier;

import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Detects vehicle spawns and responds accordingly.
 * @author Weilon Ying
 *
 */
public class VehicleListener implements Listener {
	private JavaPlugin plugin;
	
	public VehicleListener (JavaPlugin pl) {
		this.plugin = pl;

	}

	@EventHandler
	public void onMinecartSpawn (VehicleCreateEvent vehicle) {
		if (vehicle.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart) vehicle.getVehicle();
			cart.setMaxSpeed(getMinecartSpeed());
		}
	}
	
	private double getMinecartSpeed () {
		double speed = Definitions.DEFAULT_MAX_MINECART_SPEED;
		try {
			plugin.reloadConfig(); //ensure we have latest version before getting speed.
			
			//set speed according to config.yml
			double multiplier = Double.parseDouble(plugin.getConfig().getString(Definitions.MAX_MINECART_SPEED_SETTING));
			speed = multiplier * Definitions.DEFAULT_MAX_MINECART_SPEED;

		} catch (NumberFormatException e) {
			plugin.getLogger().severe("Unable to parse double " + Definitions.MAX_MINECART_SPEED_SETTING +
					" in config.yml. Please ensure it is a decimal number.");
			plugin.getLogger().warning("Using default minecart speed.");
		}
		
		return speed;
	}
}
