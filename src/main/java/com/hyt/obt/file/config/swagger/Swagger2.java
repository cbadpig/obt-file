package com.hyt.obt.file.config.swagger;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置
 * @author liuq
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableSwagger2
public class Swagger2 {
    
    /*
	    @Api：用在类上，说明该类的作用
	    @ApiOperation：用在方法上，说明方法的作用
	    @ApiImplicitParams：用在方法上包含一组参数说明
	    @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
	        paramType：参数放在哪个地方
	            header-->请求参数的获取：@RequestHeader
	            query-->请求参数的获取：@RequestParam
	            path（用于restful接口）-->请求参数的获取：@PathVariable
	            body（不常用）
	            form（不常用）
	        name：参数名
	        dataType：参数类型
	        required：参数是否必须传
	        value：参数的意思
	    defaultValue：参数的默认值
	    @ApiResponses：用于表示一组响应
	    @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
	        code：数字，例如400
	        message：信息，例如"请求参数没填好"
	        response：抛出异常的类
	    @ApiModel：描述一个Model的信息（这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
	    @ApiModelProperty：描述一个model的属性
    */
    /**
     * Api配置
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hyt"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * 页面显示的配置
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("海云天面试系统")
                .description("海云天文件服务数据接口")
                .termsOfServiceUrl("http://www.seaskylight.com")
                .contact(new Contact("海云天研发项目组", "http://www.seaskylight.com", "liuq@seaskylight.com"))
                .version("1.0")
                .build();
    }

}
