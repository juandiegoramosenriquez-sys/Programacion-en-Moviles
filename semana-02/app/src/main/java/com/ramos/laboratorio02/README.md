Prompts de IA - Laboratorio 02 Carrito de Compras (version con IA)

Nombre: Juan Diego Ramos Enriquez
Curso: Programacion en Moviles

A continuacion se detallan los prompts utilizados para generar cada parte
del proyecto aplicando los 4 pilares de la Programacion Orientada a Objetos.

Prompt 1 - Clase abstracta (Abstraccion)
Tengo un programa de carrito de compras en Kotlin hecho con una data class
Producto simple. Ayudame a convertirlo en una clase abstracta llamada
Producto que aplique el pilar de abstraccion, con atributos privados
(nombre, precio, cantidad), sus getters y setters, un metodo para calcular
el importe, y un metodo abstracto calcularDescuento() que cada tipo de
producto implementara distinto.

Prompt 2 - Subclases (Herencia)
Ahora que tengo la clase abstracta Producto, genera tres subclases que
apliquen herencia: ProductoElectronico, ProductoRopa y ProductoAlimento.
Cada una debe heredar de Producto y recibir los mismos parametros del
constructor (nombre, precio, cantidad).

Prompt 3 - Descuentos distintos y clase Carrito (Polimorfismo)
Para cada subclase de Producto, implementa el metodo calcularDescuento()
con una logica distinta: 5% para ProductoElectronico, 10% para
ProductoRopa, y 0% para ProductoAlimento. Luego crea una clase Carrito
que guarde una lista de productos y calcule el descuento total recorriendo
la lista, aprovechando que cada producto aplica su propio calculo
(polimorfismo).

Prompt 4 - Refuerzo de atributos privados (Encapsulamiento)
Revisa mi clase Producto y asegurate de que aplique correctamente
encapsulamiento: los atributos deben ser privados y solo accesibles
mediante metodos publicos. Agrega un metodo adicional que devuelva una
descripcion legible del producto usando esos atributos privados.

Prompt 5 - Funcion principal del programa
Genera la funcion main() que use la clase Carrito: agrega varios productos
de distintos tipos (electronico, ropa, alimento), muestre el detalle,
calcule subtotal, IGV del 18%, descuento total y total final, y al final
elimine un producto del carrito y muestre el detalle actualizado.

Prompt 6 - Documentacion del proyecto
Ayudame a redactar un README.md para este proyecto que incluya el titulo
del laboratorio, mi nombre, una descripcion del programa, los 4 pilares
de POO aplicados (abstraccion, herencia, polimorfismo, encapsulamiento)
con un ejemplo de cada uno en el codigo, y un espacio para la captura de
consola del resultado final.