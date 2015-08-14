package eu.gyurasz.mariaradio.program;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import eu.gyurasz.mariaradio.R;

public class ProgramFragment extends Fragment{
    private AbsListView mListView;
    private ProgramAdapter mAdapter;
    private List<Program> mPrograms;

    public ProgramFragment() {
        mPrograms = new ArrayList<Program>();
    }

    public List<Program> getPrograms() {
        return mPrograms;
    }

    public void programsUpdated(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program_list, container, false);

        // Set the adapter
        mAdapter = new ProgramAdapter(getActivity(), R.layout.program_item, mPrograms);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);

        return view;
    }

}
