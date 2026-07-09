package com.lucas.estoque.exception;


import com.lucas.estoque.dto.ErroResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        var erro = new ErroResponseDTO(
                404,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponseDTO> handleBusinessException(BusinessException ex) {
        var erro = new ErroResponseDTO(
                422,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(422).body(erro);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErroResponseDTO> handleDuplicateResource(DuplicateResourceException ex) {
        var erro = new ErroResponseDTO(
                409,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(409).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> campos = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(erro ->
                campos.put(erro.getField(), erro.getDefaultMessage()));
        var erroResponse = new ErroResponseDTO(
                400,
                "Dados inválidos",
                campos
        );
        return ResponseEntity.status(400).body(erroResponse);
    }
}
