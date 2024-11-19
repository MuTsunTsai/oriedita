package oriedita.editor.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import oriedita.editor.canvas.animation.EaseOutInterpolation;
import oriedita.editor.canvas.animation.Interpolation;

public class InterpolationFactory {
    @Named("default_animation_interpolation")
    @Produces
    @ApplicationScoped
    public static Interpolation defaultInterpolation() {
        return new EaseOutInterpolation();
    }
}
