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

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ButterKnife.bind(this);
	}

//	@OnClick(R.id.grant_permission)
//	protected void grantPermission()
//	{
//		try {
//			final java.lang.Process process = Runtime.getRuntime().exec(
//					"pm grant com.innovattic.logeverywhere android.permission.READ_LOGS"
//			);
//			final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			String line = "";
//			while ((line = reader.readLine()) != null) {
//				Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
//				Log.e(TAG, line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
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
