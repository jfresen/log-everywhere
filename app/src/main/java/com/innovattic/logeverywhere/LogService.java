package com.innovattic.logeverywhere;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;

public class LogService extends Service
{

	@SuppressWarnings("unused")
	private static final String TAG = LogService.class.getSimpleName();

	private LogcatHandler mLogcatHandler;

	@Override
	public void onCreate()
	{
		// Start up the thread running the service.  Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.  We also make it more
		// favorable priority so CPU-intensive work will less likely stop our service.
		final HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_MORE_FAVORABLE);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mLogcatHandler = new LogcatHandler(this, thread.getLooper());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Util.showToast(this, "service starting");

		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the job
		mLogcatHandler.start(startId);

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
		Util.showToast(this, "service stopping");
		mLogcatHandler.stop();
	}
}