package com.nectavox.nxtpa.commands;

import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.guis.TPAAcceptListGUI;
import com.nectavox.nxtpa.models.PlayerData;
import com.nectavox.nxtpa.utils.Perms;
import com.nectavox.nxcore.commands.CommandBase;
import com.nectavox.nxcore.commands.annotation.Command;
import com.nectavox.nxcore.commands.annotation.Completer;
import com.nectavox.nxcore.commands.annotation.DefaultCommand;
import com.nectavox.nxcore.commands.annotation.access.HasPermission;
import com.nectavox.nxcore.commands.annotation.access.PlayerOnly;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.nectavox.nxtpa.NxTpa.LANG;

@RequiredArgsConstructor
@Command(name = "nxtpaaccept")
@HasPermission(Perms.TPA_ACCEPT_CMD)
@PlayerOnly
public class TpaAcceptCommand extends CommandBase {
    private final NxTpa plugin;

    @Override
    public void onNoPermission(CommandSender sender) {
        LANG.sendMessage(sender, "NO_PERM", true, true);
    }

    @DefaultCommand
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            PlayerData data = plugin.getDataManager().getPlayerData(player);
            if (data == null) return;

            if (data.isTpaAccept()) {
                TPAAcceptListGUI.open(player, plugin);
            } else {
                plugin.getTpaManager().requestAcceptLast(player, false);
            }

        } else {
            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                LANG.sendMessage(sender, "TPA_REQUEST_ACCEPT_NOT_EXIST", true, true, "%player%", targetName);
                return;
            }

            plugin.getTpaManager().requestAccept(player, target, false);
        }
    }

    @Completer("nxtpaaccept")
    public List<String> completer(CommandSender sender, String[] args) {
        return plugin.getTpaManager().getRequestedTpaPlayer((Player) sender);
    }

}
