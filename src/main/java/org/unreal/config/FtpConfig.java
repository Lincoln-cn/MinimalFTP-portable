package org.unreal.config;

import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FtpConfig {
    private static final Logger logger = Logger.getLogger(FtpConfig.class.getName());
    public static final String DEFAULT_CONFIG_PATH = "ftp-config.json";

    public int port = 21;
    public Map<String, UserConfig> users = new HashMap<>();

    public static FtpConfig load(String configFile) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(configFile)));
            JSONObject json = new JSONObject(content);

            FtpConfig config = new FtpConfig();
            config.port = json.optInt("port", 21);

            JSONArray users = json.getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                JSONObject u = users.getJSONObject(i);
                UserConfig uc = new UserConfig();
                uc.username = u.getString("username");
                uc.password = u.getString("password");
                uc.root = u.getString("root");
                uc.readonly = u.optBoolean("readonly", false);
                config.users.put(uc.username, uc);
            }
            return config;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not load config file '" + configFile + "', generating default configuration.", e);
            return createDefaultConfig(configFile);
        }
    }

    /**
     * 创建默认配置，并保存到文件
     */
    public static FtpConfig createDefaultConfig(String configFile) {
        FtpConfig config = new FtpConfig();
        config.port = 2121;
        
        // 生成随机高强度密码
        String randomPassword = generateRandomPassword(16);
        
        // 添加默认用户
        UserConfig defaultUser = new UserConfig();
        defaultUser.username = "admin";
        defaultUser.password = randomPassword;
        defaultUser.root = "./ftp-root";
        defaultUser.readonly = false;
        config.users.put(defaultUser.username, defaultUser);
        
        // 保存默认配置到文件
        saveDefaultConfig(config, configFile, randomPassword);
        
        return config;
    }
    
    /**
     * 生成随机高强度密码
     */
    private static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * 保存默认配置到文件
     */
    private static void saveDefaultConfig(FtpConfig config, String configFile, String generatedPassword) {
        try {
            JSONObject json = getJsonObject(config);

            Files.write(Paths.get(configFile), json.toString(4).getBytes(StandardCharsets.UTF_8));
            logger.info("Generated default config file: " + configFile);
            logger.info("Default user: admin");
            logger.info("Default password: " + generatedPassword);
            logger.warning("Please change the password after first login!");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not save default config file: " + e.getMessage(), e);
        }
    }

    private static JSONObject getJsonObject(FtpConfig config) {
        JSONObject json = new JSONObject();
        json.put("port", config.port);

        JSONArray users = new JSONArray();
        for (Map.Entry<String, UserConfig> entry : config.users.entrySet()) {
            UserConfig user = entry.getValue();
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.username);
            userJson.put("password", user.password);
            userJson.put("root", user.root);
            userJson.put("readonly", user.readonly);
            users.put(userJson);
        }
        json.put("users", users);
        return json;
    }

    public static class UserConfig {
        public String username;
        public String password;
        public String root;
        public boolean readonly;
    }
}