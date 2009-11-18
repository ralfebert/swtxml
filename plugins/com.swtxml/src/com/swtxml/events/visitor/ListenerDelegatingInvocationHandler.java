package com.swtxml.events.visitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.swtxml.events.EventListenerException;

/**
 * Invocation handler that delegates method calls to expectedMethodName to the
 * responder's responderMethod. Other method calls are ignored.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
final class ListenerDelegatingInvocationHandler implements InvocationHandler {

	private Object responder;
	private final Method responderMethod;
	private final String expectedMethodName;

	public ListenerDelegatingInvocationHandler(String expectedMethodName, Object responder,
			Method responderMethod) {
		this.expectedMethodName = expectedMethodName;
		this.responder = responder;
		this.responderMethod = responderMethod;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (!method.getName().equals(expectedMethodName)) {
			return null;
		}

		Object[] parameters = args;
		if (responderMethod.getParameterTypes().length == 0) {
			parameters = new Object[0];
		}

		try {
			responderMethod.setAccessible(true);
			return responderMethod.invoke(responder, parameters);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if (targetException instanceof RuntimeException) {
				throw targetException;
			} else {
				throw new EventListenerException(e);
			}
		} catch (Exception e) {
			throw new EventListenerException(e);
		}
	}
}