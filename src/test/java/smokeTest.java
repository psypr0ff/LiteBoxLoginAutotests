import org.testng.annotations.*;
import pages.LiteBoxLoginPage;

/**
 * Created by Александр on 03.09.2018.
 */
public class smokeTest {

    @Parameters("browser")
    @BeforeTest
    public void init(@Optional("chrome") String browser) throws InterruptedException{
        Helper.init(browser);
    }

    @AfterTest
    public void killAll(){
        Helper.driver.quit();
    }

    @Test
    public void smoke() throws InterruptedException{
        Helper.authorization(Helper.validLogin, Helper.validPassword); //авторизуемся
        Helper.checkWindowTitle(Helper.storePageTitle); //проверяем, соответствует ли открывшаяся страничка ожидаемой
    }
}
