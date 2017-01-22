import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.orekyuu.bts4j.BugReport;
import net.orekyuu.bts4j.BugReportBuilder;
import net.orekyuu.bts4j.BugReportService;
import net.orekyuu.bts4j.BugReportServiceBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("電卓");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        String token = "d8684659-6bb1-46c0-93f8-cf55fa2c40d0";
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            BugReportService service = new BugReportServiceBuilder("http://localhost:18080", token).build();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            String stacktrace = sw.toString();

            try {
                BugReport report = new BugReportBuilder()
                        .title(e.getClass().getName())
                        .description("")
                        .log("")
                        .assignUserId(1)
                        .stacktrace(stacktrace)
                        .runtimeInfo(System.getProperty("os.name"))
                        .version("1.0.0")
                        .build();
                service.sendReport(report);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        });
        launch(args);
    }
}
