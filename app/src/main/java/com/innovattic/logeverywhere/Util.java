package com.innovattic.logeverywhere;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Jelle on 18-7-2015.
 */
public class Util
{

	public static void showToast(final Context context, final String line)
	{
		new Handler(Looper.getMainLooper()).post(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(context, line, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
