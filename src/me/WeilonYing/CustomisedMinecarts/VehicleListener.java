package me.WeilonYing.CustomisedMinecarts;


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
			cart.setSlowWhenEmpty(getMinecartSlowWhenEmpty());
		}
	}
	
	private boolean getMinecartSlowWhenEmpty() {
		String slowWhenEmpty = getSetting(Definitions.SETTING_SLOW_WHEN_EMPTY);
		if (slowWhenEmpty.equalsIgnoreCase ("true")) {
			return true;
		} else if (slowWhenEmpty.equalsIgnoreCase ("false")) {
			return false;
		} else {
			reportError(Definitions.SETTING_SLOW_WHEN_EMPTY);
			return false;
		}
	}

	private double getMinecartSpeed () {
		double speed = Definitions.DEFAULT_MAX_MINECART_SPEED;
		try {
			//set speed according to config.yml
			String strMultiplier = getSetting (Definitions.SETTING_MAX_MINECART_SPEED);
			double multiplier = Double.parseDouble (strMultiplier);
			speed = multiplier * Definitions.DEFAULT_MAX_MINECART_SPEED;
		} catch (NumberFormatException e) {
			reportError(Definitions.SETTING_MAX_MINECART_SPEED);
		}
		return speed;
	}
	
	private void reportError (String setting) {
		plugin.getLogger().severe("Unable to parse setting " + setting + " in config.yml. Using default setting.");
	}
	private String getSetting(String setting) {
		return plugin.getConfig().getString(setting);
	}
}
