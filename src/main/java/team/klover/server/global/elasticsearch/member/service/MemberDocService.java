package team.klover.server.global.elasticsearch.member.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.klover.server.domain.member.v1.dto.MemberDto;
import team.klover.server.global.elasticsearch.member.doc.MemberDoc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberDocService {
    private final ElasticsearchClient client;

    @SneakyThrows
    public Page<MemberDto> search(String keyword, Pageable pageable){
        SearchResponse<MemberDoc> response =
                client.search(
                        SearchRequest.of(sq->sq.index("members")
                                .minScore(5.0)
                                .from((int)pageable.getOffset())
                                .size(pageable.getPageSize())
                                        .sort(SortOptions.of(so1->so1.field(f1->f1.field("_score").order(SortOrder.Desc))),
                                                SortOptions.of(so2->so2.field(f2->f2.field("id").order(SortOrder.Desc)))
                                        )
                                .query(q->q.bool(
                                        b->b.should(
                                                s1->s1.matchPhrase(mp1->mp1.field("nickname").query(keyword).boost(30f))
                                        ).should(
                                                s2->s2.matchPhrase(mp2->mp2.field("nickname.normalized").query(keyword).boost(15f)
                                                )
                                        ).should(
                                                s3->s3.match(m -> m.field("nickname.ngram").query(keyword).boost(1f))
                                        )

                                ))),
                        MemberDoc.class
                );

        long totalElements = Objects.requireNonNull(response.hits().total()).value();

        List<MemberDto> dtos = response.hits().hits().stream().map(
                hit -> new MemberDto(Objects.requireNonNull(hit.source()))
        ).toList();

        return new PageImpl<>(dtos, pageable,totalElements);
    }
}
