package team.klover.server.domain.tour.tourPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.tour.tourPost.entity.TourPostSave;

import java.util.List;

public interface TourPostSaveRepository extends JpaRepository<TourPostSave, Long> {
    List<TourPostSave> findAllByMember(Member member);
}
