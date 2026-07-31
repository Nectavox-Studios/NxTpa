package com.nectavox.nxtpa.commands;

import com.nectavox.nxtpa.NxTpa;
import com.nectavox.nxtpa.guis.IgnoreGUI;
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
@Command(name = "nxtpignore")
@HasPermission(Perms.IGNORE_CMD)
@PlayerOnly
public class IgnoreCommand extends CommandBase {
    private final NxTpa plugin;

    @Override
    public void onNoPermission(CommandSender sender) {
        LANG.sendMessage(sender, "NO_PERM", true, true);
    }

    @DefaultCommand
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            IgnoreGUI.openIgnoreSelector(player, plugin);
        } else {
            String targetName = args[0];

            if (targetName.equals(player.getName())) {
                LANG.sendMessage(sender, "IGNORE_SELF", true, true);
                return;
            }

            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                LANG.sendMessage(sender, "PLAYER_NOT_FOUND", true, true);
                return;
            }

            plugin.getDataManager().ignorePlayer(player, target);
        }
    }

    @Completer("nxtpignore")
    public List<String> completer(CommandSender sender, String[] args) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(target -> !plugin.getDataManager().isIgnored((Player) sender, target))
                .map(Player::getName)
                .filter(filter -> filter.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList();
    }
}
