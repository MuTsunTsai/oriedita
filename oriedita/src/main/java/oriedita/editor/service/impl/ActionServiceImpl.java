package oriedita.editor.service.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import oriedita.editor.action.ActionHandler;
import oriedita.editor.action.ActionType;
import oriedita.editor.action.OrieditaAction;
import oriedita.editor.action.ActionService;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ActionServiceImpl implements ActionService {
    private final Map<ActionType, OrieditaAction> registeredActions;

    @Inject
    public ActionServiceImpl(@Any Instance<OrieditaAction> actions) {
        registeredActions = new HashMap<>();
        // actions.handles().forEach(this::registerAction);
    }

    @Override
    public void registerAction(ActionType actionType, OrieditaAction orieditaAction) {
        registeredActions.put(actionType, orieditaAction);
    }

    // public void registerAction(Instance.Handle<OrieditaAction> handle) {
    //     ActionHandler annotation = getActionHandlerQualifier(handle.getBean());

    //     registeredActions.put(annotation.value(), handle.get());
    // }

    private ActionHandler getActionHandlerQualifier(Bean<OrieditaAction> bean) {
        return bean.getQualifiers().stream().<ActionHandler>mapMulti((q, consumer) -> {
            if (q instanceof ActionHandler) consumer.accept((ActionHandler)q);
        }).findFirst().orElseThrow();
    }

    @Override
    public Map<ActionType, OrieditaAction> getAllRegisteredActions() {
        return registeredActions;
    }

}
