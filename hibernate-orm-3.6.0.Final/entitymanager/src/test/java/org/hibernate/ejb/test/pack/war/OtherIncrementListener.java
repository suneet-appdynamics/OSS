//$Id$
package org.hibernate.ejb.test.pack.war;

/**
 * @author Emmanuel Bernard
 */
public class OtherIncrementListener {
	private static int increment;

	public static int getIncrement() {
		return OtherIncrementListener.increment;
	}

	public static void reset() {
		increment = 0;
	}

	public void increment(Object entity) {
		OtherIncrementListener.increment++;
	}
}
