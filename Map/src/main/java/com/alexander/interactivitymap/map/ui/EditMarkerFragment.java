package com.alexander.interactivitymap.map.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alexander.interactivitymap.map.core.Consts;
import com.alexander.interactivitymap.map.R;

public class EditMarkerFragment extends Fragment implements View.OnClickListener {

    private EditText vEdtTitle;
    private EditText vEdtDescription;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View fragmentRootView = inflater.inflate(R.layout.edit_marker_fragment, container, false);

        vEdtTitle = (EditText) fragmentRootView.findViewById(R.id.edt_markerTitle);
        if (getActivity().getIntent() != null && getActivity().getIntent().getStringExtra(Consts.MARKER_TITLE_FIELD) != null) {
            vEdtTitle.setText(getActivity().getIntent().getStringExtra(Consts.MARKER_TITLE_FIELD));
        }
        vEdtDescription = (EditText) fragmentRootView.findViewById(R.id.edt_markerDescription);
        if (getActivity().getIntent() != null && getActivity().getIntent().getStringExtra(Consts.MARKER_DESCRIPTION_FIELD) != null) {
            vEdtDescription.setText(getActivity().getIntent().getStringExtra(Consts.MARKER_DESCRIPTION_FIELD));
        }
        Button vBtnOk = (Button) fragmentRootView.findViewById(R.id.btn_ok);
        Button vBtnCancel = (Button) fragmentRootView.findViewById(R.id.btn_cancel);

        vBtnOk.setOnClickListener(this);
        vBtnCancel.setOnClickListener(this);

        return fragmentRootView;
    }

    private String getEnteredText(EditText targetView) {
        if (targetView == null) throw new AssertionError();
        if (targetView.getText() == null) throw new AssertionError();
        return targetView.getText().toString();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_ok:
                // String.isEmpty() method cannot be used with API 8
                if (!getEnteredText(vEdtTitle).equals("")) {
                    Intent userData = new Intent();
                    userData.putExtra(Consts.MARKER_TITLE_FIELD, getEnteredText(vEdtTitle));
                    userData.putExtra(Consts.MARKER_DESCRIPTION_FIELD, getEnteredText(vEdtDescription));

                    getActivity().setResult(Activity.RESULT_OK, userData);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.emptyTitle_message), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancel:
            default:
                getActivity().onBackPressed();
        }
    }
}
