package me.eventually.valuableforceload.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.structure.Locale;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class I18nUtil {
    private static final Path BASE_PATH = ValuableForceload.getInstance().getDataFolder().toPath().resolve("lang" + File.separator);
    private static final Locale DEFAULT_LANG = Locale.en;
    private static Map<String, Map<String, String>> cache = new HashMap<>();
    private static Locale currentLocale = Locale.getDefaultLocale();

    static {
        loadLocale(currentLocale);
    }
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        if (!cache.containsKey(locale.getLocaleName())) {
            loadLocale(locale);
        }
    }
    public static String getLocale() {
        return currentLocale.getLocaleName();
    }
    private static void loadLocale(Locale locale){
        try {
            Path path = BASE_PATH.resolve("messages_" + locale.getLocaleName() + ".json");
            InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
            if (is == null) { // Fallback to default
                path = BASE_PATH.resolve("messages_" + DEFAULT_LANG.getLocaleName() + ".json");;
                is = Files.newInputStream(path, StandardOpenOption.READ);
            }

            ObjectMapper mapper = new ObjectMapper();
            HashMap messages = mapper.readValue(is, HashMap.class);
            cache.put(locale.toString(), messages);
        } catch (Exception e) {
            throw new RuntimeException("Load i18n file failed, filename: "+ BASE_PATH.toFile() + "messages_" + locale.getLocaleName() + ".json, Error: " + e.getMessage(), e);
        }
    }
    public static String get(String key, Object... args) {
        Map<String, String> messages = cache.get(currentLocale.getLocaleName());
        String template = messages.getOrDefault(key, "["+key+"]");
        String formattedTemplate = template.replaceAll("\\{(\\d+)}","%s");
        return String.format(formattedTemplate, args);
    }

    public static String getWithPrefix(String key, Object... args) {
        String prefix = get("prefix");
        return prefix + " " + get(key, args);
    }

}
