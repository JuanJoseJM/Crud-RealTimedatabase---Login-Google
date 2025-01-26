# Guía de Elaboración de la Aplicación

## 1. Estructura General

La aplicación está dividida en varias actividades y clases para manejar diferentes funcionalidades:

- **MainActivity**: Maneja la autenticación de Google.
- **PrincipalActivity**: Es la actividad principal donde se muestra la lista de artículos.
- **AddActivity**: Permite agregar o editar artículos.
- **ArticuloAdapter** y **ArticuloViewHolder**: Manejan la visualización de los artículos en un RecyclerView.
- **Articulo**: Es un modelo de datos que representa un artículo en la agenda.
- **ArticuloProviders**: Es una clase que gestiona la obtención de datos de Firebase.

## 2. Flujo de la Aplicación

### a) Autenticación con Google (MainActivity)

- Cuando el usuario abre la aplicación, la primera pantalla es la de login (MainActivity).
- En el botón de "Login" se inicia el flujo de autenticación de Google mediante la clase `GoogleSignInOptions`.
- Si la autenticación es exitosa, se redirige al usuario a la **PrincipalActivity** donde podrá gestionar los artículos.

**Claves importantes:**
- `GoogleSignInOptions` y `GoogleAuthProvider.getCredential` permiten la autenticación con Firebase.
- `responseLauncher` maneja la respuesta de la actividad de autenticación.

### b) Pantalla Principal de la Aplicación (PrincipalActivity)

- **PrincipalActivity** es donde se manejan los artículos. Aquí se muestra una lista de artículos y se pueden realizar acciones sobre ellos (editar, borrar).
- La clase **ArticuloAdapter** se utiliza para mostrar los artículos en un RecyclerView, configurado con un `LinearLayoutManager` para que los elementos se muestren verticalmente.
- La base de datos Firebase se gestiona con `FirebaseDatabase.getInstance().getReference("agenda")`.
- `setRecycler()` y `recuperarDatosAgenda()` gestionan la carga de datos de la base de datos Firebase.
- `borrarItem()` y `editarItem()` permiten eliminar y editar los artículos.

**Claves importantes:**
- El RecyclerView utiliza un Adapter (**ArticuloAdapter**) y un ViewHolder (**ArticuloViewHolder**).
- El uso de `FirebaseDatabase` permite leer y escribir datos en Firebase en tiempo real.

### c) Agregar o Editar Artículos (AddActivity)

- En **AddActivity**, se permiten dos acciones:
  - **Agregar un nuevo artículo**: Si el intent no lleva datos previos, se permite ingresar un nuevo artículo.
  - **Editar un artículo existente**: Si el intent trae datos, se permite editar un artículo ya existente. En este caso, el nombre no puede modificarse (es único para cada artículo), pero la descripción y el precio sí.

**Claves importantes:**
- `addItem()` es la función que guarda o actualiza los datos en Firebase.
- Se hace una validación básica para asegurar que los datos introducidos sean correctos (`datosOk()`).

### d) Modelo Articulo

- **Articulo** es una clase que representa un artículo. Tiene tres propiedades:
  - **nombre**: El nombre del artículo.
  - **descripcion**: Una descripción del artículo.
  - **precio**: El precio del artículo.

**Claves importantes:**
- `Serializable` se usa para pasar objetos de tipo **Articulo** entre actividades.

## 3. Base de Datos Firebase

Se usa Firebase Realtime Database para almacenar y recuperar los datos. En la base de datos, los artículos se almacenan bajo el nodo **agenda**, y cada artículo se guarda usando su nombre (modificado para evitar problemas con los espacios).

- `addItem()`: Se utiliza para insertar un nuevo artículo en la base de datos de Firebase o para actualizar uno existente.
- `recuperarDatosAgenda()`: Recupera todos los artículos desde la base de datos y los muestra en el RecyclerView.

## 4. Interacción con el Usuario

- En la **PrincipalActivity**, se tiene un menú lateral donde el usuario puede:
  - **Cerrar sesión**: Mediante la opción de logout.
  - **Salir**: Cierra la aplicación completamente.
  - **Borrar todos los artículos**: Elimina todos los artículos de la base de datos.
