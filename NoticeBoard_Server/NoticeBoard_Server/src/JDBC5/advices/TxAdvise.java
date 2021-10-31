package JDBC5.advices;

import java.lang.reflect.InvocationTargetException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TxAdvise implements MethodInterceptor{
	PlatformTransactionManager transactionManager;
	

	public TxAdvise(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	
@Override
public Object invoke(MethodInvocation invocation) throws Throwable {
	TransactionStatus status = transactionManager
			.getTransaction(new DefaultTransactionDefinition());
	try {
		// ProxyFactoryBean은 구현체(target)를 내부적으로 가지고있다.
		Object result = invocation.proceed();
		transactionManager.commit(status);
		return result;
	} catch (InvocationTargetException e) {
		transactionManager.rollback(status);
		throw new RuntimeException(e.getCause());
	}
}
}
