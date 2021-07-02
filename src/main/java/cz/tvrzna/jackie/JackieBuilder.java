package cz.tvrzna.jackie;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * This class allows creation of anonymous JSON objects.
 *
 * @author michalt
 */
public class JackieBuilder
{
	private JackieBuilder()
	{
	}

	/**
	 * Creates the <code>JackieElement</code>.
	 *
	 * @return the jackie element
	 */
	public static JackieElement create()
	{
		return new JackieElement();
	}

	/**
	 * From string.
	 *
	 * @param json the json
	 * @return the jackie element
	 */
	public static JackieElement fromString(String json)
	{
		Jackie jackie = new Jackie();
		JackieElement result = new JackieElement();
		result.putAll(jackie.fromJson(json, JackieElement.class));
		return result;
	}

	/**
	 * The Class JackieElement.
	 *
	 * @author michalt
	 */
	public static class JackieElement extends HashMap<String, Object>
	{
		private static final long serialVersionUID = -790430865279217546L;

		/**
		 * Adds the value of <code>Object</code>.
		 *
		 * @param name
		 *          the name
		 * @param value
		 *          the value
		 * @return the jackie element
		 */
		public JackieElement add(String name, Object value)
		{
			put(name, value);
			return this;
		}

		/**
		 * Adds the child as <code>JackieElement</code> to be processed as object.
		 *
		 * @param name
		 *          the name
		 * @param childElementFunction
		 *          the child element function
		 * @return the jackie element
		 */
		public JackieElement add(String name, Consumer<JackieElement> childElementFunction)
		{
			JackieElement childElement = new JackieElement();
			childElementFunction.accept(childElement);
			put(name, childElement);
			return this;
		}

		/**
		 * To string.
		 *
		 * @return the string
		 */
		@Override
		public String toString()
		{
			Jackie jackie = new Jackie();
			return jackie.toJson(this);
		}
	}
}
