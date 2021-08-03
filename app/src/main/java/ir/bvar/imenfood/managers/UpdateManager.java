package ir.bvar.imenfood.managers;

import java.util.HashMap;

import ir.bvar.imenfood.interfaces.UpdatableDataViewInterface;

/**
 * Created by rezapilehvar on 19/12/2017 AD.
 */

public class UpdateManager {
    private HashMap<String, UpdatableDataViewInterface> updatableViews = new HashMap<>();

    public void addUpdatableView(String key, UpdatableDataViewInterface updatableDataViewInterface) {
        updatableViews.put(key, updatableDataViewInterface);
    }

    public void removeUpdatableView(String key) {
        updatableViews.remove(key);
    }

    public void updateDataViews() {
        for (UpdatableDataViewInterface updatableDataViewInterface : updatableViews.values()) {
            if (updatableDataViewInterface != null) {
                updatableDataViewInterface.updateData();
            }
        }
    }
}
