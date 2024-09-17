package ma.zyn.app.app;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeAll;


public class AppTest {


    @BeforeAll
    public static void beforeAll() {
        System.setProperty("karate.env", "dev");
    }

    @Karate.Test
    Karate InfoAppTest() {
        return Karate.run("actuator/AppStatusTest").relativeTo(getClass());
    }



}
