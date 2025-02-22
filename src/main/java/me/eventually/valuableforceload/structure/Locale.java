package me.eventually.valuableforceload.structure;

public enum Locale {
    en("en"),
    zh_CN("zh_CN");

    private final String localeName;
    Locale(String name){
        this.localeName = name;
    }

    public String getLocaleName() {
        return localeName;
    }
    public static Locale getDefaultLocale() {
        return en;
    }
}
