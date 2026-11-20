package br.com.ewallet.service;

import br.com.ewallet.model.Conta;
import br.com.ewallet.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContaService {
    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Double consultarSaldo(Long id) {
        Optional<Conta> contaOptional = contaRepository.findById(id);
        if (contaOptional.isEmpty()) {
            throw new IllegalArgumentException("Conta não encontrada para o ID: " + id);

        }
        return contaOptional.get().getSaldo();
    }
}
