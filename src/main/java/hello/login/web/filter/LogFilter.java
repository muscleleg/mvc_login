package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    /**
     *ServletRequest가 HttpServletRequest로 변환되는이유는
     *애초에 데이터가 들어온다면 HttpServletRequest로 들어왔을거임
     * 따라서 ServletRequest request = new HttpServletRequest()로 생성되어 들어왔을거고
     * 이를 다운캐스팅해서 HttpServletRequest httpRequest = (HttpServletRequest) request로 가져오는건 문제가 안됨
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();//요청온것을 구분하기위해
        try{
            log.info("REQUEST[{}][{}]",uuid,requestURI);
            chain.doFilter(request,response) ;//다음필터로 넘겨주는것
        }catch(Exception e) {
            throw e;
        }finally{
            log.info("RESPONSE[{}][{}]",uuid,requestURI);
        }


    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }

}
