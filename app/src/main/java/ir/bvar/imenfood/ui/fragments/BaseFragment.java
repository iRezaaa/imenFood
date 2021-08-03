package ir.bvar.imenfood.ui.fragments;

import android.support.v4.app.Fragment;

import ir.bvar.imenfood.App;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public abstract class BaseFragment extends Fragment {
    protected boolean isOnline() {
        return App.getInstance().isOnline();
    }
}
