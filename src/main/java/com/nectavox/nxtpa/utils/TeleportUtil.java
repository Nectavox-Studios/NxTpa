package com.nectavox.nxtpa.utils;

import com.nectavox.nxtpa.NxTpa;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

import static com.nectavox.nxtpa.NxTpa.LANG;

public class TeleportUtil {
    public static void teleport(Player player, Location location, NxTpa plugin, Runnable callback) {
        handleTeleport(player, location, plugin, callback);
    }

    public static void teleport(Player player, Location location, NxTpa plugin) {
        handleTeleport(player, location, plugin, () -> {
        });
    }

    private static void handleTeleport(Player player, Location location, NxTpa plugin, Runnable callback) {

        final AtomicInteger delayCounter = new AtomicInteger(plugin.getConfig().getInt("teleport-delay"));
        final double startX = player.getLocation().getX();
        final double startY = player.getLocation().getY();
        final double startZ = player.getLocation().getZ();

        plugin.getScheduler().runTimerForEntity(task -> {
            int currentDelay = delayCounter.get();

            if (currentDelay <= 0 || player.hasPermission(Perms.BYPASS_TELEPORT_DELAY)) {
                com.nectavox.nxcore.utils.TeleportUtil.teleport(player, location);
                LANG.sendMessage(player, "TELEPORT_SUCCESS", true, true);
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1F, 1F);
                callback.run();
                task.cancel();
                return;
            }

            if (player.getLocation().getX() != startX || player.getLocation().getY() != startY || player.getLocation().getZ() != startZ) {
                LANG.sendMessage(player, "TELEPORT_FAIL", true, true);
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                task.cancel();
                return;
            }

            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
            LANG.sendMessage(player, "TELEPORT_DELAY", true, true,
                    "%delay%", String.valueOf(currentDelay));

            plugin.getAudience().sendActionBar(player, plugin.getLangManager().getComponent("TELEPORT_DELAY", true, false,
                    "%delay%", String.valueOf(currentDelay))
            );
            delayCounter.decrementAndGet();

        }, 1L, 20L, player);
    }
}
