package game;

import me.drakosha.thefortressisles.TheFortressIsles;

public class ConfigManager {
    public static String getColor(String key){
        return TheFortressIsles.getInstance().getConfig().getString("colors." + key, "цвета нету");
    }
    public static int getPriceUpdate(int key){
        String keyValue = String.valueOf(key);
        return TheFortressIsles.getInstance().getConfig().getInt("updatePrice." + keyValue , 0);
    }
}
