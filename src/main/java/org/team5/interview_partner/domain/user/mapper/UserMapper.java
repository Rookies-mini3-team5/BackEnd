package org.team5.interview_partner.domain.user.mapper;

import org.team5.interview_partner.common.error.ErrorCode;
import org.team5.interview_partner.common.exception.ApiException;
import org.team5.interview_partner.domain.user.dto.JoinRequest;
import org.team5.interview_partner.domain.user.dto.LoginResponse;
import org.team5.interview_partner.entity.user.UsersEntity;
import org.team5.interview_partner.entity.user.enums.Role;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserMapper {
    //JoinRequest to UserEntity
    public static UsersEntity toEntity(JoinRequest joinRequest){
        return Optional.ofNullable(joinRequest)
                .map((it)->{
                    return UsersEntity.builder()
                            .role(Role.ROLE_USER)
                            .username(joinRequest.getUsername())
                            .name(joinRequest.getName())
                            .password(joinRequest.getPassword())
                            .email(joinRequest.getEmail())
                            .createdAt(LocalDateTime.now())
                            .build();

                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }

    //UserEntity to LoginResponse
    public static LoginResponse toResponse(UsersEntity usersEntity){
        return Optional.ofNullable(usersEntity)
                .map((it)->{
                    return LoginResponse.builder()
                            .role(usersEntity.getRole())
                            .name(usersEntity.getName())
                            .username(usersEntity.getUsername())
                            .build();

                }).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST));
    }


}