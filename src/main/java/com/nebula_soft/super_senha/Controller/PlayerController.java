package com.nebula_soft.super_senha.Controller;

import com.nebula_soft.super_senha.Entity.PlayerEntity;
import com.nebula_soft.super_senha.Service.PlayerService;
import com.nebula_soft.super_senha.dto.PlayerDTO;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlayers() {
        try {
            List<PlayerDTO> players = playerService.getAllPlayers();

            if (players.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Nenhum jogador encontrado");
            }

            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Parâmetro inválido: " + ex.getMessage());
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erro de conexão com o banco de dados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar a solicitação: " + ex.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPlayer(@RequestBody String playerName) {
        try {
            PlayerDTO player = playerService.createPlayer(playerName);
            return ResponseEntity.status(HttpStatus.CREATED).body(player);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Parâmetro inválido: " + ex.getMessage());
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erro de conexão com o banco de dados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar o jogador: " + ex.getMessage());
        }
    }

    @GetMapping("/byGameRoom/{id}")
    public ResponseEntity<?> getPlayersByGameRoomId(@PathVariable Long id) {
        try {
            List<PlayerEntity> players = playerService.findByGameRoomId(id);

            if (players.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Nenhum jogador encontrado para a GameRoom com ID: " + id);
            }

            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Parâmetro inválido: " + ex.getMessage());
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erro de conexão com o banco de dados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar jogadores para a GameRoom com ID " + id + ": " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<Long, String>> deletePlayers(@RequestParam List<Long> playersIds) {
        Map<Long, String> result;

        try {
            result = playerService.deletePlayers(playersIds);

            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(Collections.singletonMap(null, "Nenhum jogador foi encontrado para deletar."));
            }

            return ResponseEntity.ok(result);

        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap(null, "Erro ao acessar o banco de dados: " + ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(null, "Parâmetro inválido: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap(null, "Erro inesperado: " + ex.getMessage()));
        }
    }


}
