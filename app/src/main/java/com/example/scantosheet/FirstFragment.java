package com.example.scantosheet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scantosheet.databinding.FragmentFirstBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TextView idRes;
    OnScanListener scanListener;

    public static final String PREF_SS_ID = "pref_ss_id";
    public static final String PREF_USERNAME = "pref_username";

    public interface OnScanListener {
        public void onScanned(String scanResult);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onAttach(@NonNull @org.jetbrains.annotations.NotNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;

        try {
            scanListener = (OnScanListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnScanListener");
        }


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idRes = view.findViewById(R.id.textview_first);



        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                idRes.setText(sharedPreferences.getString(PREF_SS_ID, ""));
               // NavHostFragment.findNavController(FirstFragment.this)
                       // .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String scanResult;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null) {
            scanResult = result.getContents();
            scanListener.onScanned(scanResult);
            //send request
            //
            //
            //
            //

            //new SendRequest().execute();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}