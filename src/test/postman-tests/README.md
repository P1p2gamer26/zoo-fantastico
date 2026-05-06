# Pruebas de Sistema con Postman

## Archivos incluidos:
- `Zoo_Fantastico_CRUD_Tests.postman_collection.json` - Colección completa de requests
- `Zoo_Local.postman_environment.json` - Variables de entorno
- `test_results.json` - Resultados de la ejecución
- `screenshots/` - Capturas de pantalla de las pruebas

## Resultados:
- Total de tests: 15
- Tests pasados: 13 (86.7%)
- Tests fallidos esperados: 2
  - DELETE Critical Creature (validación correcta)
  - DELETE Zone with Creatures (validación correcta)

## Cómo importar en Postman:
1. Abrir Postman
2. File → Import
3. Seleccionar `Zoo_Fantastico_CRUD_Tests.postman_collection.json`
4. Importar también `Zoo_Local.postman_environment.json`
5. Seleccionar el environment "Zoo Local"
6. Run collection
