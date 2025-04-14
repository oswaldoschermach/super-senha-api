package com.nebula_soft.super_senha.Repository;

import com.nebula_soft.super_senha.Entity.WordEntity;
import com.nebula_soft.super_senha.Enum.WordStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface WordRepository extends JpaRepository<WordEntity, Long> {

    // Retorna as palavras com um determinado nível de dificuldade e páginação
    Page<WordEntity> findByDifficultyLevelId(Integer difficultyLevelId, Pageable pageable);

    // Conta o número total de palavras com um determinado nível de dificuldade e status
    long countByDifficultyLevelIdAndStatus(Long difficultyLevelId, WordStatusEnum status);

    // Retorna uma lista de palavras com um determinado nível de dificuldade e status com paginação
    List<WordEntity> findByDifficultyLevelIdAndStatus(Long difficultyLevelId, WordStatusEnum status, Pageable pageable);

    // Retorna as palavras com um determinado nível de dificuldade e um conjunto de IDs usados excluídos
    @Query("SELECT w FROM WordEntity w WHERE w.difficultyLevel.id = :difficultyId AND w.id NOT IN :usedIds")
    List<WordEntity> findAvailableByDifficulty(@Param("difficultyId") Long difficultyId, @Param("usedIds") Set<Long> usedIds);

    // Retorna as palavras com um determinado nível de dificuldade e status com um número máximo de resultados
    @Query("SELECT w FROM WordEntity w WHERE w.difficultyLevel.id = :difficultyLevelId AND w.status = :status")
    List<WordEntity> findTopNByDifficultyLevelIdAndStatus(@Param("difficultyLevelId") Long difficultyLevelId,
                                                          @Param("status") WordStatusEnum status,
                                                          Pageable pageable);

    Boolean existsByWordIgnoreCase(String value);
}
