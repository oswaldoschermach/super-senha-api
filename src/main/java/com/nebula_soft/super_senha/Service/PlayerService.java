package com.nebula_soft.super_senha.Service;

import com.nebula_soft.super_senha.Entity.PlayerEntity;
import com.nebula_soft.super_senha.Exception.PlayerOperationException;
import com.nebula_soft.super_senha.Repository.PlayerRepository;
import com.nebula_soft.super_senha.dto.PlayerDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional
    public PlayerDTO createPlayer(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            log.error("Nome do jogador inválido fornecido");
            throw new IllegalArgumentException("Nome do jogador não pode ser nulo ou vazio");
        }

        PlayerEntity player = new PlayerEntity();
        player.setPlayerName(playerName);

        try {
            PlayerEntity savedPlayer = playerRepository.save(player);
            log.info("Jogador criado com sucesso - ID: {}", savedPlayer.getId());
            return PlayerDTO.fromEntity(savedPlayer);
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao criar jogador";
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerEntity> findByGameRoomId(Long gameRoomId) {
        if (gameRoomId == null || gameRoomId <= 0) {
            log.error("ID da GameRoom inválido fornecido: {}", gameRoomId);
            throw new IllegalArgumentException("ID da GameRoom não pode ser nulo ou menor que zero");
        }

        try {
            List<PlayerEntity> players = playerRepository.findByGameRoomId(gameRoomId);
            log.info("Encontrados {} jogadores para a GameRoom ID: {}", players.size(), gameRoomId);
            return players;
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao buscar jogadores para a GameRoom ID: " + gameRoomId;
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        }
    }


    @Transactional(readOnly = true)
    public Optional<PlayerDTO> getPlayerById(Long id) {
        if (id == null || id <= 0) {
            log.warn("ID inválido fornecido: {}", id);
            return Optional.empty();
        }

        try {
            Optional<PlayerEntity> player = playerRepository.findById(id);
            return player.map(PlayerDTO::fromEntity);
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao buscar jogador por ID: " + id;
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerDTO> getAllPlayers() {
        try {
            List<PlayerEntity> players = playerRepository.findAll();
            log.debug("Encontrados {} jogadores", players.size());
            return players.stream()
                    .map(PlayerDTO::fromEntity)
                    .toList();
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao buscar todos os jogadores";
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        }
    }

    @Transactional
    public Boolean updatePlayer(Long id, PlayerDTO updatedPlayerDTO) {
        if (id == null || id <= 0) {
            log.error("ID inválido fornecido para atualização: {}", id);
            throw new IllegalArgumentException("ID inválido");
        }
        if (updatedPlayerDTO == null) {
            log.error("Dados do jogador nulos fornecidos para atualização");
            throw new IllegalArgumentException("Player não pode ser nulo");
        }

        try {
            return playerRepository.findById(id)
                    .map(existingPlayer -> {
                        existingPlayer.setPlayerName(updatedPlayerDTO.getPlayerName());
                        existingPlayer.setPoints(updatedPlayerDTO.getPoints());
                        existingPlayer.setReady(updatedPlayerDTO.isReady());
                        playerRepository.save(existingPlayer);
                        log.info("Jogador atualizado com sucesso - ID: {}", id);
                        return true;
                    })
                    .orElseThrow(() -> {
                        log.warn("Jogador não encontrado para atualização - ID: {}", id);
                        return new PlayerOperationException("Jogador não encontrado com ID: " + id);
                    });
        } catch (ObjectOptimisticLockingFailureException ex) {
            String errorMsg = "Conflito de versão ao atualizar jogador ID: " + id;
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao atualizar jogador ID: " + id;
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        }
    }

    @Transactional(readOnly = true)
    public Optional<PlayerEntity> findById(Long id) {
        if (id == null || id <= 0) {
            log.error("ID inválido fornecido para busca: {}", id);
            throw new IllegalArgumentException("ID inválido");
        }

        try {
            return playerRepository.findById(id);
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao buscar jogador com ID: " + id;
            log.error(errorMsg, ex);
            throw new PlayerOperationException(errorMsg, ex);
        }
    }


    @Transactional
    public Map<Long, String> deletePlayers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            log.error("Lista de IDs nula ou vazia fornecida para exclusão");
            throw new IllegalArgumentException("A lista de IDs não pode ser nula ou vazia");
        }

        Map<Long, String> resultado = new HashMap<>();

        for (Long id : ids) {
            try {
                if (id == null || id <= 0) {
                    log.warn("ID inválido na lista: {}", id);
                    resultado.put(id, "ID inválido");
                    continue;
                }

                if (!playerRepository.existsById(id)) {
                    log.warn("Jogador não encontrado - ID: {}", id);
                    resultado.put(id, "Jogador não encontrado");
                    continue;
                }

                playerRepository.deleteById(id);
                log.info("Jogador deletado com sucesso - ID: {}", id);
                resultado.put(id, "Deletado com sucesso");

            } catch (DataAccessException ex) {
                String erroMsg = "Erro ao deletar jogador com ID: " + id;
                log.error(erroMsg, ex);
                resultado.put(id, "Erro no banco de dados");
            } catch (Exception ex) {
                String erroMsg = "Erro inesperado ao deletar jogador com ID: " + id;
                log.error(erroMsg, ex);
                resultado.put(id, "Erro inesperado");
            }
        }

        return resultado;
    }

}