package com.nectavox.nxtpa.guis;

import com.nectavox.nxcore.models.gui.GuiData;
import com.nectavox.nxcore.utils.GuiUtil;
import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.utils.NavigationUtil;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TPAHerePlayerSelectorGUI {
    public static void open(Player player, NxTpa plugin) {
        GuiData data = plugin.getMenuManager().getGui("tpa_here_player_selector");
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(data.getTitle()))
                .rows(data.getRows())
                .pageSize(data.getItemsPerPage())
                .disableAllInteractions()
                .create();

        NavigationUtil.buildPreviousButton(gui, data, player);
        NavigationUtil.buildNextButton(gui, data, player);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equals(player.getName())) continue;
            GuiUtil.addSkullItem(onlinePlayer, onlinePlayer, gui, data, "player", e -> {
                plugin.getTpaManager().requestTpaHere(player, onlinePlayer, false);
            }, "%player%", onlinePlayer.getName());
        }

        gui.update();
        gui.updateTitle(
                Component.text(data.getTitle()
                        .replace("%page%", String.valueOf(gui.getCurrentPageNum()))
                        .replace("%pageSize%", String.valueOf(gui.getPagesNum()))
                )
        );

        gui.open(player);
    }
}
