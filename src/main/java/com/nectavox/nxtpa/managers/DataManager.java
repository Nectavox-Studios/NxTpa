package com.nectavox.nxtpa.managers;

import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.models.PlayerData;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static com.nectavox.nxtpa.NxTpa.LANG;

@RequiredArgsConstructor
public class DataManager {
    private final NxTpa plugin;

    private final Map<UUID, PlayerData> datas = new HashMap<>();

    public PlayerData getPlayerData(Player player) {
        return datas.get(player.getUniqueId());
    }

    public void handleQuit(Player player) {
        save(player);
        datas.remove(player.getUniqueId());
    }

    public void load(Player player) {

        File file = new File(plugin.getDataFolder(), "players/" + player.getUniqueId() + ".yml");

        if (!file.exists()) {
            datas.put(player.getUniqueId(),
                    PlayerData.builder()
                            .name(player.getName())
                            .uuid(player.getUniqueId())
                            .ignoredPlayers(new ArrayList<>())
                            .tpAuto(false)
                            .tpaConfirm(true)
                            .tpaRequests(true)
                            .tpaHereRequests(true)
                            .tpaAccept(true)
                            .backConfirm(true)
                            .build()
            );

            return;
        }


        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String name = config.getString("name");
        UUID uuid = UUID.fromString(config.getString("uuid"));

        List<String> ignoredPlayers = config.getStringList("ignored-players");

        boolean tpAuto = config.getBoolean("tp-auto");
        boolean tpaConfirm = config.getBoolean("tpa-confirm");
        boolean tpaRequests = config.getBoolean("tpa-requests");
        boolean tpaHereRequests = config.getBoolean("tpa-here-requests");
        boolean tpaAccept = config.getBoolean("tpa-accept");
        boolean backConfirm = config.getBoolean("back-confirm");

        datas.put(player.getUniqueId(), PlayerData.builder()
                .name(name)
                .uuid(uuid)
                .ignoredPlayers(new ArrayList<>(ignoredPlayers.stream()
                        .map(UUID::fromString)
                        .toList()
                ))
                .tpAuto(tpAuto)
                .tpaConfirm(tpaConfirm)
                .tpaRequests(tpaRequests)
                .tpaHereRequests(tpaHereRequests)
                .tpaAccept(tpaAccept)
                .backConfirm(backConfirm)
                .build());

    }

    public void save(Player player) {
        PlayerData playerData = datas.get(player.getUniqueId());
        if (playerData == null) return;

        try {
            File dir = new File(plugin.getDataFolder(), "players");
            if (!dir.exists()) {
                dir.mkdirs();
            }


            File file = new File(plugin.getDataFolder(), "players/" + player.getUniqueId() + ".yml");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set("name", playerData.getName());
            config.set("uuid", playerData.getUuid().toString());
            config.set(
                    "ignored-players",
                    playerData.getIgnoredPlayers()
                            .stream()
                            .map(UUID::toString)
                            .toList()
            );
            config.set("tp-auto", playerData.isTpAuto());
            config.set("tpa-confirm", playerData.isTpaConfirm());
            config.set("tpa-requests", playerData.isTpaRequests());
            config.set("tpa-here-requests", playerData.isTpaHereRequests());
            config.set("tpa-accept", playerData.isTpaAccept());
            config.set("back-confirm", playerData.isBackConfirm());

            config.save(file);

        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "fail to save " + player.getName() + " data.", e);
        }
    }

    public void ignorePlayer(Player player, Player target) {
        PlayerData data = getPlayerData(player);

        if (data.getIgnoredPlayers().contains(target.getUniqueId())) {
            LANG.sendMessage(player, "IGNORE_ALREADY", true, true, "%player%", target.getName());
        } else {
            data.getIgnoredPlayers().add(target.getUniqueId());
            LANG.sendMessage(player, "IGNORE", true, true, "%player%", target.getName());
        }
    }

    public void unIgnorePlayer(Player player, Player target) {
        PlayerData data = getPlayerData(player);

        if (data.getIgnoredPlayers().contains(target.getUniqueId())) {
            data.getIgnoredPlayers().remove(target.getUniqueId());
            LANG.sendMessage(player, "UNIGNORE", true, true, "%player%", target.getName());
        } else {
            LANG.sendMessage(player, "UNIGNORE_ALREADY", true, true, "%player%", target.getName());
        }
    }

    public boolean isIgnored(Player player, Player target) {
        PlayerData data = getPlayerData(player);
        return data.getIgnoredPlayers().contains(target.getUniqueId());
    }

    public boolean toggleTpAuto(Player player, PlayerData playerData) {
        playerData.setTpAuto(!playerData.isTpAuto());

        if (playerData.isTpAuto()) plugin.getTpaManager().tpAuto(player, playerData);

        return playerData.isTpAuto();
    }

    public boolean toggleTpaConfirm(PlayerData playerData) {
        playerData.setTpaConfirm(!playerData.isTpaConfirm());
        return playerData.isTpaConfirm();
    }

    public boolean toggleTpaRequests(PlayerData playerData) {
        playerData.setTpaRequests(!playerData.isTpaRequests());
        return playerData.isTpaRequests();
    }

    public boolean toggleTpaHereRequests(PlayerData playerData) {
        playerData.setTpaHereRequests(!playerData.isTpaHereRequests());
        return playerData.isTpaHereRequests();
    }

    public boolean toggleTpaAccept(PlayerData playerData) {
        playerData.setTpaAccept(!playerData.isTpaAccept());
        return playerData.isTpaAccept();
    }
    public boolean toggleBackConfirm(PlayerData playerData) {
        playerData.setBackConfirm(!playerData.isBackConfirm());
        return playerData.isBackConfirm();
    }


}
