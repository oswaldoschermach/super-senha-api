package com.nebula_soft.super_senha.Repository;

import com.nebula_soft.super_senha.Entity.GameRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoomEntity, Long> {

}
