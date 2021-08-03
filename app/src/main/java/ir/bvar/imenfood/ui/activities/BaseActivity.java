package ir.bvar.imenfood.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.bvar.imenfood.App;
import ir.bvar.imenfood.interfaces.UpdatableDataViewInterface;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public abstract class BaseActivity extends AppCompatActivity implements UpdatableDataViewInterface {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getActivityManager().setCurrentActivity(this);
    }

    @Override
    public void updateData() {

    }

    protected boolean isOnline() {
        return App.getInstance().isOnline();
    }
}