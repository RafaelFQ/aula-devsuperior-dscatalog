package org.auladevsuperior.dscatalog.controllers.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.auladevsuperior.dscatalog.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
      StandardError err = new StandardError();
      err.setTimestamp(Instant.now());
      err.setStatus(HttpStatus.NOT_FOUND.value());
      err.setError("Recurso não encontrado");
      err.setMessage("Entidade não encontrada.");
      err.setPath(request.getRequestURI());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
   }
}
