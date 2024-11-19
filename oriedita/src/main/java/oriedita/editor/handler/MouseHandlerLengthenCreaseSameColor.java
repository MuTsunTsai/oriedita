package oriedita.editor.handler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import oriedita.editor.canvas.MouseMode;

@ApplicationScoped
@Handles(MouseMode.LENGTHEN_CREASE_SAME_COLOR_70)
public class MouseHandlerLengthenCreaseSameColor extends MouseHandlerLengthenCrease {
    @Inject
    public MouseHandlerLengthenCreaseSameColor() {
    }

}
