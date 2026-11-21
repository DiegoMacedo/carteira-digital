package br.com.ewallet.service;

import br.com.ewallet.model.Conta;
import br.com.ewallet.repository.ContaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.IllegalFormatException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {
    @Mock
    private ContaRepository contaRepository;
    @InjectMocks
    private ContaService contaService;

    @Test
    @DisplayName("Deve retornar o saldo atual quando a conta existir")
    void deveConsultarSaldoComSucesso() {
        Conta conta = new Conta(1L, "Diego", 1000.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Double saldoAtual = contaService.consultarSaldo(1L);

        assertEquals(1000.0, saldoAtual, 0.001);
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao consultar saldo de conta inexistente")
    void deveLancarExcecaoQuandoContaNaoExistir() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contaService.consultarSaldo(99L));
        verify(contaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve realizar depósito com sucesso e atualizar o saldo")
    void deveRealizarDepositoComSucesso() {
        Conta conta = new Conta(1L, "Diego", 1000.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Double novoSaldo = contaService.depositar(1L, 500.0);

        assertEquals(1500.0, novoSaldo, 0.001);
        assertEquals(1500.0, conta.getSaldo(), 0.001);
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao depositar valor igual a zero")
    void deveLancarExcecaoAoDepositarValorZero() {
        assertThrows(IllegalArgumentException.class, () -> contaService.depositar(1L, 0.0));
        verifyNoInteractions(contaRepository);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao depositar valor negativo")
    void deveLancarExcecaoAoDepositarValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> contaService.depositar(1L, -100.0));
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao depositar valor nulo")
    void deveLancarExceptionAoDepositarValorNulo() {
        assertThrows(IllegalArgumentException.class, () -> contaService.depositar(1L, null));
        verifyNoInteractions(contaRepository);
    }
}

