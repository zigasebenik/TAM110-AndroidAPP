package com.example.tam110.ui.main.devices;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tam110.R;
import com.example.tam110.ui.main.devices.data.DeviceData;
import com.example.tam110.ui.main.devices.data.DeviceData.Device;
import com.example.tam110.ui.main.lights.LightsFragment;
import com.example.tam110.ui.main.lights.data.LightsData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.example.tam110.ui.main.devices.data.DeviceData.ITEMS_INITIALIZED;
import static com.example.tam110.ui.main.devices.data.DeviceData.addDevice;
import static com.example.tam110.ui.main.lights.data.LightsData.addLight;

public class DevicesFragment extends Fragment
{

    public static final String UPDATE_DEVICES_UI = "UPDATE_DEVICES_UI";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DevicesFragment()
    {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DevicesFragment newInstance(int columnCount)
    {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null)
        {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.devices_fragment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if(ITEMS_INITIALIZED == false)
            {
                List<String> arrayOfJson = Arrays.asList(this.getResources().getStringArray(R.array.Devices));

                for(int i=0;i<arrayOfJson.size(); i++)
                {
                    JSONObject jsonObject = null;
                    try
                    {
                        jsonObject = new JSONObject(arrayOfJson.get(i).replaceAll("\\s", "|"));
                        String name = jsonObject.get("name").toString().replaceAll("\\|", " ");
                        String UUID = jsonObject.get("UUID").toString();
                        boolean analog = jsonObject.getBoolean("analog");

                        addLight(new LightsData.Light(UUID, name, false, false, i, LightsFragment.class.toString()));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                ITEMS_INITIALIZED = true;
            }

            recyclerView.setAdapter(new DevicesRecyclerViewAdapter(DeviceData.ITEMS, mListener));
        }

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                               + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Device item);
    }

}


