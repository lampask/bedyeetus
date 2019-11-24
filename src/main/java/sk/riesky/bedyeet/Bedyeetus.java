package sk.riesky.bedyeet;

import org.bukkit.plugin.java.JavaPlugin;
import sk.riesky.bedyeet.Commands.Join;
import sk.riesky.bedyeet.Commands.Leave;
import sk.riesky.bedyeet.Commands.MainCommand;
import sk.riesky.bedyeet.Resources.Config;
import sk.riesky.bedyeet.Resources.Constants;
import sk.riesky.bedyeet.Resources.Database;


public final class Bedyeetus extends JavaPlugin {

    @Override
    public void onEnable() {
        Config.getInstance().init(this);
        //Database.getInstance().init(this);
        // Commands
        this.getCommand(Constants.main_command).setExecutor(new MainCommand(this));
        this.getCommand("join").setExecutor(new Join());
        this.getCommand("leave").setExecutor(new Leave());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic7
        for (Game g: Manager.getInstance().getGames().values()) {
            g.delete();
        }

    }
}
