package com.innovattic.logeverywhere;

import android.app.Service;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Handler that receives messages from the thread
public class LogcatHandler extends Handler
{

	@SuppressWarnings("unused")
	private static final String TAG = LogcatHandler.class.getSimpleName();
	private static final long INTERVAL = 5000;

	private final Service mService;
	private boolean mStarted;
	private boolean mStopped;
	private long mLastToast = Long.MIN_VALUE;

	public LogcatHandler(final Service service, final Looper looper)
	{
		super(looper);
		mService = service;
		mStarted = false;
		mStopped = false;
	}

	public void start(final int startId)
	{
		final Message msg = obtainMessage();
		msg.arg1 = startId;
		mStarted = true;
		sendMessage(msg);
	}

	public void stop()
	{
		mStopped = true;
	}

	public boolean isStarted()
	{
		return mStarted;
	}

	public boolean isStopped()
	{
		return mStopped;
	}

	@Override
	public void handleMessage(final Message msg)
	{
		try {
			final Process process = Runtime.getRuntime().exec("logcat");
			final BufferedReader reader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null && !mStopped) {
				consume(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			mStarted = false;
			mService.stopSelf(msg.arg1);
		}
	}

	private void consume(final String line)
	{
		final long now = System.currentTimeMillis();
		if (mLastToast < now - INTERVAL) {
			mLastToast = now;
			Util.showToast(mService, line);
			Log.i(TAG, "Showing toast with content " + line);
		}
	}

}
