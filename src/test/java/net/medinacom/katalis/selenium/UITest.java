/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package net.medinacom.katalis.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

/**
 *
 * @author aphasan
 */
@TestMethodOrder(OrderAnnotation.class)
public class UITest {

    private static WebDriver driver;
    private static Wait<WebDriver> wait;

    @BeforeAll
    private static void setUp() throws Exception {
        Proxy proxy = new Proxy();
        String proxyStr = "localhost:4200";
        proxy.setHttpProxy(proxyStr);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);

        driver = WebDriverManager.chromedriver().capabilities(options).create();

        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        driver.get("http://localhost:4200");
    }

    @AfterAll
    private static void tearDown() throws Exception {
        delay(2);
        driver.quit();
    }

    @Test
    @Order(1)
    public void testLoadProvinsi() {

        wait.until(
                ExpectedConditions.and(
                        ExpectedConditions.presenceOfElementLocated((By.name("provinsi"))),
                        ExpectedConditions.presenceOfElementLocated((By.name("kabupaten"))),
                        ExpectedConditions.presenceOfElementLocated((By.name("kecamatan"))),
                        ExpectedConditions.presenceOfElementLocated((By.name("kelurahan")))
                )
        );

        Select provinsi = new Select(driver.findElement(By.name("provinsi")));

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        new ByChained(By.name("provinsi"), By.tagName("option")), 1)
        );

        provinsi.getWrappedElement().click();

        delay(2);

        provinsi.getWrappedElement().click();

        assertThat(provinsi.getOptions().size()).isGreaterThan(1);

    }

    @Test
    @Order(2)
    public void testLoadKabupaten() {

        Select provinsi = new Select(driver.findElement(By.name("provinsi")));

        Select kabupaten = new Select(driver.findElement(By.name("kabupaten")));

        for (int i = 1; i <= 3; i++) {
            provinsi.selectByIndex(i);

            wait.until(
                    ExpectedConditions.numberOfElementsToBeMoreThan(
                            new ByChained(By.name("kabupaten"), By.tagName("option")), 1)
            );

            kabupaten.getWrappedElement().click();

            delay(2);

            kabupaten.getWrappedElement().click();

            assertThat(kabupaten.getOptions().size()).isGreaterThan(1);
        }

    }

    @Test
    @Order(3)
    public void testLoadKecamatan() {

        Select provinsi = new Select(driver.findElement(By.name("provinsi")));
        provinsi.selectByIndex(1);

        Select kabupaten = new Select(driver.findElement(By.name("kabupaten")));
        Select kecamatan = new Select(driver.findElement(By.name("kecamatan")));

        for (int i = 1; i <= 3; i++) {
            
            kabupaten.selectByIndex(i);

            wait.until(
                    ExpectedConditions.numberOfElementsToBeMoreThan(
                            new ByChained(By.name("kecamatan"), By.tagName("option")), 1)
            );

            kecamatan.getWrappedElement().click();

            delay(2);

            kecamatan.getWrappedElement().click();

            assertThat(kecamatan.getOptions().size()).isGreaterThan(1);
        }

    }

    @Test
    @Order(4)
    public void testLoadKelurahan() {

        Select provinsi = new Select(driver.findElement(By.name("provinsi")));
        provinsi.selectByIndex(1);
        Select kabupaten = new Select(driver.findElement(By.name("kabupaten")));
        kabupaten.selectByIndex(1);
        Select kecamatan = new Select(driver.findElement(By.name("kecamatan")));
        Select kelurahan = new Select(driver.findElement(By.name("kelurahan")));

        for (int i = 1; i <= 3; i++) {
            
            kecamatan.selectByIndex(i);

            wait.until(
                    ExpectedConditions.numberOfElementsToBeMoreThan(
                            new ByChained(By.name("kelurahan"), By.tagName("option")), 1)
            );

            kelurahan.getWrappedElement().click();

            delay(2);

            kelurahan.getWrappedElement().click();

            assertThat(kecamatan.getOptions().size()).isGreaterThan(1);
        }

    }

    @Test
    @Order(5)
    public void testResetProvinsi() {

        Select provinsi = new Select(driver.findElement(By.name("provinsi")));
        Select kabupaten = new Select(driver.findElement(By.name("kabupaten")));
        Select kecamatan = new Select(driver.findElement(By.name("kecamatan")));
        Select kelurahan = new Select(driver.findElement(By.name("kelurahan")));
        kelurahan.selectByIndex(1);

        delay(2);
        
        provinsi.getWrappedElement().click();
        delay(2);
        provinsi.selectByIndex(0);
        provinsi.getWrappedElement().click();
        
        wait.until(
                ExpectedConditions.and(
                        ExpectedConditions.numberOfElementsToBe(
                            new ByChained(By.name("kabupaten"), By.tagName("option")), 1),
                        ExpectedConditions.numberOfElementsToBe(
                            new ByChained(By.name("kecamatan"), By.tagName("option")), 1),
                        ExpectedConditions.numberOfElementsToBe(
                            new ByChained(By.name("kelurahan"), By.tagName("option")), 1)
                )
        );
        
        assertThat(kabupaten.getOptions().size()).isOne();
        assertThat(kecamatan.getOptions().size()).isOne();
        assertThat(kelurahan.getOptions().size()).isOne();

    }

    private static void delay(int d) {
        try {
            TimeUnit.SECONDS.sleep(d);
        } catch (InterruptedException ex) {
            Logger.getLogger(UITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
