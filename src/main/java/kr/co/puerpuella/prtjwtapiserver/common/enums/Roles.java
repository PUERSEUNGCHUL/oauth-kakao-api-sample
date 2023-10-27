package kr.co.puerpuella.prtjwtapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Roles {

    IT_MANAGER(9, "IT 관리자"),
    MANAGER(8, "운영자"),
    MEMBER(7, "회원")
    ;

    private int roleKey;
    private String roleName;


    private static final Map<Integer, Roles> CODE_MAP = Stream.of(values()).collect(Collectors.toMap(Roles::getRoleKey, Function.identity()));

    public static Roles valueOf(Integer type) {

        for (Roles item : Roles.values()) {

            if (item.roleKey == type) {
                return item;
            }
        }
        return null;
    }
}
