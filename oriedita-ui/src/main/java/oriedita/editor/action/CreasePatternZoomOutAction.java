package oriedita.editor.action;

import javax.enterprise.context.ApplicationScoped;

import java.awt.event.ActionEvent;

@ApplicationScoped
@ActionHandler(ActionType.creasePatternZoomOutAction)
public class CreasePatternZoomOutAction extends AbstractCreasePatternZoomAction{

    @Override
    public void actionPerformed(ActionEvent e) {
        zoom(1);
    }
}
