package com.nectavox.nxtpa.commands;

import com.nectavox.nxtpa.NxTpa;
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
@Command(name = "nxback")
@HasPermission(Perms.BACK_CMD)
@PlayerOnly
public class BackCommand extends CommandBase {
    private final NxTpa plugin;

    @Override
    public void onNoPermission(CommandSender sender) {
        LANG.sendMessage(sender, "NO_PERM", true, true);
    }

    @DefaultCommand
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            plugin.getBackManager().handleBack(player);
        } else {
            if (!player.hasPermission(Perms.BACK_OTHER_CMD)) {
                onNoPermission(sender);
                return;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                LANG.sendMessage(sender, "PLAYER_NOT_FOUND", true, true);
                return;
            }

            plugin.getBackManager().handleBack(target);
        }
    }

    @Completer("nxback")
    public List<String> completer(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Perms.BACK_OTHER_CMD)) {
            return List.of();
        }
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(filter -> filter.toLowerCase().startsWith(args[0].toLowerCase())).toList();
    }
}
