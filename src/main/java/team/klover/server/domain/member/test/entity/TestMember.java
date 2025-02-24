package team.klover.server.domain.member.test.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import team.klover.server.global.jpa.BaseEntity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TestMember extends BaseEntity {
    private String email;
    private String password;
    private String nickname;
}
