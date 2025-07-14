package it.AleCreeply.chatGate;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import it.AleCreeply.chatGate.models.customChat;
import it.AleCreeply.chatGate.commands.chatCommand;
import it.AleCreeply.chatGate.listeners.chatListener;

import java.util.HashMap;
import java.util.Map;

public final class ChatGate extends JavaPlugin {

    private static ChatGate instance;
    private final Map<String, customChat> chats = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled");
        instance = this;
        saveDefaultConfig();
        loadChats();

        getCommand("chat").setExecutor(new chatCommand());
        getCommand("chat").setTabCompleter(new chatCommand());

        getServer().getPluginManager().registerEvents(new chatListener(), this);
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
