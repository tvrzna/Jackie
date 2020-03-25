package cz.tvrzna.jackie;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommonUtils
{
	protected static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";
	protected static final String DEFAULT_SEPARATOR = "\"";
	protected static final List<Class<?>> SIMPLE_CLASSES = Arrays.asList(String.class, Boolean.class, Short.class, Integer.class, Long.class, Number.class, Float.class, Double.class,
			Date.class, boolean.class, short.class, int.class, long.class, float.class, double.class);

	private CommonUtils()
	{
	}

	/**
	 * Gets the fields.
	 *
	 * @param clazz
	 *          the clazz
	 * @return the fields
	 */
	protected static List<Field> getFields(Class<?> clazz)
	{
		List<Field> result = new ArrayList<>();
		List<String> lstFieldNames = new ArrayList<>();

		Class<?> current = clazz;
		while (current.getSuperclass() != null && !SIMPLE_CLASSES.contains(current))
		{
			for (Field field : current.getDeclaredFields())
			{
				if (!lstFieldNames.contains(field.getName()) && !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
				{
					result.add(field);
					lstFieldNames.add(field.getName());
				}
			}
			current = current.getSuperclass();
		}
		return result;
	}
}
