package com.example.user_service.mapper;


import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "name", source = "userCreateDto.name")
    @Mapping(target = "email", source = "userCreateDto.email")
    @Mapping(target = "phoneNumber", source = "userCreateDto.phoneNumber")
    @Mapping(target = "userInfo.userName", source = "userCreateDto.userName")
    @Mapping(target = "userInfo.password", source = "userCreateDto.password")
    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateDto userCreateDto) ;

    @Mapping(target = "userId", source = "id")
    UserAfterCreationDto toDto(User user);

}
