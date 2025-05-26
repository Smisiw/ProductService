package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "attributes")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NonNull
    private String name;
}