package com.xnew.kaleidoscope.dbgen.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.xnew.kaleidoscope.dbgen.config.XnewPackageConfig;
import com.xnew.kaleidoscope.dbgen.entity.DBMsg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MpGenerator
 * @Description TODO
 * @Author tinx
 * @Date 2019/5/21 0021 下午 4:32
 * @Version 1.0
 **/
public class MpGenerator {

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        DBMsg dbMsg = new DBMsg();
        String url = "jdbc:mysql://127.0.0.1:3306/shangjia?characterEncoding=utf8";
        String driver= "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "mysql123!@#";
        String proName = "shangjia";
        String moduleName = "shop";
        String sql ="create table `x_tag`(" +
                "`id` INT UNSIGNED NOT NULL  AUTO_INCREMENT comment '唯一标识'," +
                "`status` TINYINT NOT NULL comment '状态'," +
                "`tag_type` TINYINT NOT NULL comment '类型'," +
                "`create_time` TIMESTAMP NOT NULL comment '创建时间'," +
                "`create_user` CHAR(50) NOT NULL comment '创建人'," +
                "`update_time` TIMESTAMP NULL comment '修改时间'," +
                "`update_user` CHAR(50) NULL comment '修改人'," +
                "PRIMARY KEY (`id`)" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';";
        dbMsg.setUrl(url);
        dbMsg.setDriver(driver);
        dbMsg.setUser(username);
        dbMsg.setPwd(password);
        dbMsg.setDatabasename(proName);
        dbMsg.setModel(moduleName);
        dbMsg.setSql(sql);
        dbMsg.setTablename("x_tag");
        List<String> sw = new ArrayList<>();
        sw.add("status");
        sw.add("tag_type");
        dbMsg.setSw(sw);
        List<String> enums = new ArrayList<>();
        enums.add("status");
        enums.add("tag_type");
        dbMsg.setEnums(enums);
        List<String> enumsType = new ArrayList<>();
        enumsType.add("status=USED|1|已使用+TO_BE_USED|0|待使用");
        enumsType.add("tag_type=IMPORTANT|1|重要+NORMAL|0|正常");
        dbMsg.setEnumsType(enumsType);
        generator(dbMsg);

    }


    public static void generator(DBMsg dbMsg){
        String rootPath = "C://code-gen//"+dbMsg.getDatabasename()+"//"+dbMsg.getModel();
        String packageParent = String.format("com.xnew.%s.%s",dbMsg.getDatabasename(),dbMsg.getModel());
        AutoGenerator mpg = new AutoGenerator();

        Map<String,String> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(dbMsg.getEnumsType())){
            for(String type:dbMsg.getEnumsType()){
                String[] arrType = type.split("=");
                map.put(arrType[0],arrType[1]);
            }
        }
        // 选择 freemarker 引擎，默认 Veloctiy
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(rootPath);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        //gc.setKotlin(true);//是否生成 kotlin 代码
        gc.setAuthor("tinx");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });
        if(StringUtils.isBlank(dbMsg.getDriver())){
            dbMsg.setDriver("com.mysql.jdbc.Driver");
        }
        dsc.setDriverName(dbMsg.getDriver());
        dsc.setUsername(dbMsg.getUser());
        dsc.setPassword(dbMsg.getPwd());
        dsc.setUrl(dbMsg.getUrl());
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });// 此处可以修改为您的表前缀
        strategy.setTablePrefix(new String[] { "x_" });
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[]{dbMsg.getTablename()}); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
//         strategy.setSuperControllerClass("com.xnew.common.abs.controller.XnewAbstractController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        strategy.setEntityColumnConstant(false);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        //strategy.setEntityBuilderModel(true);
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        XnewPackageConfig pc = new XnewPackageConfig();
        pc.setParent(packageParent);
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEnumsName("enum");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】  ${cfg.abc}

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                map.put("root",dbMsg.getDatabasename());
                map.put("noPk",false);
                map.put("searchWord",dbMsg.getSw());
                map.put("enums",parseList(dbMsg.getEnums()));
                map.put("enumMap",parseMap(dbMsg.getEnums()));
                this.setMap(map);
            }
        };

        // 自定义 xxListIndex.html 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();

        focList.add(new FileOutConfig("/templates/ftl/view.html.vm") {

            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//templates//%s//%s//view.html",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/edit.html.vm") {

            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//templates//%s//%s//edit.html",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });


        focList.add(new FileOutConfig("/templates/ftl/edit.js.vm") {

            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//static//%s//js//%s//edit.js",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/index.html.vm") {

            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//templates//%s//%s//index.html",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });


        focList.add(new FileOutConfig("/templates/ftl/index.js.vm") {

            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//static//%s//js//%s//index.js",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });


        // 自定义  xxAdd.html 生成
        focList.add(new FileOutConfig("/templates/ftl/add.html.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//templates//%s//%s//add.html",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });


        focList.add(new FileOutConfig("/templates/ftl/add.js.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//resources//static//%s//js//%s//add.js",rootPath,dbMsg.getModel(),dbMsg.getDatabasename(),tableInfo.getEntityPath());
            }
        });





        focList.add(new FileOutConfig("/templates/ftl/controller.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                return String.format("%s//%s-console-backend//src//main//java//%s//controller//%sController.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/serviceImpl.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                return String.format("%s//%s-implecation//src//main//java//%s//service//impl//%sServiceImpl.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                return String.format("%s//%s-implecation//src//main//java//%s//mapper//%sMapper.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/service.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                return String.format("%s//%s-interface//src//main//java//%s//service//%sService.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                return String.format("%s//%s-interface//src//main//java//%s//entity//%s.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });

        focList.add(new FileOutConfig("/templates/ftl/entityVo.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                cfg.getMap().put("pkg",packageParent);
                return String.format("%s//%s-interface//src//main//java//%s//entity//vo//%sVo.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });


        focList.add(new FileOutConfig("/templates/ftl/entityQuery.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String pkg = packageParent.replace(".","//");
                // 自定义输入文件名称
                return String.format("%s//%s-interface//src//main//java//%s//entity//query//%sQuery.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName());
            }
        });

        List<String> enums = dbMsg.getEnums();
        for(String str:enums){
            focList.add(new FileOutConfig("/templates/ftl/enum.java.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    String pkg = packageParent.replace(".","//");
                    // 自定义输入文件名称
                    String enumName = String.format("%s%sEnum",tableInfo.getEntityName(),parse(str));
                    System.out.println("enumName:"+enumName);
                    cfg.getMap().put("enumName",enumName);
                    String types = map.get(str);
                    System.out.println("types:"+types);
                    String[] arrTypes = types.split("\\+");
                    List<String> enumTypes = new ArrayList<>();
                    for(String t:arrTypes){
                        enumTypes.add(t);
                    }
                    cfg.getMap().put("types",enumTypes);
                    cfg.getMap().put("pkg",packageParent);
                    return String.format("%s//%s-interface//src//main//java//%s//enums//%s%sEnum.java",rootPath,dbMsg.getModel(),pkg,tableInfo.getEntityName(),parse(str));
                }
            });
        }

//        focList.add(new FileOutConfig("/templates/ftl/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                String pkg = packageParent.replace(".","//");
//                // 自定义输入文件名称
//                return String.format("%s//implecation//%s//mapper//xml//%sMapper.xml",rootPath,pkg,tableInfo.getEntityName());
//            }
//        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        //  自定义 xxUpdate.html生成
//        focList.add(new FileOutConfig("/templates/ftl/update.html.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "C://test//html//" + tableInfo.getEntityName() + "Update.html";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        /*TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);*/

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
//        TemplateConfig tc = new TemplateConfig();
//        tc.setController("/templates/ftl/controller.java.vm");
//        tc.setService("/templates/ftl/service.java.vm");
//        tc.setServiceImpl("/templates/ftl/serviceImpl.java.vm");
//        tc.setEntity("/templates/ftl/entity.java.vm");
//        tc.setMapper("/templates/ftl/mapper.java.vm");
//        tc.setXml("/templates/ftl/mapper.xml.vm");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
//        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

    public static List<String> parseList(List<String> enums){
        List<String> enumsList = new ArrayList<>();
        for(String e:enums){
            enumsList.add(parse(e));
        }
        return enumsList;
    }

    /**
     * 把字段如：tag_type ,按照key:TagType,value:tagType保存
     * @param enums
     * @return
     */
    public static Map<String,String> parseMap(List<String> enums){
        Map<String,String> map = new HashMap<>();
        for(String e:enums){
            map.put(parse(e),parseToField(e));
        }
        return map;
    }

    /**
     * 把字段如tag_type改成tagType
     * @param field
     * @return
     */
    public static String parseToField(String field){
        String className = "";
        if(field.indexOf("_")>0){
            String[] arrField = field.split("_");
            for(int i = 0;i < arrField.length; i++){
                String str = arrField[i];
                if(i==0){
                    className = str;
                }else{
                    className+=str.substring(0,1).toUpperCase()+str.substring(1,str.length());
                }

            }
        }else{
            className = field;
        }
        return className;
    }

    /**
     * 把字段如tag_type改成TagType
     * @param field
     * @return
     */
    public static String parse(String field){
        String className = "";
        if(field.indexOf("_")>0){
            String[] arrField = field.split("_");
            for(int i = 0;i < arrField.length; i++){
                String str = arrField[i];
                className+=str.substring(0,1).toUpperCase()+str.substring(1,str.length());
            }
        }else{
            className+=field.substring(0,1).toUpperCase()+field.substring(1,field.length());
        }
        return className;
    }
}
