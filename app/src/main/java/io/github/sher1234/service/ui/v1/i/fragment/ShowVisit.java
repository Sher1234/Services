package io.github.sher1234.service.ui.v1.i.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskShow;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.functions.TaskVisit;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visits;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.ui.v1.i.Show;
import io.github.sher1234.service.ui.v1.j.AddVisit;
import io.github.sher1234.service.ui.v1.j.EditVisit;
import io.github.sher1234.service.ui.v1.j.EndVisit;
import io.github.sher1234.service.ui.v1.j.SignVisit;
import io.github.sher1234.service.ui.v1.j.StartVisit;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;

public class ShowVisit extends Fragment implements View.OnClickListener, TaskVisit.TaskUpdate {

    private final TaskVisit taskVisit;
    private final TaskShow taskShow;
    private final Common common;
    private Visits visit;
    private Call call;
    private boolean b;

    private AppCompatTextView textViewA, textViewB;
    private LinearLayoutCompat layoutI, layoutS;

    public ShowVisit() {
        taskVisit = new TaskVisit();
        taskShow = new TaskShow();
        common = new Common();
    }

    public static ShowVisit getInstance(Call call, Visits visit, boolean b) {
        ShowVisit call1 = new ShowVisit();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraString, call);
        bundle.putSerializable(Strings.ExtraData1, visit);
        bundle.putBoolean(Strings.ExtraData2, b);
        call1.setArguments(bundle);
        return call1;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        assert getActivity() != null;
        ((Show) getActivity()).onDetachVisit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getActivity() != null;
        assert getArguments() != null;
        b = getArguments().getBoolean(Strings.ExtraData2, false);
        visit = (Visits) getArguments().getSerializable(Strings.ExtraData1);
        call = (Call) getArguments().getSerializable(Strings.ExtraString);
        ((Show) getActivity()).onAttachVisit(b);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.i_fragment_2, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        taskShow.onCreateVisitForm(getContext(), recyclerView);
        layoutI = view.findViewById(R.id.linearLayout1);
        layoutS = view.findViewById(R.id.linearLayout2);
        textViewA = view.findViewById(R.id.text1);
        textViewB = view.findViewById(R.id.text2);
        layoutI.setOnClickListener(this);
        layoutS.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.visit, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        User user = new TaskUser().getUser();
        if (b) menu.findItem(R.id.add).setVisible(false).setEnabled(false);
        if (visit.visit.isIncomplete()) {
            menu.findItem(R.id.add).setVisible(false).setEnabled(false);
            menu.findItem(R.id.pdf).setVisible(false).setEnabled(false);
            menu.findItem(R.id.edit).setVisible(false).setEnabled(false);
        }
        if (call.isCompleted()) menu.findItem(R.id.add).setEnabled(false).setVisible(false);
        if (user.isAdmin()) menu.findItem(R.id.delete).setVisible(true).setEnabled(true);
        else menu.findItem(R.id.delete).setVisible(false).setEnabled(false);
        if (visit.visit.Location == null || visit.visit.Location.isEmpty())
            menu.findItem(R.id.map).setEnabled(false).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                onDialogAddVisit();
                return true;

            case R.id.map:
                String[] s = visit.visit.Location.split("-#-");
                double lat = Double.parseDouble(s[0]);
                double lon = Double.parseDouble(s[1]);
                String q = "geo:" + lat + "," + lon + "?q=" + Uri.encode(lat + "," + lon +
                        "(Max Services)") + "&z=16";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(q)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Maps Unavailable", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;

            case R.id.pdf: {
                String url = Api.BASE_URL + "pdf/visit/" + visit.visit.VisitID;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }
            case R.id.edit: {
                Intent intent = new Intent(getActivity(), EditVisit.class);
                intent.putExtra(Strings.ExtraData1, call);
                intent.putExtra(Strings.ExtraData2, visit.visit);
                startActivityForResult(intent, 1412);
                return true;
            }
            case R.id.delete:
                onDeleteDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (visit != null && visit.visit != null) {
            textViewA.setText(visit.visit.VisitID);
            textViewB.setText(visit.visit.getStartDateView());
            if (!visit.visit.isIncomplete()) {
                textViewA.setTextColor(getResources().getColor(R.color.green));
                textViewB.setTextColor(getResources().getColor(R.color.green));
                textViewB.setBackgroundResource(R.drawable.tag_green);
                if (visit.visit.Signature == null || visit.visit.Signature.isEmpty())
                    layoutS.setVisibility(View.VISIBLE);
                else layoutS.setVisibility(View.GONE);
                layoutI.setVisibility(View.GONE);
            } else {
                textViewA.setTextColor(getResources().getColor(R.color.red));
                textViewB.setTextColor(getResources().getColor(R.color.red));
                textViewB.setBackgroundResource(R.drawable.tag_red);
                layoutI.setVisibility(View.VISIBLE);
            }
            taskShow.onShowVisit(call, visit);
        }
    }

    private void onDialogAddVisit() {
        assert getContext() != null;
        MaterialDialog dialog = MaterialDialog.Dialog(getContext());
        dialog.setTitle("Add Visit").setCancelable(true);
        dialog.setDescription("Add a visit for Call Id \"" + visit.visit.CallID + "\".");
        dialog.positiveButton("Full", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                Intent intent = new Intent(getActivity(), AddVisit.class);
                intent.putExtra(Strings.ExtraData, call);
                startActivityForResult(intent, 1412);
                dialog.dismiss();
            }
        }).negativeButton("Start", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                Intent intent = new Intent(getActivity(), StartVisit.class);
                intent.putExtra(Strings.ExtraData, call);
                startActivityForResult(intent, 1412);
                dialog.dismiss();
            }
        }).neutralButton("Cancel", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.linearLayout1) {
            Intent intent = new Intent(getActivity(), EndVisit.class);
            intent.putExtra(Strings.ExtraData1, call);
            intent.putExtra(Strings.ExtraData2, visit.visit);
            startActivityForResult(intent, 1412);
        } else if (view.getId() == R.id.linearLayout2) {
            Intent intent = new Intent(getActivity(), SignVisit.class);
            intent.putExtra(Strings.ExtraData, visit.visit.VisitID);
            startActivityForResult(intent, 1412);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert getActivity() != null;
        if (requestCode == 1412 && (resultCode == 100 || resultCode == 101))
            Snackbar.make(textViewA, "Data Changed, Reload Call", Snackbar.LENGTH_SHORT)
                    .setAction("Reload", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            assert getActivity() != null;
                            ((Show) getActivity()).onReloadCall();
                        }
                    }).show();
    }

    private void onDeleteDialog() {
        assert getActivity() != null;
        MaterialDialog dialog = MaterialDialog.Dialog(getActivity());
        dialog.setDescription(R.string.delete_visit_text).setTitle(R.string.delete_visit);
        dialog.positiveButton(R.string.delete, new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                fetch();
                dialog.dismiss();
            }
        }).negativeButton(R.string.cancel, new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onFetched(Responded response, int i) {
        common.dismissProgressDialog();
        if (response != null) {
            Snackbar.make(textViewA, response.Message, Snackbar.LENGTH_SHORT).show();
            if (response.Code == 1) taskVisit.onVisitDeleted(getActivity());
        } else {
            if (i == 306)
                Snackbar.make(textViewA, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(textViewA, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(textViewA, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskVisit.onNetworkError(getContext(), this);
        }
    }

    @Override
    public void onFetch() {
        assert getActivity() != null && getContext() != null;
        common.showProgressDialog(getContext());
        getActivity().setResult(100);
    }

    @Override
    public void fetch() {
        assert getActivity() != null;
        taskVisit.onDeleteVisit(this, visit.visit.VisitID);
    }
}