package com.nebula_soft.super_senha.Service;

import com.nebula_soft.super_senha.Entity.DifficultyLevelEntity;
import com.nebula_soft.super_senha.Entity.GameRoomEntity;
import com.nebula_soft.super_senha.Entity.WordEntity;
import com.nebula_soft.super_senha.Enum.DifficultyEnum;
import com.nebula_soft.super_senha.Enum.WordStatusEnum;
import com.nebula_soft.super_senha.Exception.DifficultyLevelNotFoundException;
import com.nebula_soft.super_senha.Exception.WordAlreadyExistsException;
import com.nebula_soft.super_senha.Repository.DifficultyLevelRepository;
import com.nebula_soft.super_senha.Repository.GameRoomRepository;
import com.nebula_soft.super_senha.Repository.WordRepository;
import com.nebula_soft.super_senha.dto.WordCreateDTO;
import com.nebula_soft.super_senha.dto.WordDTO;
import com.nebula_soft.super_senha.dto.WordUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;
    private final GameRoomRepository gameRoomRepository;
    private final DifficultyLevelRepository difficultyLevelRepository;

    @Transactional
    public List<WordDTO> getAllWords() {
        return wordRepository.findAll().stream()
                .map(word -> modelMapper.map(word, WordDTO.class))
                .collect(Collectors.toList());
    }

    public WordDTO createWord(WordCreateDTO dto) {
        String normalizedValue = dto.getValue().trim().toUpperCase();

        log.info(normalizedValue);

        boolean exists = wordRepository.existsByWordIgnoreCase(normalizedValue);
        if (exists) {
            throw new WordAlreadyExistsException("A palavra '" + normalizedValue + "' já está cadastrada.");
        }

        DifficultyLevelEntity difficultyLevel = difficultyLevelRepository
                .findByLevelNameIgnoreCase(dto.getDifficulty().name())
                .orElseThrow(() -> new DifficultyLevelNotFoundException("Nível de dificuldade '" + dto.getDifficulty() + "' não encontrado."));

        WordEntity word = new WordEntity();
        word.setWord(normalizedValue);
        word.setDifficultyLevel(difficultyLevel);
        word.setStatus(WordStatusEnum.AVAILABLE);

        WordEntity saved = wordRepository.save(word);
        return modelMapper.map(saved, WordDTO.class);
    }


    @Transactional
    public WordDTO getWordById(Long id) {
        WordEntity word = wordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Palavra com ID " + id + " não encontrada"));
        return modelMapper.map(word, WordDTO.class);
    }

    public List<WordDTO> getWordsByDifficultyAndStatus(DifficultyEnum difficultyLevel, WordStatusEnum status) {
        // Definindo o número de palavras a retornar com base na dificuldade
        int numberOfWords = switch (difficultyLevel) {
            case EASY -> 10;
            case MEDIUM -> 7;
            case HARD -> 5;
        };

        // Cria um Pageable com o número de palavras a retornar e a página inicial (0)
        Pageable pageable = PageRequest.of(0, numberOfWords);

        // Busca as palavras limitadas pelo número especificado de acordo com o nível de dificuldade e status
        List<WordEntity> wordEntities = wordRepository.findTopNByDifficultyLevelIdAndStatus(
                (long) difficultyLevel.getId(), status, pageable);

        // Mapeia as entidades para DTOs e retorna
        return wordEntities.stream()
                .map(WordDTO::fromEntity)
                .collect(Collectors.toList());
    }



    @Transactional
    public WordDTO updateWord(Long id, WordUpdateDTO dto) {
        // Buscar a entidade Word pelo ID
        WordEntity word = wordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Palavra com ID " + id + " não encontrada"));

        // Atualizar os campos da entidade WordEntity a partir do DTO
        if (dto.getWord() != null) {
            word.setWord(dto.getWord());
        }

        if (dto.getDifficultyLevelId() != null) {
            // Buscar a entidade DifficultyLevel usando o ID
            DifficultyLevelEntity difficultyLevel = difficultyLevelRepository.findById(dto.getDifficultyLevelId())
                    .orElseThrow(() -> new EntityNotFoundException("Nível de dificuldade com ID " + dto.getDifficultyLevelId() + " não encontrado"));
            word.setDifficultyLevel(difficultyLevel);
        }

        if (dto.getGameRoomId() != null) {
            // Buscar a entidade GameRoom usando o ID
            GameRoomEntity gameRoom = gameRoomRepository.findById(dto.getGameRoomId())
                    .orElseThrow(() -> new EntityNotFoundException("Sala de jogo com ID " + dto.getGameRoomId() + " não encontrada"));
            word.setGameRoom(gameRoom);
        }

        if (dto.getStatus() != null) {
            word.setStatus(dto.getStatus());
        }

        // Salvar a palavra atualizada no banco de dados
        WordEntity updated = wordRepository.save(word);

        // Retornar o DTO atualizado
        return modelMapper.map(updated, WordDTO.class);
    }


    @Transactional
    public void deleteWord(Long id) {
        if (!wordRepository.existsById(id)) {
            throw new EntityNotFoundException("Palavra com ID " + id + " não encontrada");
        }
        wordRepository.deleteById(id);
    }

    @Transactional
    public boolean updateWordStatus(Long id, WordStatusEnum status) {
        WordEntity word = wordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Palavra com ID " + id + " não encontrada"));
        word.setStatus(status);
        wordRepository.save(word);
        return true;
    }
}
