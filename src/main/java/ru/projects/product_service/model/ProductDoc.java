package ru.projects.product_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Document(indexName = "products_index")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Setting(settingPath = "/elasticsearch-settings.json")
@AllArgsConstructor
public class ProductDoc {
    @Id
    private UUID id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Double)
    private BigDecimal price;
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<AttributeValueDoc> AttributeValues;

}
