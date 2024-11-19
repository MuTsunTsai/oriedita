package oriedita.editor.handler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import oriedita.editor.canvas.MouseMode;

@ApplicationScoped
@Handles(MouseMode.UNSELECT_POLYGON_67)
public class MouseHandlerUnselectPolygon extends BaseMouseHandlerPolygon {
    @Inject
    public MouseHandlerUnselectPolygon() {
    }

}
