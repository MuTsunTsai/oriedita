package oriedita.editor.action;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import oriedita.editor.service.TaskExecutorService;

import java.awt.event.ActionEvent;

@ApplicationScoped
@ActionHandler(ActionType.haltAction)
public class HaltAction extends AbstractOrieditaAction {
    @Inject
    @Named("camvExecutor")
    TaskExecutorService camvTaskExecutor;
    @Inject
    @Named("foldingExecutor")
    TaskExecutorService foldingTaskExecutor;

    @Inject
    public HaltAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        camvTaskExecutor.stopTask();
        foldingTaskExecutor.stopTask();
    }
}
