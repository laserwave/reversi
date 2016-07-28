package cn.zhikaizhang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import cn.zhikaizhang.reversi.R;

public class GameRuleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_rule);
		TextView textView = (TextView)findViewById(R.id.rule);
		textView.setText(getResources().getString(R.string.game_rule));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			GameRuleActivity.this.finish();
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
