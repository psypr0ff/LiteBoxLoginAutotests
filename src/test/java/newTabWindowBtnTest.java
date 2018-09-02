import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import pages.LiteBoxLoginPage;

/**
 * Created by Александр on 02.09.2018.
 * Тестируем обработку нажатий на элементы открывающие странички в новом окне
 * используя пары значений: веб-элемент, ожидаемый заголовок открываемой странички странички
 * 1) кликаем на выбранный элемент
 * 2) ждем пока прогрузится страничка в новом окне
 * 3) сверяем заголовок открытой странички с ожидаемым
 * 4) возвращаем все в исходное состояние
 */
public class newTabWindowBtnTest {

    @Parameters("browser")
    @BeforeTest
    public void init(@Optional("chrome") String browser) throws InterruptedException{
        Helper.init(browser);
    }

    @AfterTest
    public void killAll(){
       Helper.driver.quit();
    }

    @DataProvider
    public Object[][] elements(){
        return new Object[][]{
                {new LiteBoxLoginPage(Helper.driver).Logo,"Автоматизация магазина - кассовая программа для розничной торговли, система учета продаж LiteBox"} ,
                {new LiteBoxLoginPage(Helper.driver).btnShop, "Кассовое оборудование для автоматизации магазина розничной торговли"},
                {new LiteBoxLoginPage(Helper.driver).facebook,"Facebook"},
                {new LiteBoxLoginPage(Helper.driver).vk, "LiteBox. Автоматизация торговли. ЕГАИС. 54-ФЗ | ВКонтакте"},
                {new LiteBoxLoginPage(Helper.driver).youtube, "LiteBox - Автоматизация розничной торговли - YouTube"}

        };
    }

    @Test(dataProvider = "elements")
    public void elementClick(WebElement webElement, String title) throws InterruptedException{
        LiteBoxLoginPage loginPage = new LiteBoxLoginPage(Helper.driver);
        loginPage.click(webElement); //кликаем на веб-элемент
        Thread.sleep(3000); //ждем 3 секунды
        Helper.checkNewTabWindowTitle(title); //сверяем заголовок
        Helper.driver.get(Helper.basicURL); // возвращаемся на исходную страничку



    }


}
