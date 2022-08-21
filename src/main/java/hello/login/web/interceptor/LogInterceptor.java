package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);
        /**
         * uuid:어떤 놈이 요청했는지
         * requestURI : 어디서 요청이 온건지
         * hander : 어떤 핸들러로갈지
         */

        /**
         * @RequestMapping : HandlerMethod
         * 정적리소스 : ResourceHttpRequestHandler
         */
        if (handler instanceof HandlerMethod) { //@RequestMapping을 할때 HandlerMethod가 사용됨
            HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든정보가 포함되어있다.

        }
        log.info("REQUEST [{}][{}][{}]",uuid,requestURI,handler);
        return true; //true를 해줘야 다음 인터셉터나 컨트롤러가 실행이됨
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle[{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]",logId,requestURI,handler);
        if (ex != null) {
            log.error("afterCompletion error!!",ex);//오류는{}안넣어도됨
        }

    }
}
