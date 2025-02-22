package me.eventually.valuableforceload.manager;

import me.eventually.valuableforceload.structure.Price;
import me.eventually.valuableforceload.structure.PriceType;

public class EconomyManager {
    private static PriceType priceType;
    private static int price;
    private static int buyTimeOnce;

    public static PriceType getPriceType() {
        return priceType;
    }

    public static void setPriceType(PriceType priceType) {
        EconomyManager.priceType = priceType;
    }

    public static int getPrice() {
        return price;
    }

    public static void setPrice(int price) {
        EconomyManager.price = price;
    }

    public static int getBuyTimeOnce() {
        return buyTimeOnce;
    }

    public static void setBuyTimeOnce(int buyTimeOnce) {
        EconomyManager.buyTimeOnce = buyTimeOnce;
    }

    public static Price createPrice() {
        return new Price(price, priceType);
    }
}
