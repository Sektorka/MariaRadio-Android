package eu.gyurasz.mariaradio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import eu.gyurasz.mariaradio.mountpoint.MountPoint;
import eu.gyurasz.mariaradio.mountpoint.MountPointAdapter;
import eu.gyurasz.mariaradio.program.Program;

public class MainFragment extends Fragment implements View.OnClickListener {
    private Spinner spMountPoints;
    private MountPointAdapter mAdapter;

    private TextView tvTitle, tvProgram;
    private MainActivity mainActivity;
    private ImageView ivPlay, ivMute;

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

        ivPlay = (ImageView) rootView.findViewById(R.id.ivPlay);
        ivMute = (ImageView) rootView.findViewById(R.id.ivMute);

        ivPlay.setOnClickListener(this);
        ivMute.setOnClickListener(this);

        return rootView;
    }

    public void updateStatus(){
        Program program = mainActivity.getPrograms().get(0);

        tvTitle.setText(program.getTitle());
        tvProgram.setText(program.getDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivPlay:
                new RadioPlayer();

                RadioPlayer.startActionPlay(
                        getActivity(),
                        ((MountPoint) spMountPoints.getSelectedItem()).getStreamUrl()
                );
                break;
            case R.id.ivMute:
                break;
        }
    }

    public ImageView getIvPlay() {
        return ivPlay;
    }
}
