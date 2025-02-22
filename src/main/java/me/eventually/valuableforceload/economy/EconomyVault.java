package me.eventually.valuableforceload.economy;

import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.exceptions.NotSetupException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyVault extends AbstractEconomyAPI<Economy> {
    private static Economy api = null;

    public Economy getApi() throws NotSetupException {
        return api;
    }

    public Boolean setupApi() {
        if (ValuableForceload.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = ValuableForceload.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        api = rsp.getProvider();
        return api != null;
    }
}
