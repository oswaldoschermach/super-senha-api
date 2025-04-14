package com.nebula_soft.super_senha.Service;
import com.nebula_soft.super_senha.Entity.GameRoomEntity;
import com.nebula_soft.super_senha.Entity.PlayerEntity;
import com.nebula_soft.super_senha.Enum.GameStatusEnum;
import com.nebula_soft.super_senha.Exception.GameRoomOperationException;
import com.nebula_soft.super_senha.Exception.GameRuleException;
import com.nebula_soft.super_senha.Repository.GameRoomRepository;
import com.nebula_soft.super_senha.dto.GameRoomDTO;
import com.nebula_soft.super_senha.dto.PlayerDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final PlayerService playerService;

    @Transactional
    public GameRoomDTO createRoom(String name) {
        if (name == null || name.isEmpty()) {
            log.error("Nome da sala inválido fornecido");
            throw new IllegalArgumentException("Nome da sala não pode ser nulo ou vazio");
        }

        GameRoomEntity gameRoom = new GameRoomEntity();
        gameRoom.setName(name);
        gameRoom.setStatus(GameStatusEnum.LOBBY);

        try {
            GameRoomEntity savedRoom = gameRoomRepository.save(gameRoom);
            log.info("Sala de jogo criada com sucesso - ID: {}", savedRoom.getId());
            return GameRoomDTO.fromEntity(savedRoom);
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao criar sala de jogo";
            log.error(errorMsg, ex);
            throw new GameRoomOperationException(errorMsg, ex);
        }
    }


    @Transactional
    public void addPlayerToRoom(Long roomId, Long playerId) {
        if (playerId == null || playerId <= 0) {
            log.error("ID de jogador inválido fornecido: {}", playerId);
            throw new IllegalArgumentException("ID do jogador inválido");
        }

        GameRoomEntity room = getGameRoomById(roomId)
                .orElseThrow(() -> {
                    log.error("Sala de jogo não encontrada - ID: {}", roomId);
                    return new GameRoomOperationException("GameRoom não encontrada com ID: " + roomId);
                });

        PlayerEntity player = playerService.findById(playerId)
                .orElseThrow(() -> {
                    log.error("Jogador não encontrado - ID: {}", playerId);
                    return new GameRoomOperationException("Jogador não encontrado com ID: " + playerId);
                });

        try {
            room.addPlayer(player);
            gameRoomRepository.save(room);
            log.info("Jogador adicionado à sala de jogo - Player ID: {}, Room ID: {}", player.getId(), room.getId());
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao adicionar jogador à sala de jogo";
            log.error(errorMsg, ex);
            throw new GameRoomOperationException(errorMsg, ex);
        }
    }


    @Transactional
    public void removePlayerFromRoom(Long roomId, PlayerEntity player) {
        if (player == null) {
            log.error("Jogador nulo fornecido");
            throw new IllegalArgumentException("Player não pode ser nulo");
        }

        GameRoomEntity room = getGameRoomById(roomId)
                .orElseThrow(() -> {
                    log.error("Sala de jogo não encontrada - ID: {}", roomId);
                    return new GameRoomOperationException("GameRoom não encontrada com ID: " + roomId);
                });

        try {
            room.removePlayer(player);
            gameRoomRepository.save(room);
            log.info("Jogador removido da sala de jogo - Player ID: {}, Room ID: {}", player.getId(), room.getId());
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao remover jogador da sala de jogo";
            log.error(errorMsg, ex);
            throw new GameRoomOperationException(errorMsg, ex);
        }
    }

    @Transactional
    public void startGame(Long roomId) {
        GameRoomEntity room = getGameRoomById(roomId)
                .orElseThrow(() -> {
                    log.error("Sala de jogo não encontrada - ID: {}", roomId);
                    return new GameRoomOperationException("GameRoom não encontrada com ID: " + roomId);
                });

        try {
            room.startGame();
            gameRoomRepository.save(room);
            log.info("Jogo iniciado na sala de jogo - Room ID: {}", room.getId());
        } catch (GameRuleException ex) {
            String errorMsg = "Erro ao iniciar o jogo: " + ex.getMessage();
            log.error(errorMsg, ex);
            throw new GameRoomOperationException(errorMsg, ex);
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao iniciar o jogo na sala de jogo";
            log.error(errorMsg, ex);
            throw new GameRoomOperationException(errorMsg, ex);
        }
    }

    @Transactional
    public void endGame(Long roomId) {
        GameRoomEntity room = getGameRoomById(roomId)
                .orElseThrow(() -> {
                    log.error("Sala de jogo não encontrada - ID: {}", roomId);
                    return new GameRoomOperationException("GameRoom não encontrada com ID: " + roomId);
                });

        try {
            room.endGame(true);
            gameRoomRepository.save(room);
            log.info("Jogo encerrado na sala de jogo - Room ID: {}", room.getId());
        } catch (DataAccessException ex) {
            String errorMsg = "Erro ao encerrar o jogo na sala de jogo";
            log.error(errorMsg, ex);
            throw new GameRoomOperationException(errorMsg, ex);
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerDTO> drawPairs(Long roomId) {
        GameRoomEntity room = getGameRoomById(roomId)
                .orElseThrow(() -> {
                    log.error("Sala de jogo não encontrada - ID: {}", roomId);
                    return new GameRoomOperationException("GameRoom não encontrada com ID: " + roomId);
                });

        if (room.getPlayers().isEmpty()) {
            log.warn("Não é possível sortear duplas, sala vazia");
            return Collections.emptyList();
        }

        List<PlayerEntity> players = room.getPlayers();
        Collections.shuffle(players);

        if (players.size() < 2) {
            log.warn("Número insuficiente de jogadores para sortear duplas");
            return Collections.emptyList();
        }

        return players.subList(0, 2).stream()
                .map(PlayerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private Optional<GameRoomEntity> getGameRoomById(Long id) {
        return gameRoomRepository.findById(id);
    }
}