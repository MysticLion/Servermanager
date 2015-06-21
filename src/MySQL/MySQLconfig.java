package MySQL;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MySQLconfig {

	public void setStandard(){
		FileConfiguration cfg = getFileConfiguration();
		cfg.options().copyDefaults(true);
		cfg.addDefault("host", "host");
		cfg.addDefault("port", "port");
		cfg.addDefault("username", "username");
		cfg.addDefault("password", "password");
		cfg.addDefault("database", "database");
		try {
			cfg.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private File getFile() {
		return new File("plugins/ServerManager", "mysql.yml");
	}
	private FileConfiguration getFileConfiguration(){
		return YamlConfiguration.loadConfiguration(getFile());
	}
	public void readData(){
		FileConfiguration cfg = getFileConfiguration();
		MySQL.host = cfg.getString("host");
		MySQL.port = cfg.getString("port");
		MySQL.username = cfg.getString("username");
		MySQL.password = cfg.getString("password");
		MySQL.database = cfg.getString("database");
	}
}
