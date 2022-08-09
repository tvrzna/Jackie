package cz.tvrzna.jackie;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.tvrzna.jackie.CommonUtils.ESCAPE_CHARACTERS;

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
	 * @param config
	 *          the config
	 * @return the object
	 * @throws Exception
	 *           the exception
	 */
	protected static Object deserialize(String input, Config config) throws Exception
	{
		StringReader reader = new StringReader(input.trim());

		int c;
		if ((c = reader.read()) >= 0)
		{
			if (c == '{')
			{
				return deserializeToMap(reader, config);
			}
			else if (c == '[')
			{
				return deserializeToList(reader, config);
			}
			else
			{
				return sanitize(input, config);
			}
		}

		return null;
	}

	/**
	 * Deserialize to map.
	 *
	 * @param input
	 *          the input
	 * @param config
	 *          the config
	 * @return the map
	 * @throws Exception
	 *           the exception
	 */
	private static Map<String, Object> deserializeToMap(StringReader input, Config config) throws Exception
	{
		Map<String, Object> result = new LinkedHashMap<>();

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
				result.put(sanitize(sw.toString(), config), deserializeValue(input, config));
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
	 * @param config
	 *          the config
	 * @return the list
	 * @throws Exception
	 *           the exception
	 */
	private static List<Object> deserializeToList(StringReader input, Config config) throws Exception
	{
		List<Object> result = new ArrayList<>();

		StringWriter sw = new StringWriter();
		int c;
		while ((c = input.read()) >= 0)
		{
			if (c == ']' || c == ',')
			{
				Object o = deserialize(sw.toString(), config);
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
				result.add(deserializeToList(input, config));
				sw = new StringWriter();
			}
			else if (c == '{')
			{
				result.add(deserializeToMap(input, config));
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
	 * @param config
	 *          the config
	 * @return the object
	 * @throws Exception
	 *           the exception
	 */
	private static Object deserializeValue(StringReader input, Config config) throws Exception
	{
		StringWriter sw = new StringWriter();
		Integer delimiter = null;
		int c;
		boolean previousWasEscape = false;
		boolean isInQuotes = false;
		boolean usedQuotes = false;
		while ((c = input.read()) >= 0)
		{
			if (c == '{' && !isInQuotes)
			{
				return deserializeToMap(input, config);
			}
			else if (c == '[' && !isInQuotes)
			{
				return deserializeToList(input, config);
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
				usedQuotes = true;
			}
			else if (delimiter != null && c == delimiter && !previousWasEscape)
			{
				isInQuotes = false;
			}
			else
			{
				if (previousWasEscape && c != '"' && c != '\'')
				{
					String character = "\\" + Character.toString((char) c);
					for (ESCAPE_CHARACTERS escape : ESCAPE_CHARACTERS.values())
					{
						character = character.replace(escape.getValue(), escape.getCharacter());
					}
					sw.write(character);
				}
				else
				{
					sw.write(c);
				}
				previousWasEscape = false;
			}
		}
		String value = sanitize(sw.toString(), config);
		if (config.isUseObjectWrapper() && !usedQuotes)
		{
			if (isNumeric(value))
			{
				if (value.contains("."))
				{
					return new ObjectWrapper(Double.parseDouble(value), Double.class);
				}
				else
				{
					return new ObjectWrapper(Long.parseLong(value), Long.class);
				}
			}
			else if ("true".equals(value) || "false".equals(value))
			{
				return new ObjectWrapper(Boolean.parseBoolean(value), Boolean.class);
			}
		}
		return value;
	}

	private static boolean isNumeric(String value)
	{
		if (value == null || value.isEmpty())
		{
			return false;
		}
		for (int i = 0; i < value.length(); i++)
		{
			if (!Character.isDigit(value.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Sanitize String <code>value</code> to not contain unsuitable characters.
	 *
	 * @param value
	 *          the value
	 * @param config
	 *          the config
	 * @return the string
	 */
	private static String sanitize(String value, Config config)
	{
		return value.trim().replaceAll("(^['\"]|['\"]$)", "").trim();
	}
}
