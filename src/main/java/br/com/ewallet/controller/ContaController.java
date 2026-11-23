package br.com.ewallet.controller;

import br.com.ewallet.exception.SaldoInsuficienteException;
import br.com.ewallet.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contas")
public class ContaController {
    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity consltarSaldo(@PathVariable Long id) {
        Double saldo = contaService.consultarSaldo(id);
        return ResponseEntity.ok(Map.of(
                "id", id,
                "saldo", saldo
        ));
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity depositar(@PathVariable Long id,
                                    @RequestParam Double valor) {
        Double novoSaldo = contaService.depositar(id, valor);
        return ResponseEntity.ok(Map.of(
                "id", id,
                "saldo", novoSaldo,
                "mensagem", "Depósito realizado com sucesso."
        ));
    }

    @PostMapping("/transferencia")
    public ResponseEntity

    transferir(@RequestParam Long idOrigem,
               @RequestParam Long idDestino,
               @RequestParam Double valor) {
        contaService.transferir(idOrigem, idDestino, valor);
        return ResponseEntity.ok(Map.of(
                "idOrigem", idOrigem,
                "idDestino", idDestino,
                "valor", valor,
                "mensagem", "Transferência realizada com sucesso."
        ));
    }

    @ExceptionHandler({IllegalArgumentException.class, SaldoInsuficienteException.class})
    public ResponseEntity tratarErroDeNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

}
