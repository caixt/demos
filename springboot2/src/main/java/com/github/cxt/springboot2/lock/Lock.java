package com.github.cxt.springboot2.lock;

import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Lock {
	
	private InterProcessLock lock;
	private CuratorFramework zk;
	private String key;
	
	protected Lock(CuratorFramework zk, String key) {
		this.zk = zk;
		this.key = key;
		this.lock = new InterProcessMutex(zk, key);
	}

	public boolean tryLock(long time, TimeUnit unit) {
		try {
			return lock.acquire(time, unit);
		} catch (Exception e) {
			log.warn("上锁失败", e);
			return false;
		}
    }
	
	public boolean tryLock() {
        return tryLock(0, TimeUnit.SECONDS);
    }

	public boolean lock() {
		try {
			lock.acquire();
			return true;
		} catch (Exception e) {
			log.warn("上锁失败", e);
			return false;
		}
	}
	
	public void unlock() {
		try {
			lock.release();
			try {
                if (zk.getChildren().forPath(key).size() == 0) {
                    zk.delete().guaranteed().forPath(key);
                }
            } catch (Exception ignore) {
            	log.debug("删除节点失败", ignore);
            }
		} catch (Exception e) {
			log.warn("释放锁失败", e);
		}
	}
}
