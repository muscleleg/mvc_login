package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    /*
        세션생성
         */
    public void createSession(Object value, HttpServletResponse response) {
        //session id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        //쿠키생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
        //createSession에 받으면 서버의 map에 저장을하고 클라이언트에게 sessionid가 담긴 쿠키를보낸다.


    }

    /**
     * 세션조회
     * 처음에 findCookie를 실행
     * request해더에 쿠키가 있는지 확인, 없으면 null
     * 있다면 쿠키들 목록에서 이름이 SESSION_COOKIE_NAME(mySessionId)와 같은게 있는 지 필터로 확인,있다면 해당값 없다면 null,반환
     * 반환값을 sessionCookie에 대입
     * null이라면 null로 반환
     * 있다면 sessionStore.get에 findCookie로 받아온 쿠키의 value를 넣어서 찾음(쿠키의 value는 session의 아이디값임),
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);//
        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findAny().orElse(null);
    }
}
