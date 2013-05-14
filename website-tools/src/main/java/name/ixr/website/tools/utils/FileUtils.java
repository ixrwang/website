package name.ixr.website.tools.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 文件操作相关的工具包
 * 
 * @author IXR
 */
public class FileUtils{

    /**
     * 获取所有包的所有资源文件，支持表达式
     * 
     * @param locationPattern
     *            例如 ： classpath:spring/*.xml 按优先级加载 
     *                classpath*:spring/*.xml 对所有jar扫描
     * @return the corresponding Resource objects
     * @throws IOException
     *             in case of I/O errors
     */
    public static Resource[] getResources(String locationPattern) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(locationPattern);
        return resources;
    }
    
    
    /**
     * 获取所有包的所有资源文件，支持表达式
     * 
     * @param locationPattern
     *            例如 ： classpath:spring/*.xml 按优先级加载 
     *                classpath*:spring/*.xml 对所有jar扫描
     * @return the corresponding Resource object
     * @throws IOException
     *             in case of I/O errors
     */
    public static Resource getResource(String locationPattern) throws IOException {
        Resource[] resources = getResources(locationPattern);
        if(resources.length > 0){
            return resources[0];
        } else {
            return null;
        }
    }
    
    /**
     * 获取文件夹下所有的文件列表
     * 
     * @param directory
     *            文件目录
     * @return 目录下所有的文件列表
     */
    public static List<File> fiandDirectoryFiles(File directory) {
        List<File> files = new ArrayList<File>();
        if (directory.isDirectory()) {
            File[] dirFiles = directory.listFiles();
            for (int i = 0; i < dirFiles.length; i++) {
                File file = dirFiles[i];
                if (file.isDirectory()) {
                    files.addAll(fiandDirectoryFiles(file));
                } else {
                    files.add(file);
                }
            }
        } else {
            files.add(directory);
        }
        return files;
    }
}
