package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId",required = false)Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }
        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";

    }

    /**
     *
     * getSession에 request를 넣음
     * findCookie에 request와 SESSION_COOKIE_NAME을 넣음
     * findCookie에서 request에 쿠키 자체가없으면 null반환
     * 아니면 request에 있는 cookie배열을 스트림으로 바꿔고
     * 필터로 쿠키이름이 SESSION_COOKIE_NAME과 같은 것을 찾음,없으면null을 보냄
     * sessionCookie가 null이면 null이고 필터로 받아온 쿠키의 값(mySessionId의 value)를
     * sessionStore.get에 넣음 있으면 Object(member)가 나오겠고 없으면 null나옴
     * object로 반환되었기때문에 member로 형변환 반약없다면 형변환이고 나발이고 없는겨
     * 만약 member가 null이면 쿠키뒤져보고 세션도 뒤져봤는데 없다는거
     * 있다면 loginHome으로 이동
     * Member member가 null이면 그냥 홈화면으로 보냄
     */
//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);

        //로그인
        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";

    }

    /**
     *로그인 하면 서버에서 세션을 만든다. 서버에서 클라이언트에 세션id를 담은 쿠키를 보낸다.
     *home에 오면 getSession으로 session이 있나보고 없으면 null
     */
//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        //세션 관리자에 저장된 회원 정보 조회
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        //세션에 회원데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";

    }
    @GetMapping("/")//@SessionAttribute는 세션을 생성하지는 않음 따라서 세션을 조회할때만 사용해야함
    public String homeLoginV3Spring(@SessionAttribute(name=SessionConst.LOGIN_MEMBER,required = false) Member loginMember, Model model) {
        //세션 관리자에 저장된 회원 정보 조회

        //세션에 회원데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";

    }
}