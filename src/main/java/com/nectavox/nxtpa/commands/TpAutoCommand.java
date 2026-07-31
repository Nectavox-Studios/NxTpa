package com.nectavox.nxtpa.commands;

import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.models.PlayerData;
import com.nectavox.nxtpa.utils.Perms;
import com.nectavox.nxcore.commands.CommandBase;
import com.nectavox.nxcore.commands.annotation.Command;
import com.nectavox.nxcore.commands.annotation.DefaultCommand;
import com.nectavox.nxcore.commands.annotation.access.HasPermission;
import com.nectavox.nxcore.commands.annotation.access.PlayerOnly;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.nectavox.nxtpa.NxTpa.LANG;

@RequiredArgsConstructor
@Command(name = "nxtpauto")
@HasPermission(Perms.TP_AUTO_CMD)
@PlayerOnly
public class TpAutoCommand extends CommandBase {
    private final NxTpa plugin;

    @Override
    public void onNoPermission(CommandSender sender) {
        LANG.sendMessage(sender, "NO_PERM", true, true);
    }

    @DefaultCommand
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        PlayerData data = plugin.getDataManager().getPlayerData(player);
        plugin.getDataManager().toggleTpAuto(player, data);

        if (data.isTpAuto()) {
            player.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
        } else {
            player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);
        }
    }
}
