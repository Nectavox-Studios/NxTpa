package com.nectavox.nxtpa.guis;

import com.nectavox.nxcore.models.GuiData;
import com.nectavox.nxcore.models.GuiItemData;
import com.nectavox.nxcore.utils.GuiUtil;
import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.utils.NavigationUtil;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TPAConfirmGUI {
    public static void open(Player player, Player target, boolean accept, NxTpa plugin) {
        GuiData data = plugin.getMenuManager().getGui("tpa_confirm");
        Gui gui = Gui.gui()
                .title(Component.text(data.getTitle()))
                .rows(data.getRows())
                .disableAllInteractions()
                .create();

        GuiUtil.setItem(player, gui, data, "cancel", iData -> {
            gui.close(player);
        });

        GuiUtil.setItem(target, gui, data, "location", iData -> {
        });
        GuiUtil.setSkullItem(target, target, gui, data, "player", iData -> {
        }, "%player%", target.getName());

        GuiUtil.setItem(player, gui, data, "confirm", iData -> {
            gui.close(player);
            if (!accept) {
                plugin.getTpaManager().requestTpa(player, target, true);
            } else {
                plugin.getTpaManager().requestAccept(player, target, true);
            }
        });

        gui.open(player);
    }
}
