package team.klover.server.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import team.klover.server.domain.member.v1.enums.Country;
import team.klover.server.global.exception.KloverLogicException;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.security.custom.CustomUserDetails;

import java.util.Collection;

public class AuthUtil {

    public static Enum<Country> getCurrentMemberCountry(){ return getUserDetails().getMember().getCountry();}

    public static String getCurrentMemberEmail() {
        return getUserDetails().getUsername();
    }

    public static String getCurrentMemberNickname() {
        return getUserDetails().getMember().getNickname();
    }

    public static Collection<? extends GrantedAuthority> getAuth() {
        return getUserDetails().getAuthorities();
    }

    public static Long getCurrentMemberId() {
        return getUserDetails().getMember().getId();
    }

    public static Long getCurrentMemberIdRoughly(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object userDetails =  authentication.getPrincipal();
        if (userDetails == null || userDetails instanceof String) {
            return null;
        }

        return ((CustomUserDetails)userDetails).getMember().getId();
    }
    public static Authentication getAuthentication(){return SecurityContextHolder.getContext().getAuthentication();}

    private static CustomUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new KloverLogicException(ReturnCode.NOT_AUTHORIZED);

        Object userDetails =  authentication.getPrincipal();
        if (userDetails == null || userDetails instanceof String) throw new KloverLogicException(ReturnCode.NOT_FOUND_ENTITY);

        return (CustomUserDetails)userDetails;
    }
}
