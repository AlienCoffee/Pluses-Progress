package ru.shemplo.pluses.layout;


import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import ru.shemplo.pluses.R;

public class ToolbarManager {
    public static String holder;
    private final Toolbar toolbar;
    Stack<CharSequence> stack = new Stack<>();
    private int size;
    private final Context context;

    public ToolbarManager(final DiaryMainActivity context) {
        this.context = context;

        toolbar = (Toolbar) context.findViewById(R.id.toolbar);

        toolbar.findViewById(R.id.toolbar_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context.getFragmentManager().getBackStackEntryCount() > 0) {
                    context.getFragmentManager().popBackStack();
                }
            }
        });
        toolbar.findViewById (R.id.toolbar_update_label).setOnClickListener (
            new View.OnClickListener () {
                // TODO: infer to common instance of ClickListener with @toolbar_update_button
                @Override
                public void onClick (View view) {
                    context.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                    Fragment fragment = context.getFragmentManager().findFragmentById(R.id.main_frame);
                    Toast toast = Toast.makeText(DiaryMainActivity.page,
                            "Update button clicked", Toast.LENGTH_SHORT);
                    toast.show();
                    if (fragment == null) {
                        Log.e("ERROR", "Cannot update, fragment is null");
                        return;
                    }
                    if (DiaryMainActivity.group == -1) {
                        Log.i("DMA", "Update groups");
                        ((GroupsFragment) fragment).updateData();
                    } else if (DiaryMainActivity.student == -1) {
                        Log.i("DMA", "Update students");
                        ((StudentsFragment) fragment).updateData(DiaryMainActivity.group);
                    } else {
                        Log.i("DMA", "Update topics");
                        ((TopicsFragment) fragment).updateData(DiaryMainActivity.student);
                    }
                    context.findViewById(R.id.progress_bar).setVisibility(View.GONE);
                }
        });

        toolbar.findViewById(R.id.toolbar_update_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                        Fragment fragment = context.getFragmentManager().findFragmentById(R.id.main_frame);
                        Toast toast = Toast.makeText(DiaryMainActivity.page,
                                "Update button clicked", Toast.LENGTH_SHORT);
                        toast.show();
                        if (fragment == null) {
                            Log.e("ERROR", "Cannot update, fragment is null");
                            return;
                        }
                        if (DiaryMainActivity.group == -1) {
                            Log.i("DMA", "Update groups");
                            ((GroupsFragment) fragment).updateData();
                        } else if (DiaryMainActivity.student == -1) {
                            Log.i("DMA", "Update students");
                            ((StudentsFragment) fragment).updateData(DiaryMainActivity.group);
                        } else {
                            Log.i("DMA", "Update topics");
                            ((TopicsFragment) fragment).updateData(DiaryMainActivity.student);
                        }
                        context.findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    }

                });
    }

    public void setDepth(int depth) {
        //Log.e("tmp_pp0", stack.toString());
        while (size > depth) {
            setHeading(stack.pop());
            size--;
        }
        if (size == 0) {
            toolbar.findViewById(R.id.toolbar_back_button).setVisibility(View.GONE);
        }
        //Log.e("tmp_pp1", stack.toString());
    }

    public void push(CharSequence name) {
        //Log.e("tmp_psh0", stack.toString() + "s: " + name);
        if (size == 0) {
            toolbar.findViewById(R.id.toolbar_back_button).setVisibility(View.VISIBLE);
        }
        stack.push(getHeading());
        size++; setHeading(name);
        //Log.e("tmp_psh1", stack.toString());
    }

    private void setHeading(CharSequence s) {
        ((TextView) toolbar.findViewById(R.id.toolbar_text)).setText(s);
    }

    private CharSequence getHeading() {
        return ((TextView) toolbar.findViewById(R.id.toolbar_text)).getText();
    }

}
