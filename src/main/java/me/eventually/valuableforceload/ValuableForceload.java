package me.eventually.valuableforceload;

import me.eventually.valuableforceload.commands.MainCommand;
import me.eventually.valuableforceload.economy.EconomyPlayerPoints;
import me.eventually.valuableforceload.economy.EconomyVault;
import me.eventually.valuableforceload.economy.EconomyXConomy;
import me.eventually.valuableforceload.listener.PlayerListener;
import me.eventually.valuableforceload.manager.EconomyManager;
import me.eventually.valuableforceload.manager.ForceloadChunkManager;
import me.eventually.valuableforceload.manager.PlayerChunkLimitManager;
import me.eventually.valuableforceload.structure.Locale;
import me.eventually.valuableforceload.structure.Price;
import me.eventually.valuableforceload.structure.PriceType;
import me.eventually.valuableforceload.utils.I18nUtil;
import me.eventually.valuableforceload.utils.inventory.Menu;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"unused", ""})
public final class ValuableForceload extends JavaPlugin {
    private static ValuableForceload instance;

    // Config
    private FileConfiguration config;
    private final File configFile = new File(getDataFolder(), "config.yml");

    // Plugin
    public static ValuableForceload getInstance() {
        return instance;
    }
    private Metrics metrics;


    // Economy
    private static EconomyVault economyVault = null;
    private static EconomyPlayerPoints economyPlayerPoints = null;
    private static EconomyXConomy economyXConomy = null;
    public static EconomyPlayerPoints getPlayerPoints() {
        return economyPlayerPoints;
    }
    public static EconomyVault getEconomyVault() {
        return economyVault;
    }
    public static EconomyXConomy getEconomyXConomy() {
        return economyXConomy;
    }

    public static Price price;
    public static Price getPrice() { return price; }


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        loadConfig();

        // Listeners setup
        Menu.setupListeners(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Commands setup
        getServer().getPluginCommand("valuableforceload").setExecutor(new MainCommand());
        getServer().getPluginCommand("valuableforceload").setTabCompleter(new MainCommand());

        // bStats metrics
        metrics = new Metrics(this, 24805);
        metrics.addCustomChart(new SingleLineChart("purchased_chunks", () -> ForceloadChunkManager.getForceloadChunks().size()));

        // forceload manager pre load

        ForceloadChunkManager.cleanUpExpiredForceloads();
        ForceloadChunkManager.updateChunkForceloadStatus();

        // bukkit scheduler, run every 300s (5minute)

        new BukkitRunnable() {
            @Override
            public void run() {
                ForceloadChunkManager.updateChunkForceloadStatus();
                ForceloadChunkManager.cleanUpExpiredForceloads();
                getLogger().info("Forceload chunk status updated.");
                getLogger().info(ForceloadChunkManager.getForceloadChunks().toString());
            }
        }.runTaskTimer(this, 0L, 30 * 20L);


        // Economy plugin setup
        boolean availableEconomy = false;

        economyVault = new EconomyVault();
        if (economyVault.setupApi()) {
            getLogger().info(I18nUtil.get("economy-plugin-setup-successfully", "Vault"));
            availableEconomy = true;
        } else {
            getLogger().info(I18nUtil.get("economy-plugin-setup-failed", "Vault"));
            for (int i = 1; i <= 5; i++) {
                getLogger().info(I18nUtil.get("economy-plugin-setup-failed-causes-" + i, "Vault"));
            }

        }
        economyPlayerPoints = new EconomyPlayerPoints();
        if (economyPlayerPoints.setupApi()) {
            getLogger().info(I18nUtil.get("economy-plugin-setup-successfully", "PlayerPoints"));
            availableEconomy = true;
        }else {
            getLogger().info(I18nUtil.get("economy-plugin-setup-failed", "PlayerPoints"));
            for (int i = 1; i <= 5; i++) {
                getLogger().info(I18nUtil.get("economy-plugin-setup-failed-causes-" + i, "PlayerPoints"));
            }
        }
        economyXConomy = new EconomyXConomy();
        if (economyXConomy.setupApi()) {
            getLogger().info(I18nUtil.get("economy-plugin-setup-successfully", "Xconomy"));
            availableEconomy = true;
        } else {
            getLogger().info(I18nUtil.get("economy-plugin-setup-failed", "XConomy"));
            for (int i = 1; i <= 5; i++) {
                getLogger().info(I18nUtil.get("economy-plugin-setup-failed-causes-" + i, "XConomy"));
            }
        }

        if (!availableEconomy) {
            getLogger().severe(I18nUtil.get("economy-plugin-no-available"));
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ForceloadChunkManager.unloadAll();
        saveConfig();

        if (this.metrics != null){
            metrics.shutdown();
        }
    }

    public void loadConfig() {
        if (!getDataFolder().exists()) {
            boolean created = getDataFolder().mkdir();
            if (!created) {
                getLogger().severe("Failed to create data folder. Plugin cannot work anymore.");
                manualDisable();
            }
        }
        if (!configFile.exists()) {
            saveResource("config.yml", true);
        }
        if (!getDataFolder().toPath().resolve("lang").toFile().exists()) {
            getDataFolder().toPath().resolve("lang").toFile().mkdirs();
        }
        for (Locale locale : Locale.values()) {
            saveResource("lang/messages_" + locale.getLocaleName() + ".json", true);
        }

        config = getConfig();

        // Default configs
        config.addDefault("locale", "en");
        config.addDefault("max-forceload-chunks-per-player", 3);
        config.addDefault("forceload-chunk-price.economy-type", "PLUGIN_PLAYERPOINTS");
        config.addDefault("forceload-chunk-price.value", 200);
        config.addDefault("forceload-chunk-price.days", 3);
        config.addDefault("forceload-chunk-data", List.of());

        // Configs setup
        PlayerChunkLimitManager.setDefaultLimit(config.getInt("max-forceload-chunks-per-player"));
        EconomyManager.setPriceType(PriceType.valueOf(config.getString("forceload-chunk-price.economy-type")));
        EconomyManager.setPrice(config.getInt("forceload-chunk-price.value"));
        EconomyManager.setBuyTimeOnce(config.getInt("forceload-chunk-price.days"));
        I18nUtil.setLocale(Locale.valueOf(config.getString("locale")));
        ForceloadChunkManager.parseAllChunks((List<Map<String, Object>>) config.getList("forceload-chunk-data"));
        saveConfig();


        // Forceload chunk price~
        price = EconomyManager.createPrice();
    }
    @Override
    public void saveConfig() {
        getLogger().info("Saving config...");
        config.set("max-forceload-chunks-per-player", PlayerChunkLimitManager.getDefaultLimit());
        config.set("forceload-chunk-price.economy-type", EconomyManager.getPriceType().name());
        config.set("forceload-chunk-price.value", EconomyManager.getPrice());
        config.set("forceload-chunk-price.days", EconomyManager.getBuyTimeOnce());
        config.set("locale", I18nUtil.getLocale());
        config.set("forceload-chunk-data", ForceloadChunkManager.mapAllChunks());
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save config.yml");
            getLogger().severe("Error: " + e.getMessage());
            getLogger().severe("StackTrace: " + Arrays.toString(e.getStackTrace()));
            manualDisable();
        }
    }

    public void manualDisable() {
        getLogger().severe("Disabling ValuableForceload (IF YOU SEE THIS, PLEASE CHECK THE ERRORS ABOVE)");
        saveConfig();
        getServer().getPluginManager().disablePlugin(this);
    }

}
