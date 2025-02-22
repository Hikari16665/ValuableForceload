package me.eventually.valuableforceload.economy;

import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.exceptions.NotSetupException;
import me.yic.xconomy.api.XConomyAPI;

public class EconomyXConomy extends AbstractEconomyAPI<XConomyAPI>{
    private static XConomyAPI api = null;
    public Boolean setupApi() {
        if (ValuableForceload.getInstance().getServer().getPluginManager().getPlugin("XConomy") == null) return false;
        api = new XConomyAPI();
        return api.getversion() != null;
    }

    @Override
    public XConomyAPI getApi() throws NotSetupException {
        return api;
    }
}
