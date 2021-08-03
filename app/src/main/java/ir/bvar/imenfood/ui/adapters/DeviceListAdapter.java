package ir.bvar.imenfood.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ir.bvar.imenfood.App;
import ir.bvar.imenfood.models.Equipment;
import ir.bvar.imenfood.ui.viewholders.DeviceViewHolder;

/**
 * Created by reza on 1/18/18.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private Context context;
    private List<Equipment> equipmentList;

    public DeviceListAdapter(Context context) {
        this.context = context;
        this.equipmentList = App.getInstance().getStaticDataManager().getCloneOfEquipmentList();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceViewHolder(context, parent, equipmentList);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.bindViews(equipmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    @Override
    public long getItemId(int position) {
        return equipmentList.get(position).getEquipmentData().getID();
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }
}
