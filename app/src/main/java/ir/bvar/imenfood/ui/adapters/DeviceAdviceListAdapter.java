package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.models.Equipment;
import ir.bvar.imenfood.ui.viewholders.DeviceAdviceViewHolder;

/**
 * Created by rezapilehvar on 14/2/2018 AD.
 */

public class DeviceAdviceListAdapter extends RecyclerView.Adapter<DeviceAdviceViewHolder> {
    private Context context;
    private List<Equipment> deviceList;

    public DeviceAdviceListAdapter(Context context, List<Equipment> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public DeviceAdviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceAdviceViewHolder(context, parent, deviceList);
    }

    @Override
    public void onBindViewHolder(DeviceAdviceViewHolder holder, int position) {
        holder.bindViews(deviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
