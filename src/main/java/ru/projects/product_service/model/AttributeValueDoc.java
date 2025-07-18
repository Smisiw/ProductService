package ru.projects.product_service.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "attribute_values_index")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Setting(settingPath = "/elasticsearch-settings.json")
@AllArgsConstructor
public class AttributeValueDoc {
    @Field(type = FieldType.Text)
    private String attributeName;
    @Field(type = FieldType.Text)
    private String attributeValue;
}
