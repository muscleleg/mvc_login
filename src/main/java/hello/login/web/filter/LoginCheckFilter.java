package hello.login.web.filter;

import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {
    /**
     * 로그인 없이도 접근가능한 곳
     * css를 안 넣어주면 로그인 안 하면 css가 적용안되니 넣어줘야함
     * 또한 다른곳에서 whiteList 변수를 변경하면 안되기때문에 static에 final로 막아야함
     */
    private static final String[] whiteList={"/","/members/add","/login","/logout","/css/*"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            log.info("인증 체크 필터 시작{}", requestURI);
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행{}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                /**
                 * 세션이 null이거나 (사실 세션이 없으면 로그인 안한거기 때문에 여기서 걸리기만해도 짤없음)
                 * 세션안의 객체중 SessionConst.LOGIN_MEMBER가 NULL이면 로그인 안했다는것
                 */
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청{}", requestURI);
                    /**
                     * 로그인으로 redirect
                     */
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw  e; //예외 로깅가능하지만, 톰캣까지 예외를 보내줘야함,여기서 예외처리를 해버리면 정상동작한것 처럼 보이기때문에 서블릿컨테이너까지 올려줘야함
        }finally {
            log.info("인증 체크 필터 종료 {}",requestURI);
        }

    }
    /**
     * 화이트 리스트의 경우 인증 체크 x
     */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
        /**whitelist와 매칭하지않으면 false 이를 !하면 true
         * 즉 whitelist와 매칭하지않으면 true로 리턴하라는것
          */
    }
}
