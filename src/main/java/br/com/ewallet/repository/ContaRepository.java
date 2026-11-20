package br.com.ewallet.repository;

import br.com.ewallet.model.Conta;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ContaRepository {
    private final Map<Long, Conta> bancoEmMemoria = new HashMap<>();

    public ContaRepository() {
        bancoEmMemoria.put(1L, new Conta(1L, "Diego", 1000.00));
        bancoEmMemoria.put(2L, new Conta(2L, "Esther", 500.00));
    }

    public Optional<Conta> findById(Long id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    public Conta save(Conta conta) {
        bancoEmMemoria.put(conta.getId(), conta);
        return conta;
    }
}
