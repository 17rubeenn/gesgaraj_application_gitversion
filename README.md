░██████╗░███████╗░██████╗░██████╗░░█████╗░██████╗░░█████╗░░░░░░██╗
██╔════╝░██╔════╝██╔════╝██╔════╝░██╔══██╗██╔══██╗██╔══██╗░░░░░██║
██║░░██╗░█████╗░░╚█████╗░██║░░██╗░███████║██████╔╝███████║░░░░░██║
██║░░╚██╗██╔══╝░░░╚═══██╗██║░░╚██╗██╔══██║██╔══██╗██╔══██║██╗░░██║
╚██████╔╝███████╗██████╔╝╚██████╔╝██║░░██║██║░░██║██║░░██║╚█████╔╝
░╚═════╝░╚══════╝╚═════╝░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝░╚════╝░

Manual de Usuario para la Instalación de una Aplicación Android con SQL Server
Requisitos Previos
SQL Server Express 2022 (SQL2022-SSEI-Expr)
SQL Server Management Studio (SSMS-Setup-ENU)

Instalación de SQL Server Express
Descarga el instalador:

Navega a la carpeta D:\SQL2022\Express_ENU y ejecuta el archivo de instalación de SQL Server Express.
Instala SQL Server Express:

Selecciona BASIC en el tipo de instalación.
Elige la ruta de instalación: D:\Program Files\Microsoft SQL Server.
Sigue las instrucciones en pantalla para completar la instalación.
Revisa los logs de instalación en C:\Program Files\Microsoft SQL Server\160\Setup Bootstrap\Log\20240307_004754.

Configuración inicial:
Asegúrate de que la instancia instalada sea SQLEXPRESS.
Verifica que los administradores de SQL incluyan DESKTOP-0FUDOKJ\.
Confirma que las características instaladas incluyan SQLENGINE.
Verifica la versión: 16.0.1000.6, RTM.
Configuración de SQL Server
Habilitar el usuario 'sa' y establecer contraseña:

Accede a SQL Server Management Studio (SSMS).
Conéctate al servidor con el nombre localhost\SQLEXPRESS.
Navega a Security -> Logins.
Haz clic derecho en el usuario sa y selecciona Properties.
Establece la contraseña.
En Status, asegúrate de que Login esté habilitado.
Configurar la autenticación:

En SSMS, haz clic derecho en el servidor y selecciona Properties.
En la pestaña Security, selecciona SQL Server and Windows Authentication mode.
Habilitar TCP/IP:

Abre SQL Server Configuration Manager.
Navega a SQL Server Network Configuration -> Protocols for SQLEXPRESS.
Habilita TCP/IP.
Configura las direcciones IP:
En IP All, establece TCP Port a 1433.
Reiniciar el servicio de SQL Server:

En SQL Server Configuration Manager, navega a SQL Server Services.
Selecciona SQL Server (SQLEXPRESS) y reinicia el servicio.
Instalación de SQL Server Management Studio (SSMS)
Descarga e instala SSMS:
Navega a la carpeta D:\Program Files (x86)\Microsoft SQL Server Management Studio 19.
Ejecuta el archivo SSMS-Setup-ENU y sigue las instrucciones en pantalla para completar la instalación.
Verifica la versión instalada: Release 19.3.
Configuración de SSMS
Conectar a la instancia de SQL Server:

Abre SSMS.
Conéctate al servidor localhost\SQLEXPRESS utilizando el usuario sa y la contraseñ5.
Configuración adicional:

Asegúrate de que todas las configuraciones anteriores estén correctamente aplicadas.
Configuración de la Conexión de la Aplicación Android
Modificar la cadena de conexión:

En el archivo de configuración de la aplicación Android, establece la cadena de conexión:
arduino
Copiar código
Server=localhost\SQLEXPRESS;Database=master;Trusted_Connection=True;
Asegurar que el dispositivo Android pueda acceder al servidor:

Si el servidor SQL y la aplicación Android están en la misma red local, asegúrate de que el firewall del servidor permita el acceso al puerto 1433.
Si la aplicación Android se conecta de forma remota, asegúrate de que la dirección IP pública del servidor SQL esté configurada correctamente en la cadena de conexión y que el puerto 1433 esté abierto y redirigido correctamente en el router.








