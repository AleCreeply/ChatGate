package it.AleCreeply.ChatGate.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
    private final JavaPlugin plugin;
    private final int resourceId;
    private String latestVersion = null;
    private boolean updateAvailable = false;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void checkForUpdate() {
        if (!plugin.getConfig().getBoolean("check-for-updates", true)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                Scanner scanner = new Scanner(conn.getInputStream());
                if (scanner.hasNext()) {
                    latestVersion = scanner.next();
                    String currentVersion = plugin.getDescription().getVersion();

                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        updateAvailable = true;

                        plugin.getLogger().warning("================================================");
                        plugin.getLogger().warning("An update is avabile for " + plugin.getName() + "!");
                        plugin.getLogger().warning("Current Version: " + currentVersion);
                        plugin.getLogger().warning("Latest Version: " + latestVersion);
                        plugin.getLogger().warning("Download id at: https://www.spigotmc.org/resources/" + resourceId);
                        plugin.getLogger().warning("================================================");
                    } else {
                        plugin.getLogger().info("The plugin is up-to-date! (" + currentVersion + ")");
                    }
                }
                scanner.close();
            } catch (IOException e) {
                plugin.getLogger().warning("Error while checking for updates: " + e.getMessage());
            }
        });
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
