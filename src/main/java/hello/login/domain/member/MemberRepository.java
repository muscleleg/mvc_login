package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) { //primarykey로 찾는거
        return store.get(id);
    }
    public Optional<Member> findByLoginId(String loginId){//로그인 아이디로 찾는거
//        List<Member> all = findAll();
//        for (Member m : all) {
//            if(m.getLongId().equals(loginId)){
//                return Optional.of(m);//값이 절대 null이 아닐때 쓰는 옵션
//            }
//        }
//        return Optional.empty();
        return findAll().stream().filter(m->m.getLoginId().equals(loginId)).findFirst(); //serivce에서 optional쓰려고 optional옵션줌
    }
    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }
}
