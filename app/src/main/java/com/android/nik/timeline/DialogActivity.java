package com.android.nik.timeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import model.dialog.DialogContext;

public class DialogActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);

        DialogContext context =  getIntent().getParcelableExtra(
                MainActivity.class.getPackage()+DialogContext.class.getSimpleName());
        Toolbar toolbar = (Toolbar)findViewById(R.id.ActionToolbar);
        String title,prefix=null;

        title = getResources().getStringArray(R.array.dialog_content)[context.getType()];

        switch (context.getAction()){
            case DialogContext.ADD:
                prefix = getString(R.string.dialog_title_add);
                break;
            case DialogContext.UPDATE:
                prefix = getString(R.string.dialog_title_update);
                break;
        }

        toolbar.setTitle(prefix+" "+title);
        setSupportActionBar(toolbar);
        Log.i("from dialog Activity","id = " + context.getId());
		if (savedInstanceState == null) {
            //вызыв соответствующего фрагмента внутрь которого передаём параметры.
            getFragmentManager().beginTransaction()
					.add(R.id.container,
                            DialogFragment
                                    .newInstanseState(context.getAction(), context.getType(), context.getId()))
                    .commit();
		}
	}

}
