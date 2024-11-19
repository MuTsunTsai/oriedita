package oriedita.editor.handler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import oriedita.editor.canvas.MouseMode;

@ApplicationScoped
@Handles(MouseMode.SELECT_POLYGON_66)
public class MouseHandlerSelectPolygon extends BaseMouseHandlerPolygon {
    @Inject
    public MouseHandlerSelectPolygon() {
    }

}
