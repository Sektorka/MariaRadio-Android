package eu.gyurasz.mariaradio.program;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import eu.gyurasz.mariaradio.R;

public class ProgramAdapter extends ArrayAdapter<Program> {
    protected int resource;

    public ProgramAdapter(Context context, int resource, List<Program> programs) {
        super(context, resource, programs);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View row;
        final Program program = getItem(position);
        final ProgramHolder holder;

        if (convertView == null) {
            holder = new ProgramHolder();
            row = ((LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, parent, false);

            holder.tvTitle = (TextView)row.findViewById(R.id.tvTitle);
            holder.tvDesc = (TextView)row.findViewById(R.id.tvDesc);
            holder.tvTime = (TextView)row.findViewById(R.id.tvTime);
            holder.tvDate = (TextView)row.findViewById(R.id.tvDate);

            row.setTag(holder);
        }
        else {
            row = convertView;
            holder = (ProgramHolder) row.getTag();
        }


        holder.tvTitle.setText(program.getTitle());
        holder.tvDesc.setText(program.getDescription());
        holder.tvTime.setText(new SimpleDateFormat("HH:mm").format(program.getDateTime()));
        holder.tvDate.setText(
                WordUtils.capitalize(
                        new SimpleDateFormat("MMM dd.\r\nEEEE ").format(program.getDateTime())
                )
        );

        row.setBackgroundColor(row.getResources().getColor(program.isCurrent() ? R.color.currentProgram : R.color.defaultBG));

        return row;
    }

    private static class ProgramHolder {
        public TextView tvTitle, tvDesc, tvTime, tvDate;
    }
}
