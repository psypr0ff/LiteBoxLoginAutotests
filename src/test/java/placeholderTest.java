import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LiteBoxLoginPage;


/**
 * Created by Александр on 03.09.2018.
 * проверяем реакцию плейсхолдеров полей ввода на клик:
 * 1) заходим на страничку
 * 2) получаем значения CSS атрибутов плэйсхолдера выбранного поля ввода
 * 3) кликаем на выбранное поле ввода
 * 4) получаем значение CSS атрибутов плэйсхолдера выбранного поля ввода после клика
 * 5) если значения CSS атрибутов до клика и после клика отличаются - тест пройден
 */
public class placeholderTest {

    @Parameters ("browser")
    @BeforeTest
    public void init(@Optional("chrome") String browser)throws InterruptedException{
        Helper.init(browser);
    }

    @AfterTest
    public void killAll(){
        Helper.driver.quit();
    }
    @DataProvider
    public Object[][] placeholders(){
            return new Object[][]{
                {new LiteBoxLoginPage(Helper.driver).loginPlaceholder}, // веб-элемент плэйсхолдер поля ввода логина
                {new LiteBoxLoginPage(Helper.driver).passwordPlaceholder} //веб-элемент плэйсхолдер поля ввода пароля
            };
    }

    @Test(dataProvider = "placeholders")
    public void placeholderAnimationCheck(WebElement webElement) throws InterruptedException{
        LiteBoxLoginPage loginPage = new LiteBoxLoginPage(Helper.driver);
        String[] beforeClickPlaceholderCss = Helper.placeholderCssAttributes(webElement); //получаем значения CSS атрибутов сразу после загрузки странички
        System.out.println("Значения CSS атрибутов плэйсхолдера поля "+webElement.getText() +" после загрузки станички:");
        Helper.printResults(beforeClickPlaceholderCss);
        loginPage.click(webElement); //кликаем на поле ввода
        System.out.println("Значения CSS атрибуто вплэйсхолдера поля "+webElement.getText() +"  после клика:");
        String[] afterClickPlaceholderCss = Helper.placeholderCssAttributes(webElement); //получаем значения CSS атрибутов после клика на поле ввода
        Helper.printResults(afterClickPlaceholderCss);
        Assert.assertTrue(!beforeClickPlaceholderCss.equals(afterClickPlaceholderCss)); //сравниваем значения CSS атрибутов до и после клика на поле ввода
    }

}
