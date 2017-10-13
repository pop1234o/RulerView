package liyafeng.com.ruler;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final SimpleRulerView rv_ruler = (SimpleRulerView) findViewById(R.id.rv_ruler);
		rv_ruler.setRange(30, 100);
		rv_ruler.scrollTo(50);
		rv_ruler.config(0.1f, "kg", Color.parseColor("#1aa260"));
		findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rv_ruler.smoothScrollTo(80);
			}
		});
//		ScrollView scrollView;
//		scrollView.is;
	}
}
