package com.vivekkaushik.resumify;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vivekkaushik.resumify.adapters.TemplateListAdapter;
import com.vivekkaushik.resumify.model.Template;

import java.util.ArrayList;

public class TemplatesActivity extends AppCompatActivity {
    boolean templateSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        templateSelect = extras.getBoolean("templateChange");

        RecyclerView recyclerView = findViewById(R.id.templatesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        TemplateListAdapter adapter = new TemplateListAdapter(this, createItemList(), templateSelect);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.templates_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.template_grid_spacing_small);
        recyclerView.addItemDecoration(new TemplateGridItemDecoration(largePadding, smallPadding));
    }

    private ArrayList<Template> createItemList() {
        ArrayList<Template> list = new ArrayList<>();

        list.add(new Template(1, R.drawable.template1));
        list.add(new Template(2, R.drawable.template2));
        list.add(new Template(3, R.drawable.template3));
        list.add(new Template(4, R.drawable.template4));
        list.add(new Template(5, R.drawable.template5));
        list.add(new Template(6, R.drawable.template6));
        list.add(new Template(7, R.drawable.template7));
        list.add(new Template(8, R.drawable.template8));
        list.add(new Template(9, R.drawable.template9));
        list.add(new Template(10, R.drawable.template10));
        return list;
    }
}
