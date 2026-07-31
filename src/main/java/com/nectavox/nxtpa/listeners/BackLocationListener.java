package com.nectavox.nxtpa.listeners;

import com.nectavox.nxtpa.managers.BackManager;
import com.nectavox.nxtpa.utils.Perms;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@RequiredArgsConstructor
public class BackLocationListener implements Listener {
    private final BackManager manager;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        manager.addBackLocation(event.getPlayer(), event.getFrom());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!player.hasPermission(Perms.BACK_ON_DEATH)) return;

        manager.addBackLocation(player, event.getEntity().getLastDeathLocation());
    }

}
