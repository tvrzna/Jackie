package cz.tvrzna.jackie;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class Deserializator.
 *
 * @author michalt
 */
public class Deserializator
{
	private Deserializator()
	{
	}

	/**
	 * Deserialize.
	 *
	 * @param input
	 *          the input
	 * @return the object
	 * @throws Exception
	 *           the exception
	 */
	protected static Object deserialize(String input) throws Exception
	{
		StringReader reader = new StringReader(input.trim());

		int c;
		if ((c = reader.read()) >= 0)
		{
			if (c == '{')
			{
				return deserializeToMap(reader);
			}
			else if (c == '[')
			{
				return deserializeToList(reader);
			}
			else
			{
				return sanitize(input);
			}
		}

		return null;
	}

	/**
	 * Deserialize to map.
	 *
	 * @param input
	 *          the input
	 * @return the map
	 * @throws Exception
	 *           the exception
	 */
	private static Map<String, Object> deserializeToMap(StringReader input) throws Exception
	{
		Map<String, Object> result = new HashMap<>();

		StringWriter sw = new StringWriter();
		int c;
		while ((c = input.read()) >= 0)
		{
			if (c == '}')
			{
				break;
			}
			else if (c == ':')
			{
				result.put(sanitize(sw.toString()), deserializeValue(input));
				sw = new StringWriter();
			}
			else if (c != ',')
			{
				sw.write(c);
			}
		}

		return result;
	}

	/**
	 * Deserialize to list.
	 *
	 * @param input
	 *          the input
	 * @return the list
	 * @throws Exception
	 *           the exception
	 */
	private static List<Object> deserializeToList(StringReader input) throws Exception
	{
		List<Object> result = new ArrayList<>();

		StringWriter sw = new StringWriter();
		int c;
		while ((c = input.read()) >= 0)
		{
			if (c == ']' || c == ',')
			{
				Object o = deserialize(sw.toString());
				if (o != null)
				{
					result.add(o);
				}
				sw = new StringWriter();
				if (c == ']')
				{
					break;
				}
			}
			else if (c == '[')
			{
				result.add(deserializeToList(input));
				sw = new StringWriter();
			}
			else if (c == '{')
			{
				result.add(deserializeToMap(input));
				sw = new StringWriter();
			}
			else
			{
				sw.write(c);
			}
		}

		return result;
	}

	/**
	 * Deserialize value.
	 *
	 * @param input
	 *          the input
	 * @return the object
	 * @throws Exception
	 *           the exception
	 */
	private static Object deserializeValue(StringReader input) throws Exception
	{
		StringWriter sw = new StringWriter();
		Integer delimiter = null;
		int c;
		boolean previousWasEscape = false;
		boolean isInQuotes = false;
		while ((c = input.read()) >= 0)
		{
			if (c == '{' && !isInQuotes)
			{
				return deserializeToMap(input);
			}
			else if (c == '[' && !isInQuotes)
			{
				return deserializeToList(input);
			}
			else if ((delimiter != null && c == delimiter && !previousWasEscape) || (delimiter == null && c == ','))
			{
				break;
			}
			else if ((c == '}' || c == ']') && !isInQuotes)
			{
				input.skip(-1l);
				break;
			}
			else if (c == '\\')
			{
				previousWasEscape = true;
			}
			else if (delimiter == null && !previousWasEscape && (c == '\'' || c == '"'))
			{
				delimiter = c;
				isInQuotes = true;
			}
			else if (delimiter != null && !previousWasEscape && (c == '\'' || c == '"'))
			{
				isInQuotes = false;
			}
			else
			{
				sw.write(c);
				previousWasEscape = false;
			}
		}
		return sanitize(sw.toString());
	}

	/**
	 * Sanitize String <code>value</code> to not contain unsuitable characters.
	 *
	 * @param value
	 *          the value
	 * @return the string
	 */
	private static String sanitize(String value)
	{
		return value.trim().replaceAll("(^['\"]|['\"]$)", "").trim();
	}
}
