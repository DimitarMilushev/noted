package com.d_m.noted.auth;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.shared.dtos.auth.SignInResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);
    SignInResponseDto userSessionDetailsToSignInResponseDto(UserPrincipal details);
}
