package calculator;

import javafx.util.Pair;

public class MyPair<K, V> extends Pair<K, V>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyPair(K arg0, V arg1) {
		super(arg0, arg1);
	}

	@Override
	public String toString()
	{
		return getKey() + " " + getValue();
	}
}
