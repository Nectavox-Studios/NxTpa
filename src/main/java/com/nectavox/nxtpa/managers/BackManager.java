package com.nectavox.nxtpa.managers;

import com.nectavox.nxcore.utils.TimeFormatUtil;
import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.guis.BackConfirmGUI;
import com.nectavox.nxtpa.models.PlayerData;
import com.nectavox.nxtpa.utils.Perms;
import com.nectavox.nxtpa.utils.TeleportUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.nectavox.nxtpa.NxTpa.LANG;

@RequiredArgsConstructor
public class BackManager {
    private final NxTpa plugin;

    private final Map<UUID, Location> backLocations = new ConcurrentHashMap<>();
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();


    public void addBackLocation(Player player, Location location) {
        backLocations.put(player.getUniqueId(), location);
    }

    public void resetBackLocation(Player player) {
        backLocations.remove(player.getUniqueId());
        cooldowns.remove(player.getUniqueId());
    }

    public Location getBackLocation(Player player) {
        return backLocations.get(player.getUniqueId());
    }

    public void handleBack(Player player) {
        Location location = backLocations.get(player.getUniqueId());
        if (location == null) {

            LANG.sendMessage(player, "BACK_NOT_FOUND", true, true);
            return;
        }

        if (getCooldown(player) > 0) {
            LANG.sendMessage(player, "BACK_COOLDOWN", true, true,
                    "%time%", TimeFormatUtil.getTimeFormatted(getCooldown(player)));
            return;
        }

        PlayerData data = plugin.getDataManager().getPlayerData(player);
        if (data.isBackConfirm()) {
            BackConfirmGUI.open(player, location, plugin);
        } else {
            TeleportUtil.teleport(player, location, plugin, () -> {
                setCooldown(player);
            });
        }

    }

    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public int getCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) return 0;
        if(player.hasPermission(Perms.BYPASS_BACK_COOLDOWN)) return 0;

        long lastUse = cooldowns.get(player.getUniqueId());
        long now = System.currentTimeMillis();
        long elapsed = now - lastUse;

        long cooldown = plugin.getConfig().getInt("back-cooldown", 0) * 1000L;

        long remaining = cooldown - elapsed;

        if (remaining > 0) {
            return (int) (remaining / 1000);
        }

        return 0;
    }


}
