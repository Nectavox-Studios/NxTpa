package com.nectavox.nxtpa.guis;

import com.nectavox.nxcore.models.GuiData;
import com.nectavox.nxcore.models.GuiItemData;
import com.nectavox.nxcore.utils.GuiUtil;
import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.utils.NavigationUtil;
import com.nectavox.nxtpa.utils.TeleportUtil;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackConfirmGUI {
    public static void open(Player player, Location loc, NxTpa plugin) {
        GuiData data = plugin.getMenuManager().getGui("back_confirm");
        Gui gui = Gui.gui()
                .title(Component.text(data.getTitle()))
                .rows(data.getRows())
                .disableAllInteractions()
                .create();

        GuiUtil.setItem(player, gui, data, "cancel", iData -> {
            gui.close(player);
        });


        GuiUtil.setItem(player, gui, data, "location", iData -> {
        }, "%world%", loc.getWorld().getName(), "%x%", loc.getBlockX(), "%y%", loc.getBlockY(), "%z%", loc.getBlockZ());


        GuiUtil.setItem(player, gui, data, "confirm", iData -> {
            gui.close(player);
            TeleportUtil.teleport(player, loc, plugin, () -> {
                plugin.getBackManager().setCooldown(player);
            });
        });
        gui.open(player);
    }
}
