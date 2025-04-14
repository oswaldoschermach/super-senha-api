package com.nebula_soft.super_senha.Repository;

import com.nebula_soft.super_senha.Entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    List<PlayerEntity> findByGameRoomId(Long gameRoomId);

}
