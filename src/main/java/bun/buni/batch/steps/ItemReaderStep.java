package bun.buni.batch.steps;

import bun.buni.entities.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemReaderStep implements Tasklet {
    @Autowired
    //ResourceLoader sirve para cargar archivos de configuración.
    private ResourceLoader resourceLoader;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("[STEP] Starting Reading Step ♥ ♥ ♥ ♥ ♥ ♥ ");

        Reader reader = new FileReader(resourceLoader
                .getResource("classpath:files/destination/persons.csv")
                .getFile());

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build();

        List<Person> personList = new ArrayList<>();

        String[] currentLine;

        while ((currentLine = csvReader.readNext()) != null) {
            Person person = new Person();
            person.setName(currentLine[0]);
            person.setLastName(currentLine[1]);
            person.setAge(Integer.parseInt(currentLine[2]));
            personList.add(person);
        }

        csvReader.close();
        reader.close();

        chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("personList", personList);

        log.info("[STEP] Ending Reading Step ♥ ♥ ♥ ♥ ♥ ♥ ");
        return RepeatStatus.FINISHED;
    }
}
