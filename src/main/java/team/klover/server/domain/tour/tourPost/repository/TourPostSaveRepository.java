package team.klover.server.domain.tour.tourPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.klover.server.domain.community.commPost.entity.CommPostSave;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.tour.tourPost.entity.TourPostSave;

import java.util.List;
import java.util.Optional;

public interface TourPostSaveRepository extends JpaRepository<TourPostSave, Long> {
    List<TourPostSave> findAllByMember(Member member);

    @Query("""
select ts from TourPostSave ts
where ts.tourPost.commonPlaceId = :commonPlaceId and ts.member.id = :memberId
""")
    Optional<TourPostSave> haveSaved(@Param("commonPlaceId")Long commonPlaceId, @Param("memberId")Long memberId);
}
