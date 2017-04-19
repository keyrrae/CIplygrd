package edu.ucsb.cs.cs190i.lithoplay;

import android.app.LauncherActivity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.LithoView;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.widget.Text;

import static com.facebook.yoga.YogaEdge.ALL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ComponentContext context = new ComponentContext(this);

        final Component text = ListItem.create(context).build();

        setContentView(LithoView.create(context, text));
    }

}
