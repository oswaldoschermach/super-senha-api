package com.nebula_soft.super_senha.Controller;

import com.nebula_soft.super_senha.Enum.DifficultyEnum;
import com.nebula_soft.super_senha.Enum.WordStatusEnum;
import com.nebula_soft.super_senha.Exception.WordAlreadyExistsException;
import com.nebula_soft.super_senha.Service.WordService;
import com.nebula_soft.super_senha.dto.WordCreateDTO;
import com.nebula_soft.super_senha.dto.WordDTO;
import com.nebula_soft.super_senha.dto.WordUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/words")
@Tag(name = "Palavras", description = """
<h3>📘 API de Gerenciamento de Palavras</h3>

<p>Esta API é responsável pelo CRUD (criação, leitura, atualização e exclusão) de palavras utilizadas nas partidas do jogo Super Senha.</p>

<hr/>

<h4>🔁 <u>Fluxo Geral de Utilização pelo Frontend</u></h4>
<ol>
  <li>🎯 <b>Listar palavras disponíveis</b> com filtros de dificuldade e status.</li>
  <li>🧠 <b>Selecionar palavras ativas</b> para compor a rodada de jogo conforme o nível de dificuldade.</li>
  <li>🆕 <b>Adicionar palavras novas</b> pelo painel de administração.</li>
  <li>✏️ <b>Atualizar ou corrigir palavras</b> existentes.</li>
  <li>🚫 <b>Inativar palavras</b> ao invés de deletar, caso estejam em uso histórico.</li>
</ol>

<hr/>

<h4>📌 <u>Conceitos Importantes</u></h4>
<ul>
  <li><b>🧩 Palavra (Word)</b>: o conteúdo a ser adivinhado pelos jogadores.</li>
  <li><b>⚙️ Status</b>: indica se a palavra está ativa no sistema (ACTIVE / INACTIVE).</li>
  <li><b>📈 Nível de Dificuldade</b>: classifica a complexidade da palavra (EASY, MEDIUM, HARD).</li>
  <li><b>🎮 GameRoom</b>: sala de jogo onde a palavra foi ou será utilizada. Palavras podem ser compartilhadas entre várias salas.</li>
</ul>

<hr/>

<h4>🎨 <u>Exemplo de Objeto</u> (WordDTO)</h4>
<pre>
{
  "id": 12,
  "value": "astronauta",
  "status": "ACTIVE",
  "difficulty": "HARD",
  "gameRoomId": 3
}
</pre>
""")
public class WordController {

    private final WordService wordService;

    @Operation(
            summary = "Listar todas as palavras",
            description = "Retorna todas as palavras cadastradas, sem filtros."
    )
    @GetMapping
    public ResponseEntity<List<WordDTO>> getAllWords() {
        try {
            return ResponseEntity.ok(wordService.getAllWords());
        } catch (Exception e) {
            log.error("Erro ao buscar todas as palavras", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar palavras", e);
        }
    }

    @Operation(
            summary = "Buscar palavra por ID",
            description = "Retorna os dados de uma palavra específica, dado seu ID. Retorna 404 se não encontrada."
    )
    @GetMapping("/{id}")
    public ResponseEntity<WordDTO> getWordById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(wordService.getWordById(id));
        } catch (EntityNotFoundException e) {
            log.warn("Palavra com ID {} não encontrada", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro ao buscar palavra por ID", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar palavra", e);
        }
    }

    @Operation(
            summary = "Filtrar palavras por dificuldade e status",
            description = """
        Retorna as palavras filtradas com base na dificuldade (EASY, MEDIUM, HARD)
        e status (ACTIVE ou INACTIVE). Ideal para seleção dinâmica no jogo.
        """
    )
    @GetMapping("/filter")
    public ResponseEntity<List<WordDTO>> getWordsByDifficultyAndStatus(
            @RequestParam DifficultyEnum difficulty,
            @RequestParam WordStatusEnum status) {
        try {
            return ResponseEntity.ok(wordService.getWordsByDifficultyAndStatus(difficulty, status));
        } catch (Exception e) {
            log.error("Erro ao filtrar palavras", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao filtrar palavras", e);
        }
    }

    @Operation(
            summary = "Atualizar palavra",
            description = "Atualiza os dados de uma palavra existente. Não altera o status."
    )
    @PutMapping("/{id}")
    public ResponseEntity<WordDTO> updateWord(
            @PathVariable Long id,
            @RequestBody WordUpdateDTO dto) {
        try {
            return ResponseEntity.ok(wordService.updateWord(id, dto));
        } catch (EntityNotFoundException e) {
            log.warn("Atualização falhou: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro ao atualizar palavra", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar palavra", e);
        }
    }

    @Operation(
            summary = "Atualizar status da palavra",
            description = "Muda o status da palavra para ACTIVE ou INACTIVE."
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Boolean> updateWordStatus(
            @PathVariable Long id,
            @RequestParam WordStatusEnum status) {
        try {
            return ResponseEntity.ok(wordService.updateWordStatus(id, status));
        } catch (EntityNotFoundException e) {
            log.warn("Erro ao atualizar status: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro ao atualizar status da palavra", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar status", e);
        }
    }

    @Operation(
            summary = "Deletar palavra",
            description = "Remove uma palavra do sistema de forma permanente. ⚠️ Use com cuidado!"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        try {
            wordService.deleteWord(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.warn("Erro ao deletar palavra: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro interno ao deletar palavra", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao deletar palavra", e);
        }
    }

    @Operation(
            summary = "Criar nova palavra",
            description = """
        Cria uma nova palavra no banco de dados.

        <b>Campos obrigatórios:</b>
        <ul>
            <li><code>value</code> – o texto da palavra</li>
            <li><code>difficulty</code> – nível da palavra</li>
        </ul>
        <b>Campo opcional:</b>
        <ul>
            <li><code>gameRoomId</code> – ID da sala de jogo onde será usada</li>
        </ul>

        ⚠️ Retorna 409 CONFLICT se a palavra já existir (case insensitive).
        """
    )
    @PostMapping("/addWord")
    public ResponseEntity<WordDTO> createWord(@Valid @RequestBody WordCreateDTO dto) {
        try {
            WordDTO created = wordService.createWord(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (WordAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            log.error("Erro ao criar palavra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}