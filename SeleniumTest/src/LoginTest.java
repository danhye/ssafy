import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTest {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();

        // 1. edu.ssafy 사이트에 접속
        driver.get("https://edu.ssafy.com");
        Thread.sleep(1000);

        //#userId
        //#userPwd
        // 2. ID / PW 입력
        WebElement idInput = driver.findElement(By.id("userId"));
        WebElement pwInput = driver.findElement(By.id("userPwd"));

        idInput.sendKeys("dan0628@naver.com");
        pwInput.sendKeys("danzzang1!");

        // 3. 로그인 버튼 클릭
        WebElement loginSubmit = driver.findElement(By.cssSelector("#wrap > div > div > div.section > form > div > div.field-set.log-in > div.form-btn > a"));
        loginSubmit.click();
    }
}
