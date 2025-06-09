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
