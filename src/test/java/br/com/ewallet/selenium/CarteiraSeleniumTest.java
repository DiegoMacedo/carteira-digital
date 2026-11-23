package br.com.ewallet.selenium;

import br.com.ewallet.model.Conta;
import br.com.ewallet.repository.ContaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarteiraSeleniumTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ContaRepository contaRepository;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        contaRepository.save(new Conta(1L, "Diego", 1000.0));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Deve consultar saldo e realizar depósito pela interface HTML")
    void deveRealizarDepositoPelaInterfaceWeb() {
        driver.get("http://localhost:" + port + "/index.html");
        //System.out.println("URL: " + driver.getCurrentUrl());
        //System.out.println("TITULO: " + driver.getTitle());
        //System.out.println("HTML: " + driver.getPageSource());


        //driver.findElement(By.id("btnConsultar")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnConsultar"))).click();
        wait.until(ExpectedConditions.textToBe(By.id("saldoAtual"), "1000.00"));


        WebElement campoValor = driver.findElement(By.id("valorDeposito"));
        campoValor.clear();
        campoValor.sendKeys("250");
        driver.findElement(By.id("btnDepositar")).click();


        wait.until(ExpectedConditions.textToBe(By.id("saldoAtual"), "1250.00"));
        String mensagem = driver.findElement(By.id("mensagem")).getText();

        assertEquals("1250.00", driver.findElement(By.id("saldoAtual")).getText());
        assertEquals("Depósito realizado com sucesso.", mensagem);
    }
}