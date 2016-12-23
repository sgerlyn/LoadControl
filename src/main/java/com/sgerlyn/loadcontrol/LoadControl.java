package com.sgerlyn.loadcontrol;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class LoadControl extends JavaPlugin implements Listener {

    private List<String> worlds;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        String name = world.getName();

        if (!worlds.contains(name)) {
            List<String> blacklist = getConfig().getStringList("worlds");
            for (String blacklisted : blacklist) {
                if (name.contains(blacklisted)) {
                    world.setKeepSpawnInMemory(true);
                    getServer().unloadWorld(name, false);
                    worlds.add(name);
                    new WorldUnloadEvent(world);
                    getLogger().info("World " + name + " has been unloaded due to your WOrldControl configuration.");
                }
            }
        }
    }
}
