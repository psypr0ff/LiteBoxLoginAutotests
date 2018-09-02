import org.testng.Assert;
import org.testng.annotations.*;
import pages.LiteBoxLoginPage;

/**
 * Created by Александр on 02.09.2018.
 * проверка работы чекбокса "Запомнить меня"
 * тестовые данные: пары значений - ожидаемый заголовок странички и статус чекбокса
 * 1) проверяем начальное состояние чекбокса
 * 2) меняем состояние чекбокса согласно тестовым данным
 * 3) авторизуемся
 * 4) возвращаемся на страничку авторизации
 * 5) сверяем заголовок открытой странички с ожидаемым значением
 */
public class rememberMeTest {

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
    public Object[][] testData() {
        return new Object[][]{
                {Helper.loginPageTitle, false},
                {Helper.storePageTitle, true}
        };
    }

    @Test (dataProvider = "testData")
    public void checkSessionSave(String title, Boolean checkboxStatus)throws InterruptedException{
        LiteBoxLoginPage loginPage = new LiteBoxLoginPage(Helper.driver);
        System.out.println("Начальное значение чек-бокса Запомнить меня - "+loginPage.idRemember.isSelected()); //выводим начальный статус чекбокса
        System.out.println("Тестовое значение чек-бокса Запомнить меня - "+ checkboxStatus); //выводим тестовое статус чекбокса
        if (!checkboxStatus){
            if (loginPage.idRemember.isSelected()){
                loginPage.click(loginPage.idRemember); //кликаем на чекбокс
            }
        } else
        if(!loginPage.idRemember.isSelected()){
            loginPage.click(loginPage.idRemember);
        }
        Helper.authorization(Helper.validLogin, Helper.validPassword); //авторизуемся
        System.out.println("Заголовок открывшейся странички - "+Helper.driver.getTitle()); //выводим заголовок
        Helper.driver.get(Helper.basicURL); //возвращаемся на страничку авторизации
        System.out.println("Вернулись на страничку авторизации");
        System.out.println("Заголовок открывшейся странички - "+Helper.driver.getTitle()); //выводим заголовок
        Assert.assertEquals(title, Helper.driver.getTitle()); //сверяем заголовок с ожидаемым результатом
    }
}
