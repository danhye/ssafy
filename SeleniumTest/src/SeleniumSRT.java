import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class SeleniumSRT {

	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		
		driver.get("https://etk.srail.kr/main.do");
		
		System.out.println(driver.getCurrentUrl());
		
		// 현재 페이지의 핸들을 저장해두기
		String mainPage = driver.getWindowHandle();
		
		// 모든 핸들취득
		Set<String> handles = driver.getWindowHandles();
		
		// 현재 핸들을 제외한 모든 핸들을 닫아주기 (팝업창 대응)
		for (String handle : handles) {
			if (handle.equals(mainPage)) continue; 
			driver.switchTo().window(handle).close();
		}
		
		// 메인페이지 이외의 페이지가 전부 닫혔으므로, 메인페이지로 돌아가기
		driver.switchTo().window(mainPage);
		
		System.out.println(driver.getCurrentUrl());
		
		// 출발역을 Selenium의 Select객체 형태로 취득
		Select departure = new Select(driver.findElement(By.cssSelector("#dptRsStnCd")));
		departure.selectByVisibleText("대전");
		
		// 도착역 취득
		Select arrival = new Select(driver.findElement(By.cssSelector("#arvRsStnCd")));
		arrival.selectByValue("0551");
		
		// 날짜 변경해보기
		// readonly 객체이므로 직접 값을 집어넣는건 불가능
//		WebElement calandar = driver.findElement(By.cssSelector("#search-form > fieldset > div:nth-child(10) > div > input.calendar1"));
//		calandar.sendKeys("2025.07.19");
		
		WebElement calendarBtn = driver.findElement(By.cssSelector("#search-form > fieldset > div:nth-child(10) > div > input.calendar2"));
		calendarBtn.click();
		
		//화면 전환필요
		
		// 모든 핸들 새로취득
		handles = driver.getWindowHandles();
		
		// 현재 핸들을 제외한 모든 핸들을 닫아주기 (팝업창 대응)
		for (String handle : handles) {
			if (handle.equals(mainPage)) continue; 
			driver.switchTo().window(handle);
		}
		
		// 날짜를 찾아서 클릭
		WebElement calendarClick = driver.findElement(By.cssSelector("#wrap > div.contents.etk-calendar > div.area > div.calendar.this > table > tbody > tr:nth-child(3) > td.sat > a"));
		calendarClick.click();
		
		// 메인페이지로 돌아가기
		driver.switchTo().window(mainPage);
		
		// index로 선택해보기
		Select dptTime = new Select(driver.findElement(By.cssSelector("#dptTm")));
		dptTime.selectByIndex(2);
	}

}
