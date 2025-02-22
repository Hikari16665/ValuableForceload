package me.eventually.valuableforceload.economy;

import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.exceptions.NotSetupException;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;

public class EconomyPlayerPoints extends AbstractEconomyAPI<PlayerPointsAPI>{
    private PlayerPointsAPI ppApi;

    @Override
    public Boolean setupApi() {
        if (ValuableForceload.getInstance().getServer().getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppApi = PlayerPoints.getInstance().getAPI();
            return true;
        }
        return false;
    }

    @Override
    public PlayerPointsAPI getApi() throws NotSetupException {
        return ppApi;
    }
}
