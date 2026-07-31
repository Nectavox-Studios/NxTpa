package com.nectavox.nxtpa.listeners;

import com.nectavox.nxtpa.managers.DataManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {
    private final DataManager manager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        manager.load(event.getPlayer());
    }
}
