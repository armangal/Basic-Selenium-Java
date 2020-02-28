package localbrowsers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class A1 {

    public static void main(String[] args)
        throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 768);
        deviceMetrics.put("height", 1024);
        deviceMetrics.put("pixelRatio", 2);
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent",
                            "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53");
        Map<String, Object> chromeOptions = new HashMap<>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        WebDriver driver = new ChromeDriver(capabilities);
        // ChromeDriver driver = new ChromeDriver(); // init chrome driver
        try {

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // driver.manage().window().maximize();

            driver.get("https://nj-betamerica-dge.staging.sbtech.com/en/sports/");

            // Tell webdriver to wait
            WebDriverWait wait = new WebDriverWait(driver, 10);


            // Screenshot screenshot = new AShot().takeScreenshot(driver);
            // ImageIO.write(screenshot.getImage(), "jpg", new File("/users/armangal/ashot/ElementScreenshot-" + UUID.randomUUID().toString() + ".jpg"));

            boolean continueFlow = false;
            while (!continueFlow) {
                try {
                    WebElement login = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(.,'Login')]")));
                    System.out.println("Login click");
                    login.click();
                    continueFlow = true;
                } catch (Exception e) {
                    Thread.sleep(1000);
                }
            }

            takeSnapShot(driver, "/users/armangal/ashot/pic1-" + UUID.randomUUID().toString() + ".jpg");

            System.out.println("Enter username");
            WebElement userName = driver.findElement(By.id("Center_LoginResponsiveBlock_17114-responsive-login-name-input"));
            userName.sendKeys("SBTest1v3dot9");

            System.out.println("Enter password");
            WebElement pass = driver.findElement(By.id("Center_LoginResponsiveBlock_17114-responsive-login-password-input"));
            pass.sendKeys("Sb123456");

            System.out.println("Click Login");
            WebElement loginBtn = driver.findElement(By.id("Center_LoginResponsiveBlock_17114-submit-button"));
            loginBtn.click();

            Boolean loginDiv = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#pop-up-login > div")));
            System.out.println("Login div was removed");

            WebElement casino = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(.,'Casino')]")));
            casino.click();
            System.out.println("Clicked casino");
            Thread.sleep(5000);

            WebElement rocketMan = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"High Limit\"]/div[1]/h2")));
            System.out.println("Casino games visible");

            driver.findElement(By.xpath("//*[@id=\"High Limit\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/div[2]")).click();
            System.out.println("Clicked game");

            Thread.sleep(12000);

            WebDriver casinoFrame = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("desktop_casinoGameHolder_inner")));
            System.out.println("Found casino iframe and switched to it");

            WebDriverWait casinoWait = new WebDriverWait(casinoFrame, 40);
            WebElement spin = casinoWait.until(ExpectedConditions.elementToBeClickable(By.id("spinButtonArea")));
            System.out.println("Spin visible and ready");
            for (int i = 0; i < 10; i++) {
                System.out.println("Spin click:" + i);
                spin.click();
                Thread.sleep(5000);
                // takeSnapShot(casinoFrame, "/users/armangal/ashot/cas-" + UUID.randomUUID().toString() + ".jpg");
                Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(3000)).takeScreenshot(casinoFrame);
                ImageIO.write(screenshot.getImage(), "jpg", new File("/users/armangal/ashot/cas-" + i + "-" + UUID.randomUUID().toString() + ".jpg"));

                WebElement amount = casinoFrame.findElement(By.xpath("//*[@id=\"balanceAmount\"]"));
                System.out.println("Balance after:" + amount.getText());
            }
            System.out.println(casinoFrame.getCurrentUrl());

            Thread.sleep(3000);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public static void takeSnapShot(WebDriver webdriver, String fileWithPath)
        throws Exception {

        // Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

        // Call getScreenshotAs method to create image file

        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        // Move image file to new destination

        File DestFile = new File(fileWithPath);

        // Copy file at destination

        FileUtils.copyFile(SrcFile, DestFile);

    }

}
