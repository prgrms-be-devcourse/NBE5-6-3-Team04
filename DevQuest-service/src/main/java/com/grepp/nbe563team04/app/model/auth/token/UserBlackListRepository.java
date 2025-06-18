package com.grepp.nbe563team04.app.model.auth.token;

import com.grepp.nbe563team04.app.model.auth.token.entity.UserBlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlackListRepository extends CrudRepository<UserBlackList, String> {
}
