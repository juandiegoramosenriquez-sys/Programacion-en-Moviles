Pantalla inicial

app resultado 
![img_1.png](img_1.png)

Lab03 - Registro de Producto

Nombre: Juan Diego Ramos Enriquez

Descripcion:
El funcionamiento de mi app, primero pongo el nombre de mi producto luego el precio
y la cantidad y al agregar mi producto lo que me mostrara sera 
el producto el precio la cantidad y el importe que sera la multiplicacion de
el precio con la cantidad

Capturas

Pantalla inicial:
![img.png](img.png)

Despues de presionar:
![img_1.png](img_1.png)

Reflexión: ¿Qué pasa sin remember?
Sin remember, la app pierde el estado en cada recomposición. 
Al presionar una tecla, la variable se reinicia a su valor 
por defecto (""), impidiendo que el usuario pueda escribir.

Reflexión: ¿Por qué se borran los datos al girar la pantalla?
Al girar el teléfono ocurre un cambio de configuración que destruye 
y vuelve a crear la Activity. Como remember solo conserva los datos 
mientras la pantalla no se destruya, la información se pierde. Para 
solucionarlo se usa rememberSaveable, el cual guarda el estado en un 
Bundle y permite conservar los datos tras la rotación.


Mejora con IA (Rama con-ia)

Prompt que usé:
Agrega validación de campos vacíos, números inválidos y un botón Limpiar 
en PantallaRegistro usando Jetpack Compose.

Qué generó Gemini:
Lógica con condicionales para validar texto vacío/nulos, mensaje de error 
en color rojo y un `OutlinedButton`de Limpiar.

Qué acepté o corregí (y por qué):
Acepté las validaciones y el botón de Limpiar. Corregí el uso de`remember` 
reemplazándolo por `rememberSaveable` para evitar la pérdida de datos 
tras un cambio de configuración (rotación).
aunque ya estaba desde la primera rama y ya lo tenia en cuenta
imagen:
![img_2.png](img_2.png)
imagen 2:
![img_3.png](img_3.png)
![img_4.png](img_4.png)