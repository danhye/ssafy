import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {

	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		
		driver.get("https://www.naver.com/");
		
		WebElement financeBtn = driver.findElement(By.cssSelector("#shortcutArea > ul > li:nth-child(6) > a"));
		financeBtn.click();
		
//		driver.get("https://finance.naver.com/");

		// 페이지 전환이 안되는 이유 확인팁
//		click() 이후 driver.current_url을 출력해 보세요. 정말 증권 페이지로 이동했는지 확인 가능.
//		time.sleep(5)으로 일시적으로 대기시켜 테스트해보는 것도 디버깅에 유용.
		
		// 보안설정 -> 봇 감지 또는 동적난독화를 하는경우 요소가 일시적으로 숨겨짐
		// 회피방법 몇가지 (GPT생성)
//		1. User-Agent를 브라우저와 동일하게 설정
//		2. headless=False 모드에서 눈으로 DOM 확인
//		3. 때로는 Selenium 대신 BeautifulSoup + requests가 더 나을 수 있음 (단, 동적 페이지는 Selenium이 유리)
		
		// 현재 페이지 확인
		System.out.println(driver.getCurrentUrl());
		
		// 현재 페이지의 핸들을 저장해두기
		String mainPage = driver.getWindowHandle();
		
		// 모든 핸들취득
		Set<String> handles = driver.getWindowHandles();
		
		// 클릭이벤트 이후 새로 생성된 핸들을 찾아서 페이지를 변경해주기
		for (String handle : handles) {
			if (handle.equals(mainPage)) continue; 
			driver.switchTo().window(handle);
		}
		
		// 핸들 변경이 됬는지 확인
		System.out.println(driver.getCurrentUrl());
		
		// 1. input박스를 찾아서 입력
//		WebElement searchInput = driver.findElement(By.cssSelector("#stock_items"));
//		System.out.println(searchInput);
//		searchInput.sendKeys("삼성전자");
//		searchInput.sendKeys(Keys.ENTER);
		
		// 2. 뉴스 리스트를 취득
		List<WebElement> newsList = driver.findElements(By.cssSelector("#content > div.article > div.section > div.news_area._replaceNewsLink > div > ul"));
		for (WebElement news : newsList) {
			System.out.println(news.getText());
		}
		
		
	}

}
