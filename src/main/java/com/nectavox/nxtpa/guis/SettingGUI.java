package com.nectavox.nxtpa.guis;

import com.nectavox.nxcore.models.gui.GuiData;
import com.nectavox.nxcore.models.gui.GuiItemData;
import com.nectavox.nxcore.utils.GuiUtil;
import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.managers.DataManager;
import com.nectavox.nxtpa.models.PlayerData;
import com.nectavox.nxtpa.utils.Perms;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static com.nectavox.nxtpa.NxTpa.LANG;

public class SettingGUI {
    public static void open(Player player, NxTpa plugin) {
        GuiData data = plugin.getMenuManager().getGui("settings");
        Gui gui = Gui.gui()
                .title(Component.text(data.getTitle()))
                .title(Component.text(data.getTitle()))
                .rows(data.getRows())
                .disableAllInteractions()
                .create();

        DataManager manager = plugin.getDataManager();
        PlayerData playerData = manager.getPlayerData(player);

        String on = data.getItem("status_on").getName();
        String off = data.getItem("status_off").getName();


        Sound enable = data.getItem("enabled").getSound();
        Sound disable = data.getItem("disabled").getSound();

        for (GuiItemData guiItemData : data.getItems().values()) {
            String key = guiItemData.getId();
            if (key.equals("status_on") || key.equals("status_off") || key.equals("enabled") || key.equals("disabled"))
                continue;

            switch (key) {
                case "tpa_confirm_menu": {
                    GuiUtil.setItem(player, gui, data, key, iData -> {
                        if (!player.hasPermission(Perms.SETTING_TPA_CONFIRM)) {
                            LANG.sendMessage(player, "NO_PERM", true, true);
                            return;
                        }

                        playSound(player, manager.toggleTpaConfirm(playerData), enable, disable);
                        open(player, plugin);
                    }, "%status%", playerData.isTpaConfirm() ? on : off);
                    break;
                }
                case "tpa_requests": {
                    GuiUtil.setItem(player, gui, data, key, iData -> {
                        if (!player.hasPermission(Perms.SETTING_TPA_REQUESTS)) {
                            LANG.sendMessage(player, "NO_PERM", true, true);
                            return;
                        }

                        playSound(player, manager.toggleTpaRequests(playerData), enable, disable);
                        open(player, plugin);
                    }, "%status%", playerData.isTpaRequests() ? on : off);
                    break;
                }
                case "tpa_here_requests": {
                    GuiUtil.setItem(player, gui, data, key, iData -> {
                        if (!player.hasPermission(Perms.SETTING_TPA_HERE_REQUESTS)) {
                            LANG.sendMessage(player, "NO_PERM", true, true);
                            return;
                        }
                        playSound(player, manager.toggleTpaHereRequests(playerData), enable, disable);
                        open(player, plugin);
                    }, "%status%", playerData.isTpaHereRequests() ? on : off);
                    break;
                }
                case "tp_auto": {
                    GuiUtil.setItem(player, gui, data, key, iData -> {
                        if (!player.hasPermission(Perms.SETTING_TP_AUTO)) {
                            LANG.sendMessage(player, "NO_PERM", true, true);
                            return;
                        }

                        playSound(player, manager.toggleTpAuto(player, playerData), enable, disable);
                        open(player, plugin);
                    }, "%status%", playerData.isTpAuto() ? on : off);
                    break;
                }
                case "tpa_accept": {
                    GuiUtil.setItem(player, gui, data, key, iData -> {
                        if (!player.hasPermission(Perms.SETTING_TPA_ACCEPT)) {
                            LANG.sendMessage(player, "NO_PERM", true, true);
                            return;
                        }
                        playSound(player, manager.toggleTpaAccept(playerData), enable, disable);
                        open(player, plugin);
                    }, "%status%", playerData.isTpaAccept() ? on : off);
                    break;
                }
                case "back_confirm": {
                    GuiUtil.setItem(player, gui, data, key, iData -> {
                        if (!player.hasPermission(Perms.SETTING_BACK_CONFIRM)) {
                            LANG.sendMessage(player, "NO_PERM", true, true);
                            return;
                        }
                        playSound(player, manager.toggleBackConfirm(playerData), enable, disable);
                        open(player, plugin);
                    }, "%status%", playerData.isBackConfirm() ? on : off);
                    break;
                }
            }
        }

        gui.open(player);

    }

    private static void playSound(Player player, boolean status, Sound enable, Sound disable) {
        if (status && enable != null) {
            player.playSound(player, enable, 1f, 1f);
        } else if (!status && disable != null) {
            player.playSound(player, disable, 1f, 1f);
        }
    }
}
