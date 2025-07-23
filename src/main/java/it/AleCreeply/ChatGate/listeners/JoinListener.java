package it.AleCreeply.ChatGate.listeners;

import it.AleCreeply.ChatGate.ChatGate;
import it.AleCreeply.ChatGate.listeners.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ChatGate plugin;

    public JoinListener(ChatGate plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UpdateChecker checker = plugin.getUpdateChecker();

        if (p.isOp() && checker.isUpdateAvailable()) {
            p.sendMessage(" ");
            p.sendMessage(" §e✱ §7An update is avabile for §f" + plugin.getName() + "§r§7!");
            p.sendMessage(" §8» §7Latest Version: §f" + checker.getLatestVersion());
            p.sendMessage(" ");
            p.sendMessage(" §7Download it at: §bhttps://www.spigotmc.org/resources/12345");
            p.sendMessage(" ");
        }
    }
}