import org.testng.Assert;
import org.testng.annotations.*;
import pages.LiteBoxLoginPage;

/**
 * Created by Александр on 02.09.2018.
 * протестируем кнопку показать/скрыть пароль
 * 1) введем текст в поле ввода пароля
 * 2) проверим значение аттрибута type у поля ввода пароля - если password, значит пароль скрыт
 * 3) нажмем на элемент показать/скрыть пароль
 * 4) проверим значение аттрибута type у поля ввода пароля - если text, значит пароль показывается
 */
public class showPassTest {

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
    public void checkShowHidePasswordBtn() throws InterruptedException{
        LiteBoxLoginPage loginPage = new LiteBoxLoginPage(Helper.driver);
        loginPage.sendText("password", loginPage.idPassword); //вводим текст в поле ввода пароля
        String inputTypePassword = loginPage.inputPasswordField.getAttribute("type"); //получаем значение атрибута type
        System.out.println("тип поля ввода пароля:"+inputTypePassword);
        if (inputTypePassword.equals("password")){ //если тип поля ввода password
            System.out.println("пароль скрыт");
            loginPage.click(loginPage.eyeBtn); // то кликаем на кнопку скрыть/показать пароля
            String inputTypeText = loginPage.inputPasswordField.getAttribute("type"); //получаем значение атрибута type
            System.out.println("тип поля ввода пароля "+ inputTypeText);
            if (inputTypeText.equals("text")){ //если значение атрибута сменилось на text
                System.out.println("пароль показывается"); //пишем об этом
            }
        }
        Assert.assertEquals(loginPage.inputPasswordField.getAttribute("type"),"text"); //сверяем значение атрибута с ожидаемым результатом
    }
}
