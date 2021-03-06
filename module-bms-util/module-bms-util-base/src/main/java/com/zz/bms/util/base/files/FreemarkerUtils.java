package com.zz.bms.util.base.files;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;

/**
 * @Author liweizhi
 * @Date 2017-02-03 23:23
 */
public class FreemarkerUtils {


    /**
     * 根据模板生成文件
     * @param model 模板参数
     * @param dirPath 生成文件的路径
     * @param fileName 文件名称
     * @param ftlName 模板名称
     * @param ftlPath 模板路径
     * @throws IOException
     */
    public static void saveFile(Map<String, Object> model, String dirPath, String fileName, String ftlName, String ftlPath) throws IOException {

        Configuration cfg = FreemarkerUtils.buildConfiguration("classpath:" + ftlPath);
        Template template = cfg.getTemplate(ftlName);
        template.setEncoding("UTF-8");
        String result = FreemarkerUtils.renderTemplate(template, model);

        File dir = new File(dirPath);
        dir.mkdirs();
        String pathname = dirPath + File.separator + fileName;
        File file = new File(pathname);
        file.createNewFile();
        IOUtils.write(result, new FileOutputStream(file),"UTF-8");

    }

    /**
     * 渲染模板字符串。
     */
    public static String renderString(String templateString, Map<String, ?> model) {
        try {
            StringWriter result = new StringWriter();
            Template t = new Template("default", new StringReader(templateString), new Configuration());
            t.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 渲染Template文件.
     */
    public static String renderTemplate(Template template, Object model) {
        try {
            StringWriter result = new StringWriter();
            template.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建默认配置，设定模板目录.
     */
    public static Configuration buildConfiguration(String directory) throws IOException {
        Configuration cfg = new Configuration();
        Resource path = new DefaultResourceLoader().getResource(directory);
        cfg.setDirectoryForTemplateLoading(path.getFile());
        return cfg;
    }
}
