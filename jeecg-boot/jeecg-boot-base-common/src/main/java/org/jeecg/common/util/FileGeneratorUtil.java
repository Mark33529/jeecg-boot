package org.jeecg.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 代码文件生成工具类
 * @Author: Leo He
 * @Date:   2019-06-19
 * @Version: V1.0
 */
public class FileGeneratorUtil {

    public static String bussiPackage = "";

    public static String entityPackage = "";

    public static String entityName = "";

    public static JSONArray columns = null;

    public static String tableName = "";

    public static String description = "";


    /**
     * 返回已生成的代码文件数量
     *
     * @param bussiPackageName
     * @param entityPackageName
     * @param entityname
     * @return
     */
    public static int generateCodes(String bussiPackageName, String entityPackageName, String entityname,
                                    String templatePath, String codePath, String templateName,
                                    String tName, String desc, JSONArray columnArray) {
        try {
            //step 1 初始化变量
            bussiPackage = bussiPackageName;

            entityPackage = entityPackageName;

            entityName = entityname;

            tableName = tName;

            description = desc;

            columns = columnArray;

            //step 2 获取业务目录
            String[] packages = getPackageName(templateName);

            //step 3 生成指定路径下的代码目录及返回代码目录路径
            codePath = generatePackage(codePath, packages);

            //step 4 获取模版目录路径
            templatePath = getPackageName(templatePath, templateName);

            //step 5 遍历模版下的模版文件，进行生成代码文件
            generateEntityPackageAndCodeFile(templatePath, codePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取业务代码目录
     *
     * @param packagePath
     * @return
     */
    public static String[] getPackageName(String packagePath) {
        //业务目录
        packagePath = ParserUtils.parse0(packagePath, bussiPackage, entityPackage);

        String[] packages = packagePath.split("\\.");

        return packages;
    }

    /**
     * 获取模版目录
     *
     * @param template
     * @param packagePath
     * @return
     */
    public static String getPackageName(String template, String packagePath) {
        String[] packages = packagePath.split("\\.");
        if (packages != null && packages.length > 0) {
            StringBuffer templatePath = new StringBuffer(template);
            for (String packageName : packages) {
                templatePath = templatePath.append(packageName).append("/");
            }
            return templatePath.toString();
        }
        return null;
    }


    /**
     * 生成指定路径下的代码目录及返回代码目录路径
     *
     * @param code
     * @param packages
     * @return
     */
    public static String generatePackage(String code, String[] packages) {
        if (packages != null && packages.length > 0) {
            StringBuffer codePath = new StringBuffer(code);
            for (String packageName : packages) {
                codePath = codePath.append(packageName).append("/");
                checkFileAndCreate(codePath.toString());
            }
            return codePath.toString();
        }
        return null;
    }

    /**
     *
     * 遍历模版下的模版文件，进行生成代码文件
     *
     * @param templatePath
     * @param codePath
     * @return
     */
    public static String generateEntityPackageAndCodeFile(String templatePath, String codePath) {
        File file = new File(templatePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                if (subFile.isDirectory()) {
                    //如果下级为文件夹，则再次遍历
                    File codeFile = new File(codePath + "/" + ParserUtils.parse0(subFile.getName(), entityName));
                    if(!codeFile.exists()) {
                        codeFile.mkdir();
                    }
                    generateEntityPackageAndCodeFile(subFile.getPath(), codeFile.getPath());
                } else {
                    String filename = subFile.getName();
                    generateCodeFile(templatePath, codePath, filename, ParserUtils.parse0(filename, entityName));
                }
            }
        }
        return "success";
    }


    public static String generateCodeFile(String packagePath, String codePath, String filename, String codeFileName) {
        try {
            File codeFolder = new File(codePath);

            if (!codeFolder.exists()) {

                codeFolder.mkdir();

            }

            //获取模版中需要的变量值
            Map map = generateDataMap();

            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);

            cfg.setDirectoryForTemplateLoading(new File(packagePath));

            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28));

            Template temp = cfg.getTemplate(filename);

            File file = new File(codeFolder + "/" + convertFilename(codeFileName));

            FileWriter fw = new FileWriter(file);

            BufferedWriter bw = new BufferedWriter(fw);

            temp.process(map, bw);

            bw.flush();

            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 将模版文件名转为实际代码文件名
     *
     * @param fileName
     * @return
     */
    public static String convertFilename(String fileName) {
        if(StringUtils.isBlank(fileName)) {
            return "";
        }
        StringBuffer prefix = new StringBuffer(fileName.substring(0, fileName.lastIndexOf(".")));
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        return prefix.append(suffix.replaceAll("i", "")).toString();
    }

    /**
     *
     * @return
     */
    public static Map<String, Object> generateDataMap() {
        Map<String, Object> dataMap = new HashMap<String, Object>();

        dataMap.put("bussiPackage", bussiPackage);
        dataMap.put("entityPackage", entityPackage);
        dataMap.put("entityName", entityName);
        dataMap.put("tableName", tableName);

        JSONObject tableVo = new JSONObject();
        tableVo.put("searchFieldNum", 0);
        tableVo.put("ftlDescription", description);
        dataMap.put("tableVo", tableVo);

        for(int i = 0; i < columns.size(); i ++) {
            JSONObject column = columns.getJSONObject(i);
            String primary = column.getString("primary");
            String fieldType = mappingType(column.getString("fieldDbType"));
            if(StringUtils.isNotBlank(primary) && primary.equals("Y")) {
                fieldType = "Long";
            }
            column.put("fieldType", fieldType);
        }
        dataMap.put("columns", columns);

        return dataMap;
    }

    /**
     * 获取java类与数据库表字段对应的类型
     *
     * @param fieldDbType
     * @return
     */
    public static String mappingType(String fieldDbType) {
        String fieldType = "String";

        if(fieldDbType.equals("varchar")) {

            fieldType = "String";

        }else if(fieldDbType.equals("int")) {

            fieldType = "Integer";

        }else if(fieldDbType.equals("bigint")) {

            fieldType = "BigInteger";

        }else if(fieldDbType.equals("bollean")) {

            fieldType = "Boolean";

        }else if(fieldDbType.equals("tinyint")) {

            fieldType = "Integer";

        }else if(fieldDbType.equals("text")) {

            fieldType = "String";

        }else if(fieldDbType.equals("timestamp")) {

            fieldType = "java.util.Date";

        }else if(fieldDbType.equals("BigDecimal")) {

            fieldType = "java.math.BigDecimal";

        }else if(fieldDbType.equals("date")) {

            fieldType = "java.util.Date";

        }

        return fieldType;
    }

    /**
     * 检查文件是否存在，不存在则创建
     *
     * @param path
     */
    public static void checkFileAndCreate(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
    }

    public static void main(String[] args) {

        String bussiPackageName = "demoBussiPackageName";
        String entityPackageName = "demoEntityPackageName";
        String entityname = "Demo";
        String templatePath = "/Users/leohe/git/jeecg-boot/jeecg-boot/jeecg-boot-module-system/src/main/resources/skyline/code-template/";
        String codePath = "/Users/leohe/git/jeecg-boot/jeecg-boot/jeecg-boot-module-system/src/main/java/";
        String templateName = "com.springboot.cloud.${bussiPackage}.${entityPackage}";
        String tName = "t_demo";
        String desc = "t_demo desc";
        JSONArray columnArray = new JSONArray();

        System.out.println(generateCodes(bussiPackageName, entityPackageName, entityname,
                templatePath, codePath, templateName, tName, desc, columnArray));

    }
}