package com.nectavox.nxtpa;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nectavox.nxcore.NxPlugin;
import com.nectavox.nxcore.commands.CommandManager;
import com.nectavox.nxcore.managers.LangManager;
import com.nectavox.nxtpa.commands.*;
import com.nectavox.nxtpa.listeners.BackLocationListener;
import com.nectavox.nxtpa.listeners.PlayerJoinListener;
import com.nectavox.nxtpa.listeners.PlayerQuitListener;
import com.nectavox.nxtpa.managers.BackManager;
import com.nectavox.nxtpa.managers.DataManager;
import com.nectavox.nxtpa.managers.TpaManager;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
public final class NxTpa extends NxPlugin {

    private DataManager dataManager;
    private TpaManager tpaManager;
    private BackManager backManager;

    public static LangManager LANG;

    @Override
    public void enable() {
        saveDefaultConfig();

        dataManager = new DataManager(this);
        tpaManager = new TpaManager(this);
        backManager = new BackManager(this);

        LANG = getLangManager();

        registerListeners();
        registerCommand();

        setupMetrics();
    }

    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(dataManager::save);
    }

    private void registerCommand() {
        CommandManager commandManager = getCommandManager();

        commandManager.register(new TpaCommand(this));
        commandManager.register(new TpaSettingCommand(this));
        commandManager.register(new TpaReloadCommand(this));
        commandManager.register(new TpaAcceptCommand(this));
        commandManager.register(new TpaCancelCommand(this));
        commandManager.register(new TpaHereCommand(this));
        commandManager.register(new TpAutoCommand(this));
        commandManager.register(new IgnoreCommand(this));
        commandManager.register(new UnIgnoreCommand(this));
        commandManager.register(new BackCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(dataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(dataManager), this);
        getServer().getPluginManager().registerEvents(new BackLocationListener(backManager), this);
    }

    private void setupMetrics() {
        int pluginId = 33039;
        Metrics metrics = new Metrics(this, pluginId);
    }
}
