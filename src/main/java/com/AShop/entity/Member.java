package com.AShop.entity;

import com.AShop.constant.Role;
import com.AShop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name ="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    /*회원은 이메일을 통해 유일하게 구분해야 하기 때문에 동일한 값이 데이터베이스에 들어올 수 없도록 UNIQUE 속성 지정*/
    private  String email;
    private String password;
    private String zipcode;
    private String address;
    private String detailAddr;
    @Enumerated(EnumType.STRING)
    private Role role;
    /*자바의 enum 타입을 엔티티 속성 지정 가능. Enum을 사용할 때 기본적으로 순서가 지정되는데
     * enum의 순서가 바뀔경우 문제가 발생할 수 있으므로  EnumType.String  옵션을 사용해 String 으로 저장 권장*/

    public  static Member createMember(MemberFormDto memberFormDto,
                                        PasswordEncoder passwordEncoder){
        /*Member 엔티티를 생성하는 메소드
        * Member 엔티티에 회원을 생성하는 메소드를 만들어서 관리를 한다면
        * 코드가 변경되더라도 한 군데 수정하면 되는 이점이 있음*/

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setZipcode(memberFormDto.getZipcode());
        member.setAddress(memberFormDto.getAddress());
        member.setDetailAddr(memberFormDto.getDetailAddr());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        /*스프링 시큐리티 설정 클레스에 등록한 BCryptPasswordEncoder Bean을 파라미터로 넘겨서 비밀번호를 암호화 함*/
        member.setPassword(password);
        member.setRole(Role.ADMIN);

        return member;
            }

    @OneToMany(mappedBy = "member")
    private List<Notice> notices;
}
