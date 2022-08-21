package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();
    @Test
    @DisplayName("세션테스트")
    void sessionTest(){
        //세션 생성

        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member,response);
        //createSession에 받으면 서버의 map에 저장을하고 클라이언트에게 sessionid가 담긴 쿠키를보낸다. 웹브라우저에 응답이 나갔다 생각하삼

        //요청에 응답 쿠키 저장, 웹브라우저가 쿠키를 저장한것을 우리 서버에 보내준거임
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());


        //세션조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);
        //세션만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();
    }
}
