package oriedita.editor.handler;


import javax.inject.Inject;
import javax.inject.Named;
import oriedita.editor.canvas.CreasePattern_Worker;

import java.util.EnumSet;

public abstract class BaseMouseHandler implements MouseModeHandler {
    @Inject
    @Named("mainCreasePattern_Worker")
    protected CreasePattern_Worker d;

    public BaseMouseHandler() {
    }

    @Override
    public EnumSet<Feature> getSubscribedFeatures() {
        return EnumSet.of(Feature.BUTTON_1);
    }
}
