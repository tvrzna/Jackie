# Jackie
[![javadoc](https://javadoc.io/badge2/cz.tvrzna/jackie/0.4.0/javadoc.svg)](https://javadoc.io/doc/cz.tvrzna/jackie/0.4.0)

Simple JSON serialization and deserialization from/to object.

## What is Jackie good for?
Yet another JSON object serializer/deserializer, that is small and relatively fast.

|   | **Simple array from JSON** | **Complex object from JSON** | **Simple array to JSON** | **Complex object to JSON** |
--- | --- | --- | --- | ---
| **Jackie** | 3ms | 19ms | 2ms | 3ms |
| **Gson** | 59ms | 66ms | 60ms | 60ms |

## Installation
```xml
<dependency>
    <groupId>cz.tvrzna</groupId>
    <artifactId>jackie</artifactId>
    <version>0.4.0</version>
</dependency>
```

## Example
Let have sample object class, that defines primitive structure.

__JackieExample.java__
```java
package test.project;

import java.util.Date;
import cz.tvrzna.jackie.Jackie;

public class JackieExample
{
	private Long id;
	private String name;
	private boolean active;
	private Date createDate;

	public JackieExample()
	{
	}

	public JackieExample(Long id, String name, boolean active, Date createDate)
	{
		this.id = id;
		this.name = name;
		this.active = active;
		this.createDate = createDate;
	}
}

```

#### Object To JSON

```java
Jackie jackie = new Jackie();

JackieExample example = new JackieExample(123456789l, "Jackie's example", true, new Date());
System.out.println(jackie.toJson(example));
```

and the result is

```json
{"id":123456789,"name":"Jackie's example","active":true,"createDate":"2020-03-10T13:43:24"}
```

#### JSON To Object
```java
final String json = "[{\"id\":123456789,\"name\":\"Jackie's example\",\"active\":true,\"createDate\":\"2020-03-10T13:43:24\"}]";

Jackie jackie = new Jackie();

JackieExample example = jackie.fromJson(json, JackieExample.class);
````

#### JSON To List
```java
final String json = "[{\"id\":123456789,\"name\":\"Jackie's example\",\"active\":true,\"createDate\":\"2020-03-10T13:43:24\"}]";

Jackie jackie = new Jackie();

List<JackieExample> lstExamples = jackie.fromJson(json, List.class, JackieExample.class);
````

#### JSON To Map
```java
final String json = "{0: {\"id\":123456789,\"name\":\"Jackie's example\",\"active\":true,\"createDate\":\"2020-03-10T13:43:24\"}}";

Jackie jackie = new Jackie();

Map<Integer, JackieExample> mapExample = jackie.fromJson(json, Map.class, Integer.class, JackieExample.class);
````