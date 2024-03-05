package fr.rossi.biglistdownload.dataload;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

        @Inject
        Dataload dataload;

        @Override
        public int run(String... args) throws Exception {
            this.dataload.init();
            Quarkus.waitForExit();
            return 0;
        }
    }
}