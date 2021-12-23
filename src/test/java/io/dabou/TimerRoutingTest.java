package io.dabou;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.apache.camel.quarkus.test.support.process.QuarkusProcessExecutor;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.zeroturnaround.exec.StartedProcess;

import static org.awaitility.Awaitility.await;

@QuarkusTest
public class TimerRoutingTest {

    private static final String PACKAGE_TYPE = System.getProperty("quarkus.package.type");

    @Test
    public void testTimerLogMain() throws IOException {
        QuarkusRunnerExecutor quarkusProcessExecutor = new QuarkusRunnerExecutor();
        StartedProcess process = quarkusProcessExecutor.start();

        awaitStartup(quarkusProcessExecutor);

        try {
            File quarkusLogFile = getQuarkusLogFile();
            await().atMost(10L, TimeUnit.SECONDS).pollDelay(1, TimeUnit.SECONDS).until(() -> {
                String log = FileUtils.readFileToString(quarkusLogFile, StandardCharsets.UTF_8);
                return log.contains("Greetings");
            });
        } finally {
            if (process != null && process.getProcess().isAlive()) {
                process.getProcess().destroy();
            }
        }
    }

    private File getQuarkusLogFile() {
        String pathPrefix = "target/quarkus";
        if (isNative()) {
            pathPrefix += "-native";
        }
        return new File(pathPrefix + ".log");
    }

    private void awaitStartup(QuarkusProcessExecutor quarkusProcessExecutor) {
        await().atMost(10, TimeUnit.SECONDS).pollDelay(1, TimeUnit.SECONDS).until(() -> {
            return isApplicationHealthy(quarkusProcessExecutor.getHttpPort());
        });
    }

    private boolean isApplicationHealthy(int port) {
        try {
            int status = RestAssured.given().port(port).get("/q/health").then().extract().statusCode();
            return status == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isNative() {
        return PACKAGE_TYPE != null && PACKAGE_TYPE.equals("native");
    }

    static final class QuarkusRunnerExecutor extends QuarkusProcessExecutor {
        @Override
        protected List<String> command(String... args) {
            List<String> command = super.command(args);
            if (isNative()) {
                command.add("-Dquarkus.log.file.path=target/quarkus-native.log");
            } else {
                command.add(1, "-Dquarkus.log.file.path=target/quarkus.log");
            }
            command.add("Greetings");
            command.add("2");
            return command;
        }

    }
}