# practicas-dgp

## Subir archivos al servidor

El servidor tiene dos vistas:

* La vista test (test.dgp.esy.es) para ir probando cosas nosotros
* La vista demo (demo.dgp.esy.es) para ir mostrándosela al cliente según vayamos teniendo diferentes versiones

Ejecutar el script, para subir los archivos de la carpeta `server` al servidor:

`./subir.sh demo` para subir los archivos a demo.dgp.esy.es

`./subir.sh test` para subir los archivos a test.dgp.esy.es

## Uso de GitHub

```
# Para descargar el repositorio

git clone https://github.com/corderop/practicas-dgp.git
```

Una vez lo tienes:

(Todos los comandos se tienen que ejecutar en la carpeta del repositorio, no en alguna subcarpeta como puede ser `server` o `base-de-datos`)

```
# Para descargar la última versión de cambios del servidor 
# sin tu haber editado nada desde el último commit que hiciste

git pull
```

```
# Para añadir un cambio

git stash # Guarda por un momento los cambios que tu has hecho y los elimina de los archivos
git pull # Descargarte la última versión por si alguien ha modificado algo
git stash pop # Volver a poner en los archivos lo que tu has cambiado

# Esto puede generar que la última versión y tus cambios sean incompatibles. 
# Por ejemplo si habéis modificado la misma línea de dos formas diferentes te aparecerá en el 
# código algo del estilo:

<<<<<< HEAD
    Código hecho por persona 1
======
    Código hecho por persona 2
>>>>>>

# Si esto ocurre, cosa que tampoco es muy común, tienes que quedarte con uno de los dos código 
# que muestra, borrando todo lo demás. Por ejemplo si te quieres quedar con el código hecho por la persona 2,
# borras todo y quedaría:

    Código hecho por persona 2

# Una vez hecho todo esto ya puedes hacer el commit para registrar tu cambio y subirlo al servidor

git add .
git commit # Escribes un título representativo de lo que has hecho y guardas
git push # Para subirlo al servidor
```