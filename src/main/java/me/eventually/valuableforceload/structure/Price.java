package me.eventually.valuableforceload.structure;

import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.exceptions.NotSetupException;
import me.eventually.valuableforceload.exceptions.NotSupportedCurrentException;
import me.yic.xconomy.api.XConomyAPI;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;

public class Price {
    private int price;
    private PriceType type;

    public Price(int price, PriceType type) {
        this.price = price;
        this.type = type;
    }

    public Price(){
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(PriceType type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public PriceType getType() {
        return type;
    }

    /**
     * Checks if the player has enough money to pay for the forceload.
     * Must case all types in {@link PriceType}.
     * @param player the player
     * @return true if the player has enough money to pay for the forceload, false otherwise.
     * @throws NotSetupException if the economy type is not setup.
     */
    public Boolean checkEnough(Player player) throws NotSetupException{
        try {
            switch (type){
                case PLUGIN_VAULT:
                    Economy vaultApi = ValuableForceload.getEconomyVault().getApi();
                    return vaultApi.has(player, price);
                case MINECRAFT_EXP_LEVEL:
                    return player.getLevel() >= price;
                case PLUGIN_PLAYERPOINTS:
                    PlayerPointsAPI ppApi = ValuableForceload.getPlayerPoints().getApi();
                    return ppApi.look(player.getUniqueId()) >= price;
                case PLUGIN_XCONOMY:
                    XConomyAPI xconomyApi = ValuableForceload.getEconomyXConomy().getApi();
                    return xconomyApi.getPlayerData(player.getUniqueId()).getBalance().longValueExact() >= price;
                default:
                    throw new NotSetupException("Economy type is not setup.");
            }
        }catch (NotSetupException | NoClassDefFoundError e) {
            throw new NotSetupException(e);
        }catch (ArithmeticException wtf) {
            // xconomy api will throw ArithmeticException when the balance cannot cast long type, and we cannot handle it currently.
            throw new NotSupportedCurrentException(wtf);
        }
    }
    /**
     * Take the money from the player.
     * Must case all types in {@link PriceType}.
     * @param player the player
     * @throws NotSetupException if the economy type is not setup.
     */
    public void pay(Player player) throws NotSetupException {
        Bukkit.getLogger().info(this.getType().toString());
        try {
            switch (type){
                case PLUGIN_VAULT:
                    Economy vaultApi = ValuableForceload.getEconomyVault().getApi();
                    vaultApi.withdrawPlayer(player, price);
                    return;
                case MINECRAFT_EXP_LEVEL:
                    player.setLevel(player.getLevel() - price);
                    return;
                case PLUGIN_PLAYERPOINTS:
                    PlayerPointsAPI ppApi= ValuableForceload.getPlayerPoints().getApi();
                    ppApi.take(player.getUniqueId(), price);
                    return;
                case PLUGIN_XCONOMY:
                    XConomyAPI xconomyApi = ValuableForceload.getEconomyXConomy().getApi();
                    xconomyApi.changePlayerBalance(player.getUniqueId(), player.getName(), BigDecimal.valueOf(price), false);
                    return;
                default:
                    throw new NotSetupException("Economy type is not setup.");
            }
        }catch (NotSetupException e) {
            throw new NotSetupException(e);
        }
    }
    public Map<String, Object> toMap() {
        return Map.of(
                "price", price,
                "type", type.getDisplayName()
        );
    }
}
