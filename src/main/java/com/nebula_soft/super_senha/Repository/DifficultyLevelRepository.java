package com.nebula_soft.super_senha.Repository;

import com.nebula_soft.super_senha.Entity.DifficultyLevelEntity;
import com.nebula_soft.super_senha.Entity.GameRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DifficultyLevelRepository extends JpaRepository<DifficultyLevelEntity, Long> {
    Optional<DifficultyLevelEntity> findByLevelNameIgnoreCase(String levelName);
}
