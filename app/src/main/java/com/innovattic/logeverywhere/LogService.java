package com.innovattic.logeverywhere;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogService extends Service
{
	private static final String TAG = LogService.class.getSimpleName();
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private boolean mStopped;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler
	{
		public ServiceHandler(Looper looper)
		{
			super(looper);
		}

		@Override
		public void handleMessage(Message msg)
		{
			try {
				showId();
				Log.e(TAG, "starting logcat :)");
				final java.lang.Process process = Runtime.getRuntime().exec("logcat");
				final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while ((line = reader.readLine()) != null && !mStopped) {
					consume(line);
				}
				Log.e(TAG, "no more logcat :(");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Normally we would do some work here, like download a file.
			// For our sample, we just sleep for 5 seconds.
//            long endTime = System.currentTimeMillis() + 5 * 1000;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                    }
//                }
//            }
//            // Stop the service using the startId, so that we don't stop
//            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
		}

		private void showId()
		{
			try {
				final java.lang.Process process = Runtime.getRuntime().exec("id");
				final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while ((line = reader.readLine()) != null) {
					showToast(line);
					Log.e(TAG, line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private long mLastToast = Long.MIN_VALUE;
	private static final long INTERVAL = 3000;

	private void consume(final String line)
	{
		final long now = System.currentTimeMillis();
		if (mLastToast < now - INTERVAL) {
			mLastToast = now;
			showToast(line);
			Log.i(TAG, "Showing toast with content " + line);
		}
	}

	private void showToast(final String line)
	{
		final Context context = this;
		new Handler(Looper.getMainLooper()).post(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(context, line, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onCreate()
	{
//		mUiHandler = new Handler(Looper.getMainLooper());
		// Start up the thread running the service.  Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.  We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_MORE_FAVORABLE);
		thread.start();
		mStopped = false;

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		showToast("service starting");

		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the job
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy()
	{
		showToast("service done");
		mStopped = true;
	}
}