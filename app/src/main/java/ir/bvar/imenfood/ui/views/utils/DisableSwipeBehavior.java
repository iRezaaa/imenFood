package ir.bvar.imenfood.ui.views.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.view.View;

/**
 * Created by rezapilehvar on 13/2/2018 AD.
 */

public class DisableSwipeBehavior extends SwipeDismissBehavior<Snackbar.SnackbarLayout> {
    @Override
    public boolean canSwipeDismissView(@NonNull View view) {
        return false;
    }
}
