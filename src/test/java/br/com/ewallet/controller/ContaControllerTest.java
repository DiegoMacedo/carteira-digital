package br.com.ewallet.controller;

import br.com.ewallet.model.Conta;
import br.com.ewallet.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp() {
        contaRepository.save(new Conta(1L, "Diego", 1000.0));
        contaRepository.save(new Conta(2L, "Esther", 500.0));
    }

    @Test
    @DisplayName("GET /api/contas/{id}/saldo deve retornar 200 OK com o saldo da conta")
    void deveConsultarSaldoComStatus200() throws Exception {
        mockMvc.perform(get("/api/contas/1/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.saldo").value(1000.0));
    }

    @Test
    @DisplayName("GET /api/contas/{id}/saldo deve retornar 400 Bad Request para conta inexistente")
    void deveRetornar400AoConsultarContaInexistente() throws Exception {
        mockMvc.perform(get("/api/contas/99/saldo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    @DisplayName("POST /api/contas/{id}/deposito deve retornar 200 OK e atualizar o saldo")
    void deveRealizarDepositoComStatus200() throws Exception {
        mockMvc.perform(post("/api/contas/1/deposito")
                        .param("valor", "250.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(1250.0))
                .andExpect(jsonPath("$.mensagem").value("Depósito realizado com sucesso."));
    }

    @Test
    @DisplayName("POST /api/contas/{id}/deposito deve retornar 400 Bad Request para valor inválido")
    void deveRetornar400AoDepositarValorInvalido() throws Exception {
        mockMvc.perform(post("/api/contas/1/deposito")
                        .param("valor", "-100.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    @DisplayName("POST /api/contas/transferencia deve retornar 200 OK ao transferir com saldo suficiente")
    void deveRealizarTransferenciaComStatus200() throws Exception {
        mockMvc.perform(post("/api/contas/transferencia")
                        .param("idOrigem", "1")
                        .param("idDestino", "2")
                        .param("valor", "300.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Transferência realizada com sucesso."));

        mockMvc.perform(get("/api/contas/1/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(700.0));

        mockMvc.perform(get("/api/contas/2/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(800.0));
    }

    @Test
    @DisplayName("POST /api/contas/transferencia deve retornar 400 Bad Request quando o saldo for insuficiente")
    void deveRetornar400NaTransferenciaComSaldoInsuficiente() throws Exception {
        mockMvc.perform(post("/api/contas/transferencia")
                        .param("idOrigem", "1")
                        .param("idDestino", "2")
                        .param("valor", "5000.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Saldo insuficiente na conta de origem."));
    }
}
