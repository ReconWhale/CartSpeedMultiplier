package me.WeilonYing.CustomisedMinecarts;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockListener implements Listener {
    JavaPlugin pl;
    Manipulator m;
    public BlockListener (JavaPlugin plugin, Manipulator m) {
        this.pl = plugin;
        this.m = m;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onRedstoneEvent (BlockRedstoneEvent evt) {
        m.redstoneHandler(evt.getBlock());
    }
}
