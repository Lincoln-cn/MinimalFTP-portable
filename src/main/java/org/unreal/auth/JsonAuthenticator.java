package org.unreal.auth;
import com.guichaguri.minimalftp.api.IFileSystem;
import com.guichaguri.minimalftp.api.IUserAuthenticator;
import com.guichaguri.minimalftp.FTPConnection;
import org.unreal.config.FtpConfig;

import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * 实现根据FtpConfig 类中的配置进行用户认证
 */
public class JsonAuthenticator implements IUserAuthenticator {
    private static final Logger logger = Logger.getLogger(JsonAuthenticator.class.getName());
    private final FtpConfig config;

    public JsonAuthenticator(FtpConfig config) {
        this.config = config;
    }


    @Override
    public boolean needsUsername(FTPConnection con) {
        return true;
    }

    @Override
    public boolean needsPassword(FTPConnection con, String username, InetAddress host) {
        return true;
    }

    @Override
    public IFileSystem authenticate(FTPConnection con, InetAddress host, String username, String password) throws AuthException {
        // 查找用户配置
        FtpConfig.UserConfig userConfig = config.users.get(username);
        
        // 如果用户不存在，抛出认证异常
        if (userConfig == null) {
            logger.warning("Authentication failed for non-existent user: " + username);
            throw new AuthException();
        }
        
        // 验证密码
        if (!userConfig.password.equals(password)) {
            logger.warning("Authentication failed for user: " + username + " due to incorrect password");
            throw new AuthException();
        }
        
        // 记录成功的认证尝试
        logger.info("User " + username + " authenticated successfully");
        
        // 创建用户的根目录文件对象
        File rootDir = new File(userConfig.root);
        
        // 确保根目录存在
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        
        // 返回权限感知的文件系统实例
        return new PermissionAwareFileSystem(rootDir, userConfig.readonly);
    }
}