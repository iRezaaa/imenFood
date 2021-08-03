package ir.bvar.imenfood.utils;

import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import ir.bvar.imenfood.ui.views.utils.DisableSwipeBehavior;

/**
 * Created by rezapilehvar on 13/2/2018 AD.
 */

public class ViewUtility {
    public static void disableSwipeOnSnackbar(Snackbar snackbar) {
        final Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams lp = layout.getLayoutParams();
                if (lp instanceof CoordinatorLayout.LayoutParams) {
                    ((CoordinatorLayout.LayoutParams) lp).setBehavior(new DisableSwipeBehavior());
                    layout.setLayoutParams(lp);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
