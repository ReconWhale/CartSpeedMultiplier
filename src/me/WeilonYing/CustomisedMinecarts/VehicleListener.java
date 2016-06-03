package me.WeilonYing.CustomisedMinecarts;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Detects vehicle spawns and responds accordingly.
 * @author Weilon Ying
 *
 */
public class VehicleListener implements Listener {
    private JavaPlugin plugin;
    private FileConfiguration cf;
    private Manipulator m;

    public VehicleListener (JavaPlugin pl, Manipulator m) {
        this.plugin = pl;
        this.cf = this.plugin.getConfig();
        this.m = m;
    }

    /**
     * Sets minecart's parameters upon its spawning
     * @param evt The minecart's creation (spawning) event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartSpawn (VehicleCreateEvent evt) {
        if (evt.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart) evt.getVehicle();

            //Slow when empty setting
            m.setMinecartSlowWhenEmpty(cart, cf.getBoolean(Definitions.SETTING_SLOW_WHEN_EMPTY));
        }
    }
    
    /**
     * Sets minecart's parameters when it moves
     * @param evt The minecart's movement event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartMove (VehicleMoveEvent evt) {
        if (evt.getVehicle() instanceof Minecart) {
            Minecart cart = (Minecart) evt.getVehicle();
            
            m.manipulateMinecart(cart);
        }
    }
}
