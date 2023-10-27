package kr.co.puerpuella.prtjwtapiserver.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Roles {

    IT_MANAGER(9, "IT 관리자"),
    MANAGER(8, "운영자"),
    MEMBER(7, "회원");

    private int roleKey;
    private String roleName;

    @JsonCreator
    public static Roles resolve(Integer roleKey) {
        return Optional.ofNullable(CODE_MAP.get(roleKey)).orElseThrow(() -> new IllegalArgumentException(roleKey+" is invalid value"));

    }

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