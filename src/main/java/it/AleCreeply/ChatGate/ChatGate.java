package it.AleCreeply.ChatGate;

import it.AleCreeply.ChatGate.listeners.JoinListener;
import it.AleCreeply.ChatGate.listeners.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import it.AleCreeply.ChatGate.models.CustomChat;
import it.AleCreeply.ChatGate.commands.ChatCommand;
import it.AleCreeply.ChatGate.listeners.ChatListener;
import it.AleCreeply.ChatGate.placeholders.PlaceholderAPIExpansion;
import it.AleCreeply.ChatGate.commands.ChatGateCommand;

import java.util.HashMap;
import java.util.Map;

public final class ChatGate extends JavaPlugin {

    private static ChatGate instance;
    private UpdateChecker updateChecker;
    private final Map<String, CustomChat> chats = new HashMap<>();

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        loadChats();

        updateChecker = new UpdateChecker(this, 127301);
        updateChecker.checkForUpdate();

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        getCommand("chat").setExecutor(new ChatCommand());
        getCommand("chat").setTabCompleter(new ChatCommand());
        getCommand("chatgate").setExecutor(new ChatGateCommand());
        getCommand("chatgate").setTabCompleter(new ChatGateCommand());

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIExpansion().register();
            getLogger().info("PlaceholderAPI hook registered");
        } else {
            getLogger().warning("Placeholder hook not registered");
        }

        long elapsed = System.currentTimeMillis() - start;
        getLogger().info("Plugin enabled in " + elapsed);
    }

    public void loadChats(){
        chats.clear();

        ConfigurationSection section = getConfig().getConfigurationSection("chats");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                String format = section.getString(key + ".format");
                String displayname = section.getString(key + ".display-name");

                chats.put(key.toLowerCase(), new CustomChat(key, format, displayname));
            }
        }
    }

    public Map<String, CustomChat> getChats() {
        return chats;
    }

    public static ChatGate getInstance() {
        return instance;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}
