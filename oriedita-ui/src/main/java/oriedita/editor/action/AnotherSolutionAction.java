package oriedita.editor.action;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import oriedita.editor.databinding.FoldedFiguresList;
import oriedita.editor.drawing.FoldedFigure_Drawer;
import oriedita.editor.service.FoldingService;

import java.awt.event.ActionEvent;

@ApplicationScoped
@ActionHandler(ActionType.anotherSolutionAction)
public class AnotherSolutionAction extends AbstractOrieditaAction {
    @Inject
    FoldedFiguresList foldedFiguresList;

    @Inject
    FoldingService foldingService;

    @Inject
    public AnotherSolutionAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FoldedFigure_Drawer selectedItem = foldedFiguresList.getActiveItem();
        if (selectedItem != null) {
            foldingService.foldAnother(selectedItem);
        }
    }
}
