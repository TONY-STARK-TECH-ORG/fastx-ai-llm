package com.fastx.ai.llm.domains.tools;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author stark
 */
public class ModelGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create(
                "jdbc:mysql://localhost:3306/fast-llm?useSSL=false&serverTimezone=UTC",
                        "root",
                        "123456")
                .globalConfig(builder -> builder
                        .author("stark")
                        .outputDir("~/Downloads" + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                )
                .packageConfig(builder -> builder
                        .parent("com.fastx.ai.llm.domains")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addTablePrefix("t_")
                        .entityBuilder()
                        .enableLombok()
                        .logicDeleteColumnName("deleted")
                        .addSuperEntityColumns("deleted", "id", "created_by", "create_time", "updated_by", "update_time")
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
