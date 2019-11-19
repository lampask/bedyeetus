package sk.riesky.bedyeet;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import sk.riesky.bedyeet.Commands.MainCommand;
import sk.riesky.bedyeet.Events.Selector;
import sk.riesky.bedyeet.Resources.Constants;

import net.minecraft.server.v1_13_R2.SharedConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Bedyeetus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Modify SharedConstants so special characters can be displayed
        try {
            //modifyAllowedCharacters(Constants.CHARS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Commands
        this.getCommand(Constants.main_command).setExecutor(new MainCommand(this));
        // Events
        this.getServer().getPluginManager().registerEvents(new Selector(this), this);
    }

    /*public void modifyAllowedCharacters(String allow_chars) throws Exception{
        String pack = Bukkit.getServer().getClass().getName();
        pack = (pack = pack.substring(0, pack.lastIndexOf("."))).substring(pack.lastIndexOf(".") + 1, pack.length());
        Class<?> c = getClass().getClassLoader().loadClass("net.minecraft.server." + pack + ".SharedConstants");
        Field f = c.getDeclaredField("allowedCharacters");
        f.setAccessible(true);
        Field mod = Field.class.getDeclaredField("modifiers");
        mod.setAccessible(true);
        mod.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        String s = String.valueOf(f.get(null));
        s += allow_chars;
        f.set(null, s);
    }*/

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
