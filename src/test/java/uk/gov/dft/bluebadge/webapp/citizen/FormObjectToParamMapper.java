package uk.gov.dft.bluebadge.webapp.citizen;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

public class FormObjectToParamMapper {

    public static MultiValueMap<String, String> convert(Object form) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(form, HashMap.class);
        LinkedMultiValueMap<String, String> targetMap = new LinkedMultiValueMap();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            targetMap.add(entry.getKey(), null == entry.getValue() ? null : entry.getValue().toString());
        }

        return targetMap;
    }
}
