package utils;

import java.util.LinkedList;

public class ThreadPool extends ThreadGroup
{
	//Members
	private boolean isAlive = true;
	private int threadID;
	private LinkedList<Runnable> queue;
	private static int threadPoolID;
	
	public ThreadPool(int numThread)
	{
		super("threadpool: " + threadPoolID);

		queue = new LinkedList<Runnable>();		
		
		setDaemon(true);
		
		for (int i = 0; i < numThread; i++)
		{
			new PooledThread().start();
		}
	}
	
	public void runTask(Runnable task)
	{
		if(!isAlive)
		{
			throw new IllegalStateException();
		}
		
		if(task != null)
		{
			queue.add(task);
			notify();
		}
	}
	
	public void close()
	{
		if(isAlive)
		{
			isAlive = false;
			queue.clear();
			interrupt();
		}
	}
	
	public void join()
	{
		synchronized (this)
		{
			isAlive = false;
			notifyAll();
		}
		
		Thread[] thread = new Thread[super.activeCount()];
		int count = super.enumerate(thread);
		
		for(int i = 0; i < count; i++)
		{
			try
			{
				thread[i].join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public synchronized Runnable getTask() throws InterruptedException
	{
		while (queue.size() == 0)
		{
			if(!isAlive)
			{
				return null;
			}
			wait();
		}
		return queue.removeFirst();
	}

	
	private class PooledThread extends Thread
	{
		public PooledThread()
		{
			super(ThreadPool.this ,"PooledThread: " + threadID++);
		}
		
		@Override
		public void run()
		{
			while(!interrupted())
			{
				Runnable task = null;
				try
				{
					task = getTask();
				}
				catch (InterruptedException e){e.printStackTrace();}
				
				if(task == null)
				{
					return;
				}
				
				try
				{
					task.run();
				}
				catch(Throwable e)
				{
					uncaughtException(this, e);
				}
			}
		}
	}

}

