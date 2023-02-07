package cz.tvrzna.jackie;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import cz.tvrzna.jackie.CommonUtils.ESCAPE_CHARACTERS;

/**
 * The Class Serializator.
 *
 * @author michalt
 */
public class Serializator
{

	private Serializator()
	{
	}

	/**
	 * Serialize <code>object</code> to <code>String</code>.
	 *
	 * @param w
	 *          the w
	 * @param object
	 *          the object
	 * @param config
	 *          the config
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	protected static String serialize(Object object, Config config, int indent) throws Exception
	{
		StringWriter sw = new StringWriter();
		serialize(sw, object, config, indent);
		return sw.toString();
	}

	/**
	 * Serialize <code>object</code> to <code>OutputStream</code>.
	 *
	 * @param w
	 *          the w
	 * @param object
	 *          the object
	 * @param config
	 *          the config
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	protected static void serialize(OutputStream os, Object object, Config config, int indent) throws Exception
	{
		OutputStreamWriter sw = new OutputStreamWriter(os);
		serialize(sw, object, config, indent);
		sw.close();
	}

	/**
	 * Serialize <code>object</code>.
	 *
	 * @param w
	 *          the w
	 * @param object
	 *          the object
	 * @param config
	 *          the config
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	private static void serialize(Writer w, Object object, Config config, int indent) throws Exception
	{
		if (object instanceof List)
		{
			serializeList(w, (List<Object>) object, config, indent);
		}
		else if (object instanceof Map)
		{
			serializeMap(w, (Map<Object, Object>) object, config, indent);
		}
		else if (object.getClass().isArray())
		{
			serializeArray(w, (Object[]) object, config, indent);
		}
		else
		{
			serializeValue(w, object, config);
		}
	}

	/**
	 * Serialize value.
	 *
	 * @param w
	 *          the w
	 * @param value
	 *          the value
	 * @param config
	 *          the config
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static void serializeValue(Writer w, Object value, Config config) throws Exception
	{
		if (value == null)
		{
			w.append("null");
			return;
		}
		else if (value instanceof String)
		{
			w.append(serializeString((String) value, config));
			return;
		}
		else if (value instanceof Number)
		{
			StringBuilder sb = new StringBuilder(value.toString());
			CommonUtils.stringBuilderReplace(sb, ",", ".");

			w.append(sb.toString());
			return;
		}
		else if (value instanceof Boolean)
		{
			w.append(value.toString());
			return;
		}
		else if (value instanceof Date)
		{
			DateFormat df = Optional.ofNullable(config.getDateFormat()).orElse(new SimpleDateFormat(CommonUtils.DATE_FORMAT_JSON));
			w.append(getSeparator());
			w.append(df.format(value));
			w.append(getSeparator());
			return;
		}
		w.append(serializeString(value.toString(), config));
	}

	/**
	 * Serialize string.
	 *
	 * @param value
	 *          the value
	 * @param config
	 *          the config
	 * @return the string
	 */
	private static String serializeString(String value, Config config)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(value);
		CommonUtils.stringBuilderReplace(sb, getSeparator(), "\\".concat(getSeparator()));
		for (ESCAPE_CHARACTERS escape : ESCAPE_CHARACTERS.values())
		{
			CommonUtils.stringBuilderReplace(sb, escape.getCharacter(), escape.getValue());
		}

		sb.insert(0, getSeparator());
		sb.append(getSeparator());
		return sb.toString();
	}

	/**
	 * Serialize map.
	 *
	 * @param w
	 *          the w
	 * @param map
	 *          the map
	 * @param config
	 *          the config
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static void serializeMap(Writer w, Map<Object, Object> map, Config config, int indent) throws Exception
	{
		w.append("{");
		indent++;
		List<Entry<Object, Object>> list = new ArrayList<>(map.entrySet());
		for (int i = 0; i < list.size(); i++)
		{
			if (i == 0)
			{
				if (config.isPrettyPrint())
				{
					w.append(config.getPrettyLineSymbol());
				}
			}

			Entry<Object, Object> entry = list.get(i);

			StringWriter sw = new StringWriter();
			serializeValue(sw, entry.getKey(), config);
			String key = sw.toString();

			boolean requireSeparator = !key.startsWith(getSeparator()) || !key.endsWith(getSeparator());
			if (config.isPrettyPrint())
			{
				for (int j = 0; j < indent; j++)
				{
					w.append(config.getPrettyIndentSymbol());
				}
			}
			w.append(requireSeparator ? getSeparator() : "").append(key).append(requireSeparator ? getSeparator() : "");
			if (config.isPrettyPrint())
			{
				w.append(" : ");
			}
			else
			{
				w.append(":");
			}
			serialize(w, entry.getValue(), config, indent);
			if (i < list.size() - 1)
			{
				w.append(",");
				if (config.isPrettyPrint())
				{
					w.append(config.getPrettyLineSymbol());
				}
			}
		}
		if (config.isPrettyPrint() && list.isEmpty())
		{
			w.append(" ");
		}
		indent--;
		if (config.isPrettyPrint() && !list.isEmpty())
		{
			w.append(config.getPrettyLineSymbol());
			for (int j = 0; j < indent; j++)
			{
				w.append(config.getPrettyIndentSymbol());
			}
		}
		w.append("}");
	}

	/**
	 * Serialize list.
	 *
	 * @param w
	 *          the w
	 * @param list
	 *          the list
	 * @param config
	 *          the config
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static void serializeList(Writer w, List<Object> list, Config config, int indent) throws Exception
	{
		w.append("[");
		if (config.isPrettyPrint())
		{
			w.append(" ");
		}
		for (int i = 0; i < list.size(); i++)
		{
			serialize(w, list.get(i), config, indent);
			if (i < list.size() - 1)
			{
				w.append(",");
				if (config.isPrettyPrint())
				{
					w.append(" ");
				}
			}
		}
		if (config.isPrettyPrint())
		{
			w.append(" ");
		}
		w.append("]");
	}

	/**
	 * Serialize array.
	 *
	 * @param w
	 *          the w
	 * @param arr
	 *          the list
	 * @param config
	 *          the config
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static void serializeArray(Writer w, Object[] arr, Config config, int indent) throws Exception
	{
		w.append("[");
		if (config.isPrettyPrint())
		{
			w.append(" ");
		}
		for (int i = 0; i < arr.length; i++)
		{
			serialize(w, arr[i], config, indent);
			if (i < arr.length - 1)
			{
				w.append(",");
				if (config.isPrettyPrint())
				{
					w.append(" ");
				}
			}
		}
		if (config.isPrettyPrint())
		{
			w.append(" ");
		}
		w.append("]");
	}

	/**
	 * Gets the separator.
	 *
	 * @return the separator
	 */
	private static String getSeparator()
	{
		return CommonUtils.DEFAULT_SEPARATOR;
	}
}
