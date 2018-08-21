package net.lonzak.common.unittest;

import java.util.Date;

import org.junit.Test;

public class AutoTesterExceptionTest {
	@Test
	public void testPrivateConstructor() {
		AutoTester.testPrivateConstructor(AutoTester.class);
	}
	
	@Test
	public void testTestException() {		
		AutoTester.testClass(DerivedExceptionOK.class);
	}

	@Test
	public void testTestException_InvalidMessage() {
		//TODO Fix the problem that the parameter is not correctly set
		AutoTester.testClass(DerivedExceptionInvalidMessage.class);
	}
	
	@Test
	public void testTestException_InvalidCause() {
		//TODO Fix the problem that the parameter is not correctly set
		AutoTester.testClass(DerivedExceptionInvalidCause.class);
	}

	@Test
	public void testTestException_StringParameter() {
		AutoTester.testClass(DerivedExceptionUnsupportedStringParameter.class);
	}

	@Test //(expected=PotentialErrorDetected.class)
	public void testTestException_UnsupportedThrowableParameter() {
		AutoTester.testClass(DerivedExceptionUnsupportedThrowableParameter.class);
	}

	@Test
	public void testTestException_UnsupportedOtherTypeParameter() {
		AutoTester.testClass(DerivedExceptionUnsupportedOtherTypeParameter.class);
	}

	public static class DerivedExceptionOK extends Exception {

		private static final long serialVersionUID = 5206991570735733655L;

		public DerivedExceptionOK() {
			super();
		}

		public DerivedExceptionOK(String message) {
			super(message);
		}

		public DerivedExceptionOK(String message, Throwable cause) {
			super(message, cause);
		}

		public DerivedExceptionOK(Throwable cause) {
			super(cause);
		}
	}

	public static class DerivedExceptionInvalidMessage extends Exception {

		private static final long serialVersionUID = 8512916749503925546L;

		public DerivedExceptionInvalidMessage(String message) {
			super("wrong message");
		}
		
		@Override
		public String getMessage() {
			return super.getMessage();
		}
	}

	public static class DerivedExceptionInvalidCause extends Exception {

		private static final long serialVersionUID = 8512916749503925546L;

		public DerivedExceptionInvalidCause(Throwable cause) {
			super(new Exception());
		}
		
		@Override
		public synchronized Throwable getCause() {
			return super.getCause();
		}
	}
	
	public static class DerivedExceptionUnsupportedOtherTypeParameter extends Exception {

		private static final long serialVersionUID = 8512916749503925546L;

		/**
		 * constructor with an extra parameter which has a different type than string or throwable
		 */
		public DerivedExceptionUnsupportedOtherTypeParameter(String message, Date notsupportedParameter) {
			super(message);
		}
	}

	public static class DerivedExceptionUnsupportedStringParameter extends Exception {

		private static final long serialVersionUID = 8512916749503925546L;

		/*
		 * Constructor with a second string parameter
		 */
		public DerivedExceptionUnsupportedStringParameter(String message, String notsupportedParameter) {
			super(message);
		}
	}

	public static class DerivedExceptionUnsupportedThrowableParameter extends Exception {

		private static final long serialVersionUID = 8512916749503925546L;

		/*
		 * Constructor with a second throwable parameter
		 */
		public DerivedExceptionUnsupportedThrowableParameter(Throwable cause, Throwable notsupportedParameter) {
			super(cause);
		}
	}

}
