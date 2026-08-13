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

import java.util.List;

public class TPAAcceptListGUI {
    public static void open(Player player, NxTpa plugin) {
        GuiData data = plugin.getMenuManager().getGui("tpa_accept_list");
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(data.getTitle()))
                .rows(data.getRows())
                .pageSize(data.getItemsPerPage())
                .disableAllInteractions()
                .create();

        NavigationUtil.buildPreviousButton(gui, data, player);
        NavigationUtil.buildNextButton(gui, data, player);

        List<String> tpa = plugin.getTpaManager().getRequestedTpaPlayer(player);
        List<String> tpahere = plugin.getTpaManager().getRequestedTpaHerePlayer(player);

        if (tpa.isEmpty() && tpahere.isEmpty()) {
            GuiUtil.setItem(player, gui, data, "nothing", iData -> {
            });
        } else {
            for (String pName : tpa) {
                Player p = Bukkit.getPlayer(pName);
                if (p != null && p.isOnline()) {
                    GuiUtil.addSkullItem(p, p, gui, data, "player_tpa", e -> {
                        plugin.getTpaManager().requestTpa(player, p, false);
                    }, "%player%", p.getName());
                }
            }
            for (String pName : tpahere) {
                Player p = Bukkit.getPlayer(pName);
                if (p != null && p.isOnline()) {
                    GuiUtil.addSkullItem(p, p, gui, data, "player_tpa_here", e -> {
                        plugin.getTpaManager().requestTpa(player, p, false);
                    }, "%player%", p.getName());
                }
            }
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
