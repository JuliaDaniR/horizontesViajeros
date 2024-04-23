# Horizontes Viajeros
# Sistema de Gestión de Agencia de Turismo

## Descripción
Este proyecto es un sistema de gestión para una agencia de turismo que permite a sus empleados vender productos y servicios turísticos a sus clientes. El sistema incluye funcionalidades para el registro de clientes y vendedores, gestión de servicios turísticos, paquetes turísticos, ventas, y operaciones de altas, bajas, modificaciones y consultas. Los clientes pueden registrarse y acceder a sistema sin necesidad de verificación. En cambio si el usuario quiere acceder como vendedor, el admin será el encargado de dar de alta o no al nuevo vendedor. También cuenta con un carrito de compras en el que el cliente puede escoger uno o varios de los servicios disponibles, seleccionar el método de pago y realizar la compra. En el sector del admin tiene acceso a un reporte que muestra la cantidad de ventas, comisiones y ganancias obtenidas separadas de manera diaria, mensual y anual. El reporte cuenta con un filtro que se pueden ingresar dos fechas y el modo de pago y muestra las tablas filtradas. Todas las tablas de servicios tienen su filtro correspondiente por tipo de servicio, por país de destino, por más vendidos y por los más populares.

## Funcionalidades Principales
- **Servicios Turísticos:**
  - Registro y gestión de servicios como hotel, alquiler de auto, pasajes de colectivo, avión, tren, excursiones, y entradas a eventos.
  - Posibilidad de contratar servicios de forma individual o en paquetes turísticos.
- **Paquetes Turísticos:**
  - Combinación de dos o más servicios turísticos en un paquete.
  - Cálculo automático del costo del paquete aplicando un 10% de descuento.
- **Clientes:**
  - Registro de nuevos clientes con información como nombre, apellido, dirección, DNI, fecha de nacimiento, nacionalidad, celular y email.
- **Vendedores:**
  - Registro de empleados con información similar a la de los clientes, además de datos como cargo y sueldo.
- **Administrador:**
  - Registro de nuevos admin con información como nombre, apellido, dirección, DNI, fecha de nacimiento, nacionalidad, celular y email.
  - El admin es el único que puede modificar, eliminar y dar de alta a nuevos usuarios.
- **Ventas:**
  - Realización de ventas de servicios y paquetes turísticos.
  - Registro de datos de la venta como número de venta, fecha, medio de pago, cliente, servicio/paquete y empleado asociado.

## Tecnologías Utilizadas
- **Spring Boot:** Framework de desarrollo rápido de aplicaciones en Java.
- **Thymeleaf:** Motor de plantillas para la generación de vistas HTML en aplicaciones web.
- **Java:** Lenguaje de programación principal utilizado en el backend.
- **HTML/CSS:** Tecnologías para la creación de interfaces de usuario en el frontend.

## Instalación y Uso
1. Clona el repositorio a tu máquina local.
   ```bash
   git clone https://github.com/JuliaDaniR/horizontesViajeros
2. Importa el proyecto en tu IDE de desarrollo (Eclipse, IntelliJ IDEA, etc.).
3. Configura la base de datos según el archivo de configuración application.properties.
4. Ejecuta la aplicación desde tu IDE.
5. Accede a la aplicación desde tu navegador web utilizando la URL proporcionada por Spring Boot (generalmente http://localhost:8080).
