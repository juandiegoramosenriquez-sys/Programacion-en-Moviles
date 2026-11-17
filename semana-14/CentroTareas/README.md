# CentroTareas

## Observación del Paso 1

Al pulsar **Tarea larga (MAL)** la pantalla se congela durante 4 segundos: el contador **Tócame** no suma y ningún botón responde.

No se llega a ver **Trabajando...** porque `Thread.sleep(4000)` bloquea el hilo principal, que es el mismo que dibuja la pantalla. El texto cambia en el estado, pero Android no puede redibujar hasta que termina la espera, y para entonces el mensaje ya es **Terminó**.

Si la espera fuera de 10 segundos, Android mostraría el aviso **"La aplicación no responde" (ANR)**, con las opciones de esperar o cerrar la app.
