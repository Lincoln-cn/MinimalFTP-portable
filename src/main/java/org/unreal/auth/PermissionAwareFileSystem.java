package org.unreal.auth;

import com.guichaguri.minimalftp.impl.NativeFileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class PermissionAwareFileSystem extends NativeFileSystem {
    private static final Logger logger = Logger.getLogger(PermissionAwareFileSystem.class.getName());

    private final boolean readOnly;
    private final File root;

    public PermissionAwareFileSystem(File root, boolean readOnly) {
        super(root);
        this.readOnly = readOnly;
        try {
            this.root = root.getCanonicalFile();
        } catch (IOException e) {
            logger.severe("Failed to initialize filesystem: " + e.getMessage());
            logger.throwing(PermissionAwareFileSystem.class.getName(), "PermissionAwareFileSystem", e);
            throw new RuntimeException("Failed to initialize filesystem", e);
        }
    }

    /**
     * 检查文件是否在允许的根目录内
     */
    private void checkAccess(File file) throws IOException {
        File canonicalFile = file.getCanonicalFile();
        if (!canonicalFile.getPath().startsWith(root.getPath())) {
            throw new IOException("Access denied: File is outside the allowed root directory");
        }
    }

    /**
     * 检查是否允许写操作
     */
    private void checkWritePermission() throws IOException {
        if (readOnly) {
            throw new IOException("Write operation not allowed in read-only mode");
        }
    }

    @Override
    public File findFile(String path) throws IOException {
        File file = super.findFile(path);
        checkAccess(file);
        return file;
    }

    @Override
    public File findFile(File base, String path) throws IOException {
        File file = super.findFile(base, path);
        checkAccess(file);
        return file;
    }

    @Override
    public InputStream readFile(File file, long offset) throws IOException {
        checkAccess(file);
        return super.readFile(file, offset);
    }

    @Override
    public OutputStream writeFile(File file, long offset) throws IOException {
        checkAccess(file);
        checkWritePermission();
        return super.writeFile(file, offset);
    }

    @Override
    public void mkdirs(File file) throws IOException {
        checkAccess(file);
        checkWritePermission();
        super.mkdirs(file);
    }

    @Override
    public void delete(File file) throws IOException {
        checkAccess(file);
        checkWritePermission();
        super.delete(file);
    }

    @Override
    public void rename(File from, File to) throws IOException {
        checkAccess(from);
        checkAccess(to);
        checkWritePermission();
        super.rename(from, to);
    }

    @Override
    public void chmod(File file, int perms) throws IOException {
        checkAccess(file);
        checkWritePermission();
        super.chmod(file, perms);
    }

    @Override
    public void touch(File file, long time) throws IOException {
        checkAccess(file);
        checkWritePermission();
        super.touch(file, time);
    }
}