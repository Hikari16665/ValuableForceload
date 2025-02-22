package me.eventually.valuableforceload.structure;

import me.eventually.valuableforceload.utils.I18nUtil;

/**
 * Type of price to pay for a forceload chunk.
 * We use it here {@link Price}
 */
public enum PriceType {
    PLUGIN_VAULT("vault"),
    PLUGIN_PLAYERPOINTS("playerpoints"),
    PLUGIN_XCONOMY("xconomy"),
    MINECRAFT_EXP_LEVEL("exp-level");

    private final String displayName;
    PriceType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return I18nUtil.get("economy-name-" + this.displayName);
    }

}
