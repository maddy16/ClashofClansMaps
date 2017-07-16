package com.logixity.apps.clashofclansmaps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<MapModel> mapsList;
    int category;

    public TabFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TabFragment newInstance(int sectionNumber) {
        TabFragment fragment = new TabFragment();
        fragment.category = sectionNumber;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.war_tab, container, false);
        //Initializing Views
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Object[] objects = NavigationActivity.listsMap.get(NavigationActivity.townHallNum + "");
        if (category == 0)
            mapsList = (List<MapModel>) objects[category];
        else
            mapsList = (List<MapModel>) objects[category-1];
        adapter = new MapsAdapter(mapsList, getContext());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}
