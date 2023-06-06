package br.com.mundim.rede.social.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.mundim.rede.social.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Documentação API Rede Social")
                .description("Este projeto foi desenvolvido como parte da disciplina de Programação Orientada a Objetos (POO) na Universidade Federal de Uberlândia, sob a orientação do Prof. Dr. José Gustavo de Souza Paiva. A disciplina de POO tem como objetivo fornecer conhecimentos teóricos e práticos sobre os conceitos fundamentais da programação orientada a objetos, bem como promover a aplicação desses conceitos em projetos práticos. O projeto em questão foi desenvolvido como uma demonstração do entendimento dos princípios e técnicas abordadas na disciplina, aplicados em um contexto específico.")
                .version("1.0.0")
                .build();
    }

}
