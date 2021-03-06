package com.t3h.immunization.fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.t3h.immunization.R;
import com.t3h.immunization.adapter.ExpanAdapterInjected;
import com.t3h.immunization.model.InjectionGroup;
import com.t3h.immunization.model.Injections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InjectedFragment extends Fragment  {
    private ExpanAdapterInjected adapter;
    @BindView(R.id.expandableListView_injected)
    ExpandableListView expandableList;
    List<InjectionGroup> groups;
    private List<List<Injections>> dataInjection;

    public InjectedFragment(List<List<Injections>> dataInjection, List<InjectionGroup>groups) {
        this.dataInjection = dataInjection;
        this.groups = groups;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.injected_fragment, container, false);
        ButterKnife.bind(this, view);
        Log.e("TATTT", "onCreateView: "+dataInjection.size() );
        adapter = new ExpanAdapterInjected(getContext());
        if (adapter!=null) {
            adapter.setDataList(dataInjection, groups);
            expandableList.setAdapter(adapter);
            for (int i = 0; i < dataInjection.size(); i++) {
                expandableList.expandGroup(i);
            }
            expandableList.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                return true; // This way the expander cannot be collapsed
            });
        }
        return view;
    }
}
