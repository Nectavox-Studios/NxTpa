package com.nectavox.nxtpa.managers;

import com.nectavox.nxcore.managers.LangManager;
import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.guis.TPAConfirmGUI;
import com.nectavox.nxtpa.guis.TPAHereConfirmGUI;
import com.nectavox.nxtpa.models.PlayerData;
import com.nectavox.nxtpa.utils.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.nectavox.nxtpa.NxTpa.LANG;

public class TpaManager {

    private final NxTpa plugin;
    private final LangManager langManager;

    public TpaManager(NxTpa plugin) {
        this.plugin = plugin;
        this.langManager = plugin.getLangManager();
    }

    private final Map<String, String> tpaRequests = new ConcurrentHashMap<>();
    private final Map<String, String> tpaHereRequests = new ConcurrentHashMap<>();

    public void requestAcceptLast(Player player, boolean force) {
        String requester = null;

        for (Map.Entry<String, String> entry : tpaRequests.entrySet()) {
            if (entry.getValue().equals(player.getName())) {
                requester = entry.getKey();
            }
        }

        if (requester == null) {
            for (Map.Entry<String, String> entry : tpaHereRequests.entrySet()) {
                if (entry.getValue().equals(player.getName())) {
                    requester = entry.getKey();
                }
            }
        }

        if (requester == null) {
            LANG.sendMessage(player, "TPA_REQUEST_ACCEPT_NOT_EXIST", true, true, "%player%", "");
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        Player target = Bukkit.getPlayerExact(requester);
        if (target == null || !target.isOnline()) {
            LANG.sendMessage(player, "PLAYER_NOT_FOUND", true, true, "%player%", requester);
            return;
        }

        boolean gui = plugin.getDataManager().getPlayerData(player).isTpaConfirm();

        if (gui && !force) {
            TPAConfirmGUI.open(player, target, true, plugin);
        } else {
            accept(player, target);
        }
    }

    public void requestAccept(Player player, Player target, boolean force) {
        boolean gui = plugin.getDataManager().getPlayerData(player).isTpaConfirm();

        if (gui && !force) {
            TPAConfirmGUI.open(player, target, true, plugin);
        } else {
            accept(player, target);
        }
    }

    public void requestTpa(Player player, Player target, boolean force) {
        boolean gui = plugin.getDataManager().getPlayerData(player).isTpaConfirm();

        boolean enable = plugin.getDataManager().getPlayerData(target).isTpaRequests();
        if (!enable) {
            LANG.sendMessage(player, "TPA_NOT_ACCEPTING", true, true);
            return;
        }

        if (player.getName().equals(target.getName())) {
            LANG.sendMessage(player, "TPA_REQUEST_SEND_SELF", true, true);
            return;
        }

        if (gui && !force) {
            TPAConfirmGUI.open(player, target, false, plugin);
        } else {
            if (tpaRequests.containsKey(player.getName()) && tpaRequests.get(player.getName()).equals(target.getName())) {
                LANG.sendMessage(player, "TPA_REQUEST_SEND_EXIST", true, true, "%player%", target.getName());
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                return;
            }

            tpaRequests.put(player.getName(), target.getName());

            if (plugin.getDataManager().getPlayerData(target).isTpAuto()) {
                accept(target, player);
                return;
            }

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
            target.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);

            expireTask(player, target, true);
            LANG.sendMessage(player, "TPA_REQUEST_SEND", true, true, "%player%", target.getName());

            if (plugin.getDataManager().isIgnored(target, player)) return;

            LANG.sendMessage(target, "TPA_REQUEST_RECEIVE", true, true, "%player%", player.getName());
        }
    }

    public void requestTpaHere(Player player, Player target, boolean force) {
        boolean gui = plugin.getDataManager().getPlayerData(player).isTpaConfirm();

        boolean enable = plugin.getDataManager().getPlayerData(target).isTpaHereRequests();
        if (!enable) {
            LANG.sendMessage(player, "TPA_NOT_ACCEPTING", true, true);
            return;
        }


        if (player.getName().equals(target.getName())) {
            LANG.sendMessage(player, "TPA_REQUEST_SEND_SELF", true, true);
            return;
        }

        if (gui && !force) {
            TPAHereConfirmGUI.open(player, target, plugin);
        } else {
            if (tpaHereRequests.containsKey(player.getName()) && tpaHereRequests.get(player.getName()).equals(target.getName())) {
                LANG.sendMessage(player, "TPA_REQUEST_SEND_EXIST", true, true, "%player%", target.getName());
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                return;
            }

            tpaHereRequests.put(player.getName(), target.getName());

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
            target.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);

            expireTask(player, target, false);
            LANG.sendMessage(player, "TPA_REQUEST_SEND", true, true, "%player%", target.getName());

            if (plugin.getDataManager().isIgnored(target, player)) return;


            LANG.sendMessage(target, "TPHERE_REQUEST_RECEIVE", true, true, "%player%", player.getName());
        }
    }

    private boolean accept(Player player, Player target) {
        if (tpaRequests.containsKey(target.getName())) {
            if (tpaRequests.get(target.getName()).equals(player.getName())) {

                TeleportUtil.teleport(target, player.getLocation(), plugin);
                tpaRequests.remove(target.getName());

                LANG.sendMessage(player, "TPA_REQUEST_ACCEPT", true, true, "%player%", target.getName());
                LANG.sendMessage(target, "TPA_REQUEST_ACCEPTED", true, true, "%player%", player.getName());

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                target.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);

                return true;
            }
        } else if (tpaHereRequests.containsKey(target.getName())) {
            if (tpaHereRequests.get(target.getName()).equals(player.getName())) {

                TeleportUtil.teleport(player, target.getLocation(), plugin);
                tpaHereRequests.remove(target.getName());

                LANG.sendMessage(player, "TPHERE_REQUEST_ACCEPT", true, true, "%player%", target.getName());
                LANG.sendMessage(target, "TPHERE_REQUEST_ACCEPTED", true, true, "%player%", player.getName());

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                target.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);


                return true;
            }
        }
        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        LANG.sendMessage(player, "TPA_REQUEST_ACCEPT_NOT_EXIST", true, true, "%player%", target.getName());
        return false;
    }

    public boolean cancel(Player player) {
        if (tpaRequests.containsKey(player.getName())) {
            tpaRequests.remove(player.getName());
            LANG.sendMessage(player, "TPA_REQUEST_CANCEL", true, true);
            return true;
        }
        if (tpaHereRequests.containsKey(player.getName())) {
            tpaHereRequests.remove(player.getName());
            LANG.sendMessage(player, "TPA_REQUEST_CANCEL", true, true);
            return true;
        }

        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        LANG.sendMessage(player, "TPA_REQUEST_CANCEL_NOT_EXIST", true, true);
        return false;
    }

    public List<String> getRequestedTpaPlayer(Player player) {
        List<String> result = new ArrayList<>();
        tpaRequests.forEach((k, v) -> {
            if (v.equals(player.getName())) result.add(k);
        });
        return result;
    }

    public List<String> getRequestedTpaHerePlayer(Player player) {
        List<String> result = new ArrayList<>();
        tpaHereRequests.forEach((k, v) -> {
            if (v.equals(player.getName())) result.add(k);
        });
        return result;
    }

    private void expireTask(Player player, Player target, boolean tpa) {
        plugin.getScheduler().runLaterForEntity(() -> {
            if (tpa) {
                if (tpaRequests.containsKey(player.getName())) {
                    if (tpaRequests.get(player.getName()).equals(target.getName())) {
                        tpaRequests.remove(player.getName());
                        if (player.isOnline()) {
                            LANG.sendMessage(player, "TPA_REQUEST_EXPIRE", true, true);
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                    }
                }
            } else {
                if (tpaHereRequests.containsKey(player.getName())) {
                    if (tpaHereRequests.get(player.getName()).equals(target.getName())) {
                        tpaHereRequests.remove(player.getName());
                        if (player.isOnline()) {
                            LANG.sendMessage(player, "TPA_REQUEST_EXPIRE", true, true);
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                    }
                }
            }
        }, 60 * 20, player);
    }

    public void tpAuto(Player player, PlayerData data) {
        plugin.getScheduler().runTimerForEntity(scheduledTask -> {
            if (!data.isTpAuto()) scheduledTask.cancel();

            plugin.getAudience().sendActionBar(player, langManager.getComponent("TPAUTO_ENABLED", true, false));
        }, 1, 20, player);
    }

}
