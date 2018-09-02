import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import pages.LiteBoxLoginPage;

/**
 * Created by Александр on 02.09.2018.
 * Тестируем обработку нажатия на ссылки открывающие новые странички в том же окне
 */
public class linksTest {


    @Parameters("browser")
    @BeforeTest
    public void init(@Optional("firefox") String browser) throws InterruptedException{
        Helper.init(browser);
    }

    @AfterTest
    public void killAll(){
        Helper.driver.quit();

    }
    @DataProvider //параметры
    public Object[][] links(){
        return new Object[][]{
                {new LiteBoxLoginPage(Helper.driver).registration, "Регистрация" },
                {new LiteBoxLoginPage(Helper.driver).fogotPass, "Сброс пароля"},
                {new LiteBoxLoginPage(Helper.driver).acceptRegister, "Повторить отправку письма о подтверждении регистрации"},
                {new LiteBoxLoginPage(Helper.driver).emailSendFail, "Не приходят email?"},
                {new LiteBoxLoginPage(Helper.driver).rates, "Тарифы Litebox"},
                {new LiteBoxLoginPage(Helper.driver).support, "Support LiteBox"}
        };
    }

    @Test(dataProvider = "links")
    public void linkClick(WebElement link, String title) throws InterruptedException{
        LiteBoxLoginPage loginPage = new LiteBoxLoginPage(Helper.driver);
        loginPage.click(link);
        Helper.checkWindowTitle(title);
    }


}
