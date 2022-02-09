package com.jesper.mapper;


import com.jesper.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface UserMapper {

    User selectByNameAndPwd(User user);

    int insert(User user);

    int update(User user);

    int selectIsName(User user);

    String selectPasswordByName(User user);

    void updateStateByCode(String code);
}
