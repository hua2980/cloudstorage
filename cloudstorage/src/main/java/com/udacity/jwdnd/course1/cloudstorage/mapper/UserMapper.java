package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * Select User Object by UserName
     * @param username
     * @return
     */
    @Select("SELECT * FROM USERS where username = #{username}")
    User selectByUserName(String username);

    @Insert("INSERT INTO USERS(username, salt, password, firstname, lastname) values(" +
            "#{username}, " +
            "#{salt}, " +
            "#{password}, " +
            "#{firstName}, " +
            "#{lastName}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);
}
