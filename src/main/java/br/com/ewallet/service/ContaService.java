package br.com.ewallet.service;

import br.com.ewallet.exception.SaldoInsuficienteException;
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

    public void transferir(Long idOrigem, Long idDestino, Double valor) {
        validarValorPositivo(valor);
        Conta contaOrigem = buscarContaPorId(idOrigem);
        Conta contaDestino = buscarContaPorId(idDestino);

        if (contaOrigem.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem.");
        }
        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
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
