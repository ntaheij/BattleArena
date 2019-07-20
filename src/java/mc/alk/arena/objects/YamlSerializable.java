package mc.alk.arena.objects;

import mc.alk.arena.objects.exceptions.SerializationException;

import java.util.Map;

public interface YamlSerializable {

    Object yamlToObject(Map<String,Object> map, String value) throws SerializationException;
    Object objectToYaml() throws SerializationException;
}
