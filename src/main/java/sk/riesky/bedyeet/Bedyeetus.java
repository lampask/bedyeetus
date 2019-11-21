package sk.riesky.bedyeet;

import org.bukkit.plugin.java.JavaPlugin;
import sk.riesky.bedyeet.Commands.MainCommand;
import sk.riesky.bedyeet.Resources.Constants;

public final class Bedyeetus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Commands
        this.getCommand(Constants.main_command).setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
