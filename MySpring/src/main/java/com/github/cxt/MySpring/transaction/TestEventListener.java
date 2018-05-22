package com.github.cxt.MySpring.transaction;

import org.springframework.context.PayloadApplicationEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public class TestEventListener {

	
	//无论事务成功与否
	@TransactionalEventListener(fallbackExecution=true, phase=TransactionPhase.AFTER_COMPLETION)
	public void handle(PayloadApplicationEvent<Integer> event) {
		System.err.println(event.getPayload());
	}
}
