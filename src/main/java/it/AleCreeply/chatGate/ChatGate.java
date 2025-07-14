package it.AleCreeply.chatGate;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import it.AleCreeply.chatGate.models.customChat;
import it.AleCreeply.chatGate.commands.chatCommand;
import it.AleCreeply.chatGate.listeners.chatListener;
import it.AleCreeply.chatGate.placeholders.placeholderapiExpansion;
import it.AleCreeply.chatGate.commands.chatGateCommand;

import java.util.HashMap;
import java.util.Map;

public final class ChatGate extends JavaPlugin {

    private static ChatGate instance;
    private final Map<String, customChat> chats = new HashMap<>();

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        loadChats();

        getCommand("chat").setExecutor(new chatCommand());
        getCommand("chat").setTabCompleter(new chatCommand());
        getCommand("chatgate").setExecutor(new chatGateCommand());
        getCommand("chatgate").setTabCompleter(new chatGateCommand());

        getServer().getPluginManager().registerEvents(new chatListener(), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new placeholderapiExpansion().register();
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

                chats.put(key.toLowerCase(), new customChat(key, format, displayname));
            }
        }
    }

    public Map<String, customChat> getChats() {
        return chats;
    }

    public static ChatGate getInstance() {
        return instance;
    }



    @Override
    public void onDisable() {

    }
}
