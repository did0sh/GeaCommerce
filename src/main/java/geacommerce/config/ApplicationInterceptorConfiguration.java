package geacommerce.config;

import geacommerce.interceptors.SessionTimerInterceptor;
import geacommerce.interceptors.TitleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationInterceptorConfiguration implements WebMvcConfigurer {

    private final TitleInterceptor titleInterceptor;
    private final SessionTimerInterceptor sessionTimerInterceptor;

    @Autowired
    public ApplicationInterceptorConfiguration(TitleInterceptor titleInterceptor, SessionTimerInterceptor sessionTimerInterceptor) {
        this.titleInterceptor = titleInterceptor;
        this.sessionTimerInterceptor = sessionTimerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.titleInterceptor);
        registry.addInterceptor(this.sessionTimerInterceptor);
    }
}
