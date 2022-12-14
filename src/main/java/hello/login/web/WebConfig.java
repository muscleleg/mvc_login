package hello.login.web;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error");
        registry.addInterceptor(new LogInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/add/","/login","/logout","/css/**","/*.ico","/error");
    }

    //@Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());//web.filter.LogFilter.class
        filterRegistrationBean.setOrder(1);//순서 정해주는것
        filterRegistrationBean.addUrlPatterns("/*");//어떤 url패턴으로 할거냐,모든 url에 적용
        return filterRegistrationBean;
    }
   // @Bean
    public FilterRegistrationBean loginCheckFitler(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());//web.filter.LogFilter.class
        filterRegistrationBean.setOrder(2);//순서 정해주는것
        filterRegistrationBean.addUrlPatterns("/*");//어떤 url패턴으로 할거냐,모든 url에 적용
        return filterRegistrationBean;
    }
}
