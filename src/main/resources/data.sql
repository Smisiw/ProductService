INSERT INTO attributes values ('737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Цвет'),
                              ('bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'Размер')
on conflict do nothing;
INSERT INTO categories values ('d8018b6f-241f-41e6-b2d1-207e3c7d0253', 'Одежда', 'clothes',null),
                              ('747f3aa9-4988-4dea-a609-f6ab5e94a5bf', 'Верхняя одежда', 'outerwear', 'd8018b6f-241f-41e6-b2d1-207e3c7d0253'),
                              ('b878fcb2-d1f3-4c92-b09b-83c481f36957', 'Футболки', 't-shirts', '747f3aa9-4988-4dea-a609-f6ab5e94a5bf'),
                              ('fac3cbd1-4db6-4671-926d-7cd752f13261', 'Головные уборы', 'headwear', 'd8018b6f-241f-41e6-b2d1-207e3c7d0253'),
                              ('68dd43bb-64c6-4b3f-abbe-3a8bd44e7848', 'Кепки', 'caps', 'fac3cbd1-4db6-4671-926d-7cd752f13261')
on conflict do nothing;
INSERT INTO products values ('89556bc4-87bf-40e2-add3-e69370291a0a', '24da571a-8ca1-42aa-a650-133dffea4197', 'b878fcb2-d1f3-4c92-b09b-83c481f36957', now(), 'Футболка оверсайз')
on conflict do nothing;
INSERT INTO product_variations values ('6aefdbc1-f5b2-401a-99a6-2ef100c9093a','89556bc4-87bf-40e2-add3-e69370291a0a','Футболка оверсайз красная XL', 'Футболка из качественных материалов', 1500, 33, 0),
                                      ('b1049428-f406-475a-9459-49b3fe1220f0','89556bc4-87bf-40e2-add3-e69370291a0a','Футболка оверсайз красная L', 'Футболка из качественных материалов', 1500, 44, 0),
                                      ('758ab5d5-855e-446e-89c8-5f67761cf00e','89556bc4-87bf-40e2-add3-e69370291a0a','Футболка оверсайз синяя XL', 'Футболка из качественных материалов', 1450, 22, 0),
                                      ('3a1fcf73-8697-4430-b82a-56c39911cace','89556bc4-87bf-40e2-add3-e69370291a0a','Футболка оверсайз синяя L', 'Футболка из качественных материалов', 1450, 22, 0)
on conflict do nothing;
INSERT INTO attribute_values values ('6aefdbc1-f5b2-401a-99a6-2ef100c9093a', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Красный'),
                                    ('6aefdbc1-f5b2-401a-99a6-2ef100c9093a', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'XL'),
                                    ('b1049428-f406-475a-9459-49b3fe1220f0', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Красный'),
                                    ('b1049428-f406-475a-9459-49b3fe1220f0', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'L'),
                                    ('758ab5d5-855e-446e-89c8-5f67761cf00e', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Синий'),
                                    ('758ab5d5-855e-446e-89c8-5f67761cf00e', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'XL'),
                                    ('3a1fcf73-8697-4430-b82a-56c39911cace', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Синий'),
                                    ('3a1fcf73-8697-4430-b82a-56c39911cace', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'L')
on conflict do nothing;

-- Новый продукт: Бейсболка "Street Style"
INSERT INTO products VALUES
    ('b93b8a72-2713-4ce6-9e8a-0e8fbcbe7e14', '24da571a-8ca1-42aa-a650-133dffea4197', '68dd43bb-64c6-4b3f-abbe-3a8bd44e7848', now(), 'Бейсболка Street Style')
ON CONFLICT DO NOTHING;

-- Вариации бейсболки
INSERT INTO product_variations VALUES
                                   ('2d347d1c-631b-4d4a-8e0d-9a32a12c028f','b93b8a72-2713-4ce6-9e8a-0e8fbcbe7e14','Бейсболка чёрная', 'Классическая чёрная бейсболка', 990, 50, 0),
                                   ('a0142c3d-1580-45a4-b50a-fb1298c6df4c','b93b8a72-2713-4ce6-9e8a-0e8fbcbe7e14','Бейсболка белая', 'Классическая белая бейсболка', 990, 35, 0)
ON CONFLICT DO NOTHING;

-- Атрибуты бейсболок
INSERT INTO attribute_values VALUES
                                 ('2d347d1c-631b-4d4a-8e0d-9a32a12c028f', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Чёрный'),
                                 ('2d347d1c-631b-4d4a-8e0d-9a32a12c028f', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'M'),
                                 ('a0142c3d-1580-45a4-b50a-fb1298c6df4c', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Белый'),
                                 ('a0142c3d-1580-45a4-b50a-fb1298c6df4c', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'M')
ON CONFLICT DO NOTHING;

-- Куртка в категорию Верхняя одежда
INSERT INTO products VALUES
    ('a91ecfd5-bcb9-4e52-982f-bacc07d799c0', '24da571a-8ca1-42aa-a650-133dffea4197', '747f3aa9-4988-4dea-a609-f6ab5e94a5bf', now(), 'Зимняя куртка мужская')
ON CONFLICT DO NOTHING;

INSERT INTO product_variations VALUES
                                   ('912bd39d-3d9b-446f-91fb-6c83dfb41ba9', 'a91ecfd5-bcb9-4e52-982f-bacc07d799c0', 'Зимняя куртка черная XL', 'Утеплённая куртка из полиэстера', 7200, 15, 0),
                                   ('39f96e56-38fa-4f89-a356-d88d89a546d3', 'a91ecfd5-bcb9-4e52-982f-bacc07d799c0', 'Зимняя куртка синяя L', 'Утеплённая куртка из полиэстера', 7200, 20, 0)
ON CONFLICT DO NOTHING;

INSERT INTO attribute_values VALUES
                                 ('912bd39d-3d9b-446f-91fb-6c83dfb41ba9', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Чёрный'),
                                 ('912bd39d-3d9b-446f-91fb-6c83dfb41ba9', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'XL'),
                                 ('39f96e56-38fa-4f89-a356-d88d89a546d3', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Синий'),
                                 ('39f96e56-38fa-4f89-a356-d88d89a546d3', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'L')
ON CONFLICT DO NOTHING;

-- Кепка (другая модель) в Кепки
INSERT INTO products VALUES
    ('faed2f0a-87ad-4ad0-bd25-0a3e159c6a2e', '24da571a-8ca1-42aa-a650-133dffea4197', '68dd43bb-64c6-4b3f-abbe-3a8bd44e7848', now(), 'Кепка Urban Classic')
ON CONFLICT DO NOTHING;

INSERT INTO product_variations VALUES
    ('77de6c64-68db-4e01-9e0d-d7e33b3f46fb', 'faed2f0a-87ad-4ad0-bd25-0a3e159c6a2e', 'Кепка белая', 'Белая кепка с прямым козырьком', 950, 50, 0)
ON CONFLICT DO NOTHING;

INSERT INTO attribute_values VALUES
                                 ('77de6c64-68db-4e01-9e0d-d7e33b3f46fb', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Белый'),
                                 ('77de6c64-68db-4e01-9e0d-d7e33b3f46fb', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'M')
ON CONFLICT DO NOTHING;

-- Панама в Головные уборы
INSERT INTO products VALUES
    ('c2d74ed6-fefc-4dc2-b5d0-e51d3d77c345', '24da571a-8ca1-42aa-a650-133dffea4197', 'fac3cbd1-4db6-4671-926d-7cd752f13261', now(), 'Панама летняя')
ON CONFLICT DO NOTHING;

INSERT INTO product_variations VALUES
    ('d4cc0cfc-4984-4ce7-9e52-e13a9e8a1de0', 'c2d74ed6-fefc-4dc2-b5d0-e51d3d77c345', 'Панама бежевая', 'Лёгкая панама для жары', 600, 35, 0)
ON CONFLICT DO NOTHING;

INSERT INTO attribute_values VALUES
                                 ('d4cc0cfc-4984-4ce7-9e52-e13a9e8a1de0', '737b8979-14f1-427c-8974-c8dc2a79c5bc', 'Бежевый'),
                                 ('d4cc0cfc-4984-4ce7-9e52-e13a9e8a1de0', 'bec311f2-c41b-4df2-a5c2-b8621f2431b5', 'S')
ON CONFLICT DO NOTHING;