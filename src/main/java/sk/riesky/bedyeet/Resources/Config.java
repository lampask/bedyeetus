package sk.riesky.bedyeet.Resources;

import org.bukkit.configuration.file.FileConfiguration;
import sk.riesky.bedyeet.Bedyeetus;

public class Config {
    private FileConfiguration config;

    //region Singleton
    private static Config cfg;
    public  static Config getInstance() {
        if (cfg == null) {
            cfg = new Config();
        }
        return cfg;
    }
    //endregion

    public void init(Bedyeetus main) {
        main.saveDefaultConfig();
        this.config = main.getConfig();
        main.getLogger().info("Configuration initialised");

    }

    public FileConfiguration get() {
        return this.config;
    }
}
