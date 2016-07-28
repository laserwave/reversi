package cn.zhikaizhang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.zhikaizhang.reversi.R;
import cn.zhikaizhang.game.Constant;


public class MainActivity extends Activity {

	private Button play;
	private Button rule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		play = (Button)findViewById(R.id.playButton);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				Bundle bundle = new Bundle();
				bundle.putByte("playerColor", Constant.BLACK);
				bundle.putInt("difficulty", 1);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		});

		rule = (Button)findViewById(R.id.ruleButton);
		rule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, GameRuleActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
	}

}
