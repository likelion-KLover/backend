package team.klover.server.domain.member.v1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import team.klover.server.domain.member.v1.dto.MemberUpdateParam;
import team.klover.server.domain.member.v1.enums.MemberRole;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.global.common.entity.BaseEntity;

@Entity
@Getter
@Setter
@Table(name = "members")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String password;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;
    private String providerId;

    private String nickname;

    private String profileUrl;

    private String country;

    //회원 탈퇴는 하드 삭제이므로,
    //멤버를 참조하는 엔티티를 OneToMany로 관리하되 이 리스트에서 조회하지 않고 Repository를 통해 조회하여
    //성능 문제와 삭제 관리를 동시에 집는 것이 목표.

    public void update(MemberUpdateParam param) {
        profileUrl = param.getProfileUrl();

        if(param.getNickname()!=null && !param.getNickname().isBlank() && !param.getNickname().equals(nickname)) {
            nickname = param.getNickname();
        }

        if(param.getCountry()!=null && !param.getCountry().isBlank() && !param.getCountry().equals(country)){
            country = param.getCountry();
        }
    }
}
