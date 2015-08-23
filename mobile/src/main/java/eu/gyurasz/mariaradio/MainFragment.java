package eu.gyurasz.mariaradio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.gyurasz.mariaradio.mountpoint.MountPoint;
import eu.gyurasz.mariaradio.mountpoint.MountPointAdapter;
import eu.gyurasz.mariaradio.program.Program;

public class MainFragment extends Fragment {
    private Spinner spMountPoints;
    private MountPointAdapter mAdapter;

    private TextView tvTitle, tvProgram;
    private MainActivity mainActivity;

    public void mountPointsUpdated(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mainActivity = (MainActivity) getActivity();
        mAdapter = new MountPointAdapter(getActivity(), R.layout.mount_point_item, R.layout.mount_point_drop_item, mainActivity.getMountPoints());

        spMountPoints = (Spinner) rootView.findViewById(R.id.spMountPoints);
        spMountPoints.setAdapter(mAdapter);

        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvProgram = (TextView) rootView.findViewById(R.id.tvProgram);

        return rootView;
    }

    public void updateStatus(){
        Program program = mainActivity.getPrograms().get(0);

        tvTitle.setText(program.getTitle());
        tvProgram.setText(program.getDescription());
    }
}
