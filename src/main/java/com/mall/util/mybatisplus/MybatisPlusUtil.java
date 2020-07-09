package com.mall.util.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @Author lijun
 * @Date 2020-05-19 19:40
 * @Description  数据库代码反向生成工具类
 * @Since version-1.0
 */
public class MybatisPlusUtil {
    public static void main(String[] args) {
        AutoGenerator gen = new AutoGenerator();

        //mybatis全局配置信息
        GlobalConfig gc = new GlobalConfig();
        //文件输出目录
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java/com/mall");

        //是否打开文件夹
        gc.setOpen(false);
        //是否覆盖已有文件
        gc.setFileOverride(true);
        //开启baseResultMap
        gc.setBaseResultMap(true);
        //开启baseColumnlist
        gc.setBaseColumnList(true);
        //自定义文件名，会以%s自动填充表实体属性
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        gen.setGlobalConfig(gc);


        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://111.229.16.103:3306/mall");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("4311");
        gen.setDataSource(dsc);

        //包配置
        PackageConfig pc = new PackageConfig();
        /*  pc.setModuleName("test");*/
        pc.setParent("com.mall");
        gen.setPackageInfo(pc);

        //策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setTablePrefix("mmall_");
        //表名生成策略
        sc.setNaming(NamingStrategy.underline_to_camel);
        //列名生成策略
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
       /* sc.setNaming(NamingStrategy.underline_to_camel);//表名生成策略
        sc.setColumnNaming(NamingStrategy.underline_to_camel);//列名生成策略
        sc.setInclude("TEST");//要反向生成的表名*/
        gen.setStrategy(sc);

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        gen.setCfg(cfg);

        //执行生成
        gen.execute();
    }

}
