package nl.colin.s3.beeple.config;

import nl.colin.s3.beeple.util.JwtAuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    public WebConfig(JwtAuthInterceptor jwtAuthInterceptor) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor).addPathPatterns("/playlist/**", "/users/**", "/songs/**");
    }

    @Value("${Beeple-fe.allowedUrl}")
    private String corsHeader;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] corsHeaders = new String[1];
        corsHeaders[0] = corsHeader;

        registry.addMapping("/**")
                .allowedOrigins(corsHeaders)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
