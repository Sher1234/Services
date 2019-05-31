package io.github.sher1234.service.ui.v1.f;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.PrivilegeAdapter;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.Navigate;
import io.github.sher1234.service.functions.TaskPrivilege;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.MaterialDialog;

public class Privileges extends AppCompatActivity implements TaskPrivilege.TaskUpdate,
        Toolbar.OnMenuItemClickListener, PrivilegeAdapter.ItemClick {

    private final TaskPrivilege taskP = new TaskPrivilege();
    private final Common common = new Common();
    private boolean exit = false;
    private Navigate funN;

    private AppCompatTextView textViewTitle;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity);
        funN = new Navigate(this, 5);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.refresh);
        toolbar.setOnMenuItemClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        textViewTitle = findViewById(R.id.textViewTitle);
        ((FloatingActionButton) findViewById(R.id.fab)).hide();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        onFragmentDetached();
        fetch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        funN.onResumeActivity();
    }

    @Override
    public void onBackPressed() {
        if (exit)
            super.onBackPressed();
        else {
            exit = true;
            Snackbar.make(textViewTitle, "Press back again to exit", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }

    private void onFragmentDetached() {
        toolbar.getMenu().findItem(R.id.refresh).setVisible(true).setEnabled(true);
        textViewTitle.setText(R.string.user_privileges);
    }

    private void onRefresh(List<User> users) {
        PrivilegeAdapter privilegeAdapter;
        if (recyclerView.getAdapter() != null) {
            privilegeAdapter = (PrivilegeAdapter) recyclerView.getAdapter();
            privilegeAdapter.onUpdateUsers(users);
        } else {
            privilegeAdapter = new PrivilegeAdapter(this, users);
            recyclerView.setAdapter(privilegeAdapter);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.refresh) {
            fetch();
            return true;
        }
        return false;
    }

    @Override
    public void onFetched(@Nullable Users users, int i, boolean b) {
        common.dismissProgressDialog();
        if (!b) {
            if (users != null) {
                Snackbar.make(recyclerView, users.Message, Snackbar.LENGTH_SHORT).show();
                onRefresh(users.Users);
            } else {
                if (i == 306)
                    Snackbar.make(recyclerView, "Content parse error", Snackbar.LENGTH_LONG).show();
                else if (i == 307)
                    Snackbar.make(recyclerView, "Network failure", Snackbar.LENGTH_LONG).show();
                else if (i == 308)
                    Snackbar.make(recyclerView, "Request cancelled", Snackbar.LENGTH_LONG).show();
                taskP.onNetworkError(this, this);
            }
        }
    }

    @Override
    public void fetch() {
        taskP.onListRefresh(this);
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
    }

    @Override
    public void onItemClick(final User user, View v) {
        MaterialDialog dialog = MaterialDialog.Dialog(this).setTitle("Update Privileges")
                .setDescription("Change Account Privileges of " + user.Name);
        if (user.isAdmin())
            dialog.neutralButton(R.string.dismiss_admin, new MaterialDialog.ButtonClick() {
                @Override
                public void onClick(MaterialDialog dialog, View v) {
                    taskP.onChangeAdministrator(Privileges.this, user.UserID, 0);
                    dialog.dismiss();
                }
            });
        else
            dialog.neutralButton(R.string.add_admin, new MaterialDialog.ButtonClick() {
                @Override
                public void onClick(MaterialDialog dialog, View v) {
                    taskP.onChangeAdministrator(Privileges.this, user.UserID, 1);
                    dialog.dismiss();
                }
            });
        if (user.isRegistered())
            dialog.negativeButton("Disable Account", new MaterialDialog.ButtonClick() {
                @Override
                public void onClick(MaterialDialog dialog, View v) {
                    taskP.onChangeAccountState(Privileges.this, user.UserID, 0);
                    dialog.dismiss();
                }
            });
        else
            dialog.negativeButton("Enable Account", new MaterialDialog.ButtonClick() {
                @Override
                public void onClick(MaterialDialog dialog, View v) {
                    taskP.onChangeAccountState(Privileges.this, user.UserID, 1);
                    dialog.dismiss();
                }
            });
        dialog.positiveButton("Delete Account", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                taskP.onDeleteAccount(Privileges.this, user.UserID, user.Email);
                dialog.dismiss();
            }
        });
        dialog.show();//.setButtonOrientation(LinearLayoutCompat.VERTICAL).show();
    }
}