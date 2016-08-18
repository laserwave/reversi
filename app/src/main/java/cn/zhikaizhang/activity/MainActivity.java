package cn.zhikaizhang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                final SharedPreferences historyChoice = getSharedPreferences("historyChoice", Context.MODE_PRIVATE);
                final boolean isBlack = historyChoice.getBoolean("role", true);
                final int lastLevel = historyChoice.getInt("level", 0);

				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				Bundle bundle = new Bundle();
				bundle.putByte("playerColor", isBlack ? Constant.BLACK : Constant.WHITE);
				bundle.putInt("difficulty", lastLevel + 1);
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
