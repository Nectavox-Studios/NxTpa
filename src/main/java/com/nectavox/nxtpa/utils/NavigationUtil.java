package com.nectavox.nxtpa.utils;

import com.nectavox.nxcore.models.GuiData;
import com.nectavox.nxcore.utils.GuiUtil;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class NavigationUtil {
    public static void buildNextButton(PaginatedGui gui, GuiData data, Player viewer) {
        GuiUtil.setItem(viewer, gui, data, "next", e -> {
            gui.next();
            gui.updateTitle(
                    Component.text(data.getTitle()
                            .replace("%page%", String.valueOf(gui.getCurrentPageNum()))
                            .replace("%pageSize%", String.valueOf(gui.getPagesNum()))
                    )
            );
        });
    }

    public static void buildPreviousButton(PaginatedGui gui, GuiData data, Player viewer) {
        GuiUtil.setItem(viewer, gui, data, "previous", e -> {
            gui.previous();
            gui.updateTitle(
                    Component.text(data.getTitle()
                            .replace("%page%", String.valueOf(gui.getCurrentPageNum()))
                            .replace("%pageSize%", String.valueOf(gui.getPagesNum()))
                    )
            );
        });
    }
}
