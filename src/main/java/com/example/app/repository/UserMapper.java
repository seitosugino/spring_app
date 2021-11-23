package com.example.app.repository;
import com.example.app.dto.UserSearchRequest;
import com.example.app.entity.User;

import org.apache.ibatis.annotations.Mapper;
/**
 * ユーザー情報 Mapper
 */
@Mapper
public interface UserMapper {
    /**
     * ユーザー情報検索
     * @param user 検索用リクエストデータ
     * @return ユーザー情報
     */
    User search(UserSearchRequest user);
}