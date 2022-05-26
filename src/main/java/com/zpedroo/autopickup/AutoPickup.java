package com.zpedroo.autopickup;

import com.zpedroo.autopickup.listeners.PlayerGeneralListeners;
import com.zpedroo.autopickup.utils.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoPickup extends JavaPlugin {

    public void onEnable() {
        new FileUtils(this);
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }
}