package com.innovattic.logeverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jelle on 6-7-2015.
 */
public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.start_service)
	protected void startService()
	{
		startService(new Intent(this, LogService.class));
	}

	@OnClick(R.id.stop_service)
	protected void stopService()
	{
		stopService(new Intent(this, LogService.class));
	}

}
