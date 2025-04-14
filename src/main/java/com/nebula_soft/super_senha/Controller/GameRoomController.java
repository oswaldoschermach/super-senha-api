package com.nebula_soft.super_senha.Controller;

import com.nebula_soft.super_senha.Entity.GameRoomEntity;
import com.nebula_soft.super_senha.Entity.PlayerEntity;
import com.nebula_soft.super_senha.Exception.GameRoomOperationException;
import com.nebula_soft.super_senha.Exception.ResourceNotFoundException;
import com.nebula_soft.super_senha.Service.GameRoomService;
import com.nebula_soft.super_senha.dto.GameRoomDTO;
import com.nebula_soft.super_senha.dto.PlayerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-room")
@RequiredArgsConstructor
@Slf4j
public class GameRoomController {

    private final GameRoomService gameRoomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestParam String name) {
        try {
            GameRoomDTO createdRoom = gameRoomService.createRoom(name);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Nome inválido: " + ex.getMessage());
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erro ao acessar banco de dados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao criar sala: " + ex.getMessage());
        }
    }

    @PostMapping("/{roomId}/add-player")
    public ResponseEntity<?> addPlayerToRoom(@PathVariable Long roomId, @RequestParam Long playerId) {
        try {
            gameRoomService.addPlayerToRoom(roomId, playerId);
            return ResponseEntity.ok("Jogador adicionado com sucesso");
        } catch (IllegalArgumentException | GameRoomOperationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao adicionar jogador: " + ex.getMessage());
        }
    }

    @PostMapping("/{roomId}/remove-player")
    public ResponseEntity<?> removePlayerFromRoom(@PathVariable Long roomId, @RequestBody PlayerEntity player) {
        try {
            gameRoomService.removePlayerFromRoom(roomId, player);
            return ResponseEntity.ok("Jogador removido com sucesso");
        } catch (IllegalArgumentException | GameRoomOperationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao remover jogador: " + ex.getMessage());
        }
    }

    @PostMapping("/{roomId}/start")
    public ResponseEntity<?> startGame(@PathVariable Long roomId) {
        try {
            gameRoomService.startGame(roomId);
            return ResponseEntity.ok("Jogo iniciado com sucesso");
        } catch (GameRoomOperationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao iniciar jogo: " + ex.getMessage());
        }
    }

    @PostMapping("/{roomId}/end")
    public ResponseEntity<?> endGame(@PathVariable Long roomId) {
        try {
            gameRoomService.endGame(roomId);
            return ResponseEntity.ok("Jogo encerrado com sucesso");
        } catch (GameRoomOperationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao encerrar jogo: " + ex.getMessage());
        }
    }

    @GetMapping("/{roomId}/draw-pairs")
    public ResponseEntity<?> drawPairs(@PathVariable Long roomId) {
        try {
            List<PlayerDTO> pairs = gameRoomService.drawPairs(roomId);

            if (pairs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Não há jogadores suficientes para formar duplas.");
            }

            return ResponseEntity.ok(pairs);
        } catch (GameRoomOperationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao sortear duplas: " + ex.getMessage());
        }
    }
}
