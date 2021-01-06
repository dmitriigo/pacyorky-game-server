package ee.pacyorky.gameserver.gameserver.config;


import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class DatabaseInitializationConfiguration {

    public static final String path = "\\src\\main\\resources\\sql\\";

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void loadAllData() {
        Set<String> folders = new HashSet<>();
        folders.add("inital\\");
        folders.add("details\\");
//        folders.add("structure\\");
        String userDir = System.getProperty("user.dir");

        for (String nameFolder : folders) {
            String pathToSqlFolder = userDir + path + nameFolder;
            File sqlFolder = new File(pathToSqlFolder);
            if (sqlFolder.listFiles() != null) {
                for (File sql : Objects.requireNonNull(sqlFolder.listFiles())) {
                    String pathToSqlFile = "\\sql\\" + nameFolder + sql.getName();
                    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(pathToSqlFile));
                    resourceDatabasePopulator.execute(dataSource);
                }
            }
        }
    }
}