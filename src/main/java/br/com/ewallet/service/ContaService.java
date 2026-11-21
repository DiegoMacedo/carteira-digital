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
        return buscarContaPorId(id).getSaldo();
    }

    private Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para o ID: " + id));
    }
}
