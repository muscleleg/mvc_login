package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/LoginForm";
    }

//    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";
        }
        //로그인 성공처리

        //쿠키에 시간 정보를 주지않으면 세션쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    /**
     * form으로 데이터를 받아옴
     * form이 잘못되었다면 bindingResult를 사용하여 오류가있는지 확일 할 수 있음
     * form으로 받은 loginId와 getPassword를 loginService에 넣음
     * findByLoginId에 들어감 findAll()에서 id가 갖은지 찾음 에서 비밀번호가 같은지 찾음
     * 같다면 해당map에 담긴 member나옴
     * 없다면 bindingResult.reject에 loginFail이라는 오류코드 들어가고 thymeleaf에서 출력
     * 성공하면 sessionManager.create에 Member와 response들어감
     * sessionId랜덤값으로 만들고 sessionStroe.put에 sessionid와 repository에서 찾아온 member넣음
     * 쿠키 만들고 이름은 mySessionId,value는 seessionId넣음
     * 쿠키를 response로 쿠키보냄
     */
//    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";
        }
        //로그인 성공처리

        //세션 관리자를 통해서 세션을 생성하고, 회원데이터를 보관
        sessionManager.createSession(loginMember,response);

        return "redirect:/";
    }
//    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";
        }
        //로그인 성공처리
        //세션이 있으면 세션반환,없으면 신규 세션 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        //세션 관리자를 통해서 세션을 생성하고, 회원데이터를 보관
//        sessionManager.createSession(loginMember,response);

        return "redirect:/";
    }
    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";
            /**
             *            여기서 주소가 왜 안바뀌냐할 수 있는데
             *            http://localhost:8080/login?adsfsad=123124로 요청받고
             *            return login/loginForm이 된다면 주소를 요청받은 주소를 옮기라는게 아니라
             *            서버내에 있는 login/loginForm으로 페이지를 렌더링하라는거임
             *            주소를 옮기고 싶다면 redirect를 해야함
             *
             *            loginForm.html이 작동하는 원리
             *            우리는 /login을 통해서 loginForm.html에 들어왔다.
             *                <form action="item.html" th:action th:object="${loginForm}" method="post">
             *            로 코드가 작성되어있음 우리가 주목해야하느건 주소임
             *            http://localhost:8080/login로 와서 loginForm.html의 action을 실행하는거임
             *            따라서 th:action="@{/items}"같이 주소값이 들어있는게 아니라 th:action으로 되어있으면
             *            action=""나 action생략과 같이 자기자신에게 요청을 보내게된다.
             *            따라서 로그인 시도를하면 post방식으로 /login에 접근하게 되는거다.
             */
        }
        //로그인 성공처리
        //세션이 있으면 세션반환,없으면 신규 세션 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        //세션 관리자를 통해서 세션을 생성하고, 회원데이터를 보관
//        sessionManager.createSession(loginMember,response);

        return "redirect:" + redirectURL;
    }


//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expiredCookie(response,"memberId");
        return "redirect:/";
    }

    /**
     * epire가 실행되면 findCookie에 request와 SESSION_COOKIE_NAME을 넣음
     * findCookie에서 request에 쿠키가 없다면 그냥 null반환함
     * request에 있는 쿠키목록에서 이름이 SEESSION_COOKIE_NAEME과 같은것이 있다면 반환하고 아니면 NULL반환함
     * sessionCookie가 있다면 해당쿠키의 값(세션토큰아이디)를 remove에 넣어서
     * sessionStore에 있는 해당토큰을 찾아서 제거함
     */
//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);//세션이 없다면 새로생성하지않고 null만 반환
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }


    private void expiredCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
