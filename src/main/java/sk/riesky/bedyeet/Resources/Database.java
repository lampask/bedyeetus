package sk.riesky.bedyeet.Resources;

import org.bukkit.configuration.file.FileConfiguration;
import sk.riesky.bedyeet.Bedyeetus;
import sk.riesky.bedyeet.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database {

    private Connection connection;
    private String host, database, username, password;
    private int port;
    private Config config;
    private Logger log;

    //region Singleton
    private static Database db;
    public  static Database getInstance() {
        if (db == null) {
            db = new Database();
        }
        return db;
    }
    //endregion

    public void init(Bedyeetus main) {
        this.config = Config.getInstance();
        this.host = this.config.get().getString("host");
        this.database = this.config.get().getString("database");
        this.username = this.config.get().getString("username");
        this.password = this.config.get().getString("password");
        this.port = this.config.get().getInt("port");
        this.log = main.getLogger();
        this.log.info("Database initialized");
        try {
            this.establishConnection();
            this.log.info("Connection established!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void establishConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
        }

    }

    public Connection get() {
        return this.connection;
    }
}
