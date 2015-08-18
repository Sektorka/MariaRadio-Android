package eu.gyurasz.mariaradio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import eu.gyurasz.mariaradio.mountpoint.MountPoint;
import eu.gyurasz.mariaradio.mountpoint.MountPointAdapter;

public class MainFragment extends Fragment {
    private Spinner spMountPoints;
    private MountPointAdapter mAdapter;
    private List<MountPoint> mMountPoints;

    public MainFragment() {
        mMountPoints = new ArrayList<MountPoint>();
    }

    public List<MountPoint> getmMountPoints() {
        return mMountPoints;
    }

    public void mountPointsUpdated(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new MountPointAdapter(getActivity(), R.layout.mount_point_item, R.layout.mount_point_drop_item, mMountPoints);

        spMountPoints = (Spinner) rootView.findViewById(R.id.spMountPoints);
        spMountPoints.setAdapter(mAdapter);

        return rootView;
    }
}
