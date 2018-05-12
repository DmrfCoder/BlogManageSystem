package com.dmrf.repository;

import com.dmrf.model.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dmrf on 2018/5/10.
 */
@Repository
public interface ManagerRepository extends JpaRepository<ManagerEntity, Integer> {

    @Modifying      // 说明该方法是修改操作
    @Transactional  // 说明该方法是事务性操作
    // 定义查询
    // @Param注解用于提取参数
    @Query("update ManagerEntity  us set us.nickname=:qNickname,  us.password=:qPassword where us.id=:qId")
    public void updateManager(@Param("qNickname") String nickname, @Param("qPassword") String password, @Param("qId") Integer id);
    @Query("SELECT mg FROM ManagerEntity mg WHERE mg.nickname=:qNickname AND mg.password=:qPassword")
    public ManagerEntity queryManager(@Param("qNickname") String nickname, @Param("qPassword") String password);
}
