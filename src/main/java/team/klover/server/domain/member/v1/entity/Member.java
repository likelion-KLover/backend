package team.klover.server.domain.member.v1.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomMember;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.entity.CommPostLike;
import team.klover.server.domain.community.commPost.entity.CommPostSave;
import team.klover.server.domain.community.comment.entity.Comment;
import team.klover.server.domain.community.comment.entity.CommentLike;
import team.klover.server.domain.member.v1.dto.MemberUpdateParam;
import team.klover.server.domain.member.v1.enums.Country;
import team.klover.server.domain.member.v1.enums.MemberRole;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.tour.review.entity.Review;
import team.klover.server.domain.tour.tourPost.entity.TourPostSave;
import team.klover.server.global.jpa.BaseEntity;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Country country;

    //회원 탈퇴는 하드 삭제이므로,
    //멤버를 참조하는 엔티티를 OneToMany로 관리하되 이 리스트에서 조회하지 않고 Repository를 통해 조회하여
    //성능 문제와 삭제 관리를 동시에 집는 것이 목표.

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<CommPost> myCommPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<CommPostSave> savedCommPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<CommPostLike> likedCommPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<Comment> myCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<CommentLike> likedCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<ChatRoomMember> enteredChatRoom = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<TourPostSave> savedTourPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    List<Review> myReviewList = new ArrayList<>();

    public void update(MemberUpdateParam param,String imageUrl) {
        if(imageUrl!=null) profileUrl = imageUrl;

        if(param.getNickname()!=null && !param.getNickname().isBlank() && !param.getNickname().equals(nickname)) {
            nickname = param.getNickname();
        }

        if(param.getCountry()!=null && !param.getCountry().equals(country)){
            country = param.getCountry();
        }
    }

    public void addCommPost(CommPost commPost){
        this.myCommPostList.add(commPost);
    }

    public void removeCommPost(CommPost commPost){
        this.myCommPostList.remove(commPost);
    }

    public void addSavedCommPost(CommPostSave commPostSave){
        this.savedCommPostList.add(commPostSave);
    }

    public void removeSavedCommPost(CommPostSave commPostSave){
        this.savedCommPostList.remove(commPostSave);
    }

    public void addLikedCommPost(CommPostLike commPostLike){
        this.likedCommPostList.add(commPostLike);
    }

    public void removeLikedCommPost(CommPostLike commPostLike){
        this.likedCommPostList.remove(commPostLike);
    }

    public void addComment(Comment comment){
        this.myCommentList.add(comment);
    }

    public void removeComment(Comment comment){
        this.myCommentList.remove(comment);
    }

    public void addLikedComment(CommentLike commentLike){
        this.likedCommentList.add(commentLike);
    }

    public void removeLikedComment(CommentLike commentLike){
        this.likedCommentList.remove(commentLike);
    }

    public void addEnteredChatRoom(ChatRoomMember chatRoomMember){
        this.enteredChatRoom.add(chatRoomMember);
    }

    public void removeEnteredChatRoom(ChatRoomMember chatRoomMember){
        this.enteredChatRoom.remove(chatRoomMember);
    }

    public void addSavedTourPost(TourPostSave tourPostSave){
        this.savedTourPostList.add(tourPostSave);
    }

    public void removeSavedTourPost(TourPostSave tourPostSave){
        this.savedTourPostList.remove(tourPostSave);
    }

    public void addReview(Review review){
        this.myReviewList.add(review);
    }

    public void removeReview(Review review){
        this.myReviewList.remove(review);
    }
}
