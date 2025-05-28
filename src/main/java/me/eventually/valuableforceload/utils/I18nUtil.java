package me.eventually.valuableforceload.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.structure.Locale;
import org.jetbrains.annotations.NotNull;

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

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setLocale(@NotNull Locale locale) {
        currentLocale = locale;
        if (!cache.containsKey(locale.getLocaleName())) {
            loadLocale(locale);
        }
    }
    public static String getLocale() {
        return currentLocale.getLocaleName();
    }
    @SuppressWarnings("unchecked")
    public static void loadLocale(Locale locale){
        try {
            Path path = BASE_PATH.resolve("messages_" + locale.getLocaleName() + ".json");
            InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
            if (is == null) { // Fallback to default
                path = BASE_PATH.resolve("messages_" + DEFAULT_LANG.getLocaleName() + ".json");;
                is = Files.newInputStream(path, StandardOpenOption.READ);
            }

            ObjectMapper mapper = new ObjectMapper();
            // messages from current locale
            HashMap messages = mapper.readValue(is, HashMap.class);

            // check if current locale is missing keys
            boolean isMissingKeys = false;

            Path tempPath = BASE_PATH.resolve("temp/messages_" + locale.getLocaleName() + ".json");
            if (!tempPath.toFile().exists()) {
                throw new IllegalStateException("Temp file should be created before this method is called.");
            }
            InputStream tempIs = Files.newInputStream(tempPath, StandardOpenOption.READ);
            HashMap<String, String> tempMessages = (HashMap<String, String>) mapper.readValue(tempIs, HashMap.class);
            for (String key : tempMessages.keySet()) {
                if (!messages.containsKey(key)) {
                    isMissingKeys = true;
                    messages.put(key, tempMessages.get(key));
                }
            }
            if (isMissingKeys) {
                ValuableForceload.getInstance().getLogger().severe("Some keys are missing in locale file, plugin will try to fix them automatically, if error occurs, please do them manually.");
                Files.write(path, mapper.writeValueAsBytes(messages), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            }
            cache.put(locale.toString(), messages);


        } catch (Exception e) {
            throw new RuntimeException("Load i18n file failed, filename: "+ BASE_PATH.toFile() + "messages_" + locale.getLocaleName() + ".json, Error: " + e.getMessage(), e);
        }
    }
    public static @NotNull String get(String key, Object... args) {
        Map<String, String> messages = cache.get(currentLocale.getLocaleName());
        String template = messages.getOrDefault(key, "["+key+"] if you see this, the language file is outdated! Please contact server admin to recreate them.");
        String formattedTemplate = template.replaceAll("\\{(\\d+)}","%s");
        return String.format(formattedTemplate, args);
    }
    public static @NotNull String getWithPrefix(String key, Object... args) {
        String prefix = get("prefix");
        return prefix + " " + get(key, args);
    }

}
