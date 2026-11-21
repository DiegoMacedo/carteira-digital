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

    public Double depositar(Long id, Double valor) {
        validarValorPositivo(valor);
        Conta conta = buscarContaPorId(id);
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);
        return conta.getSaldo();
    }

    private void validarValorPositivo(Double valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor da operação deve ser maior que zero.");
        }
    }

    private Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para o ID: " + id));
    }
}
