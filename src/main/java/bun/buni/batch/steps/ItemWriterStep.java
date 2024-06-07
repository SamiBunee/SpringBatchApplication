package bun.buni.batch.steps;

import bun.buni.entities.Person;
import bun.buni.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWriterStep implements Tasklet {

    @Autowired
    private PersonService personService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("[STEP] Starting Writing Step ♥ ♥ ♥ ♥ ♥ ♥ ");

        @SuppressWarnings("unchecked")
        List<Person> personList = (List<Person>) chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personList");

        personList.forEach( person -> {
            if(person != null) {
                log.info(person.toString());
            }
        });

        personService.saveAll(personList);

        log.info("[STEP] Ending Writing Step ♥ ♥ ♥ ♥ ♥ ♥ ");
        return RepeatStatus.FINISHED;
    }
}
