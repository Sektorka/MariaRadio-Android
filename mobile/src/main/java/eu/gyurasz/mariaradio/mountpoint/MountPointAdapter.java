package eu.gyurasz.mariaradio.mountpoint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import eu.gyurasz.mariaradio.R;
import eu.gyurasz.mariaradio.program.Program;

public class MountPointAdapter extends ArrayAdapter<MountPoint> {
    protected int item, dropItem;

    public MountPointAdapter(Context context, int item, int dropItem, List<MountPoint> mountPoints) {
        super(context, item, mountPoints);
        this.dropItem = dropItem;
        this.item = item;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, dropItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, item);
    }

    protected View createViewFromResource(int position, View convertView, ViewGroup parent, int res){
        final View row;
        final MountPoint mountPoint = getItem(position);
        final MountPointHolder holder;

        if (convertView == null) {
            holder = new MountPointHolder();
            row = ((LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(res, parent, false);

            holder.tvTitle = (TextView)row.findViewById(R.id.tvTitle);
            holder.tvDesc = (TextView)row.findViewById(R.id.tvDescription);
            holder.tvBitrate = (TextView)row.findViewById(R.id.tvBitrate);

            row.setTag(holder);
        }
        else {
            row = convertView;
            holder = (MountPointHolder) row.getTag();
        }


        holder.tvTitle.setText(mountPoint.getTitle());
        holder.tvDesc.setText(mountPoint.getDescription());
        holder.tvBitrate.setText(mountPoint.getBitrate() + "kbit/s");

        return row;
    }

    private static class MountPointHolder {
        public TextView tvTitle, tvDesc, tvBitrate;
    }
}
